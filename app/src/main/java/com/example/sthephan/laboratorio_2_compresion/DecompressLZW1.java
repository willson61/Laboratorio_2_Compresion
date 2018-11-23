package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecompressLZW1 extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    private static Charset ISO = Charset.forName("ISO-8859-1");
    public static Uri file;
    public static LZW diccionario = new LZW();
    public static int longitudBinario;
    
    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decompress_lzw1);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(DecompressLZW1.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringbuilder = new StringBuilder();
        int line = 0;
        byte[] inputData = getBytes(input);
        String text = new String(inputData, ISO);
        return text;
        /*
        while ((line = reader.read()) != -1) {
            Character val = (char) line;
            stringbuilder.append(val);
        }
        input.close();
        reader.close();
        return text;*/
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnDescomprimir})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a File"), 123);
                break;
            case R.id.btnCancelar:
                labelNombre.setText(null);
                labelContenido.setText(null);
                DecompressLZW1.file = null;
                DecompressLZW1.diccionario = new LZW();
                break;
            case R.id.btnDescomprimir:
                if (DecompressLZW1.file != null){
                    try{
                        if(DecompressLZW1.file != null){
                            String[] ascii = readTextFromUri(DecompressLZW1.file).split("/¬/");
                            DecompressLZW1.longitudBinario = Integer.parseInt(ascii[3]);
                            String texto = DecompressLZW1.diccionario.Descomprimir(extraerNumDeBinario(extraerBinarioDeAscii(ascii[1]).substring(Integer.parseInt(ascii[2]))), ascii[0], ascii[2]);
                            DecompressLZWResult.textoDescompresion = texto;
                            String pr = obtenerNombreDeArchivoDeUri(DecompressLZW1.file);
                            DecompressLZWResult.nombreArchivo = pr.replace(".lzw", "");
                            DecompressLZW1.diccionario = new LZW();
                            DecompressLZW1.file = null;
                            finish();
                            startActivity(new Intent(DecompressLZW1.this, DecompressLZWResult.class));

                        }
                        else{
                            Toast message = Toast.makeText(getApplicationContext(), "Seleccione un archivo para poder descomprimir", Toast.LENGTH_LONG);
                            message.show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            try{
                Uri selectedFile = data.getData();
                String prueba = obtenerNombreDeArchivoDeUri(selectedFile);
                if(prueba.contains(".lzw")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelNombre.setText(prueba);
                    labelContenido.setText(readTextFromUri(selectedFile));
                    DecompressLZW1.file = selectedFile;
                }
                else{
                    labelNombre.setText(null);
                    labelContenido.setText(null);
                    DecompressLZW1.file = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .lzw para descompresion. Por favor seleccione un archivo de extension .lzw", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con la compresion", Toast.LENGTH_LONG);
            message.show();
        }
    }

    public String obtenerNombreDeArchivoDeUri(Uri uri) {
        String displayName = "";
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null, null);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }

    public String extraerBinarioDeAscii(String codigoAscii){
        String txtEnBinario = "";
        for (int i = 0; i < codigoAscii.length(); i++){
            String asciiABinario = Integer.toBinaryString(codigoAscii.charAt(i));
            if (asciiABinario.length() % 8 != 0){
                int cerosRestantes = 8 - asciiABinario.length() % 8;
                for (int j = 0; j < cerosRestantes; j++){
                    asciiABinario = "0" + asciiABinario;
                }
            }
            txtEnBinario += asciiABinario;
        }
        return txtEnBinario;
    }

    public ArrayList<String> extraerNumDeBinario(String codigoBinario){
        String txt="";
        ArrayList<String> txtLZW = new ArrayList<>();
        for(int i = 0; i < codigoBinario.length(); i++){
            txt += codigoBinario.charAt(i);
            if(txt.length() % DecompressLZW1.longitudBinario == 0){
                int num = Integer.parseInt(txt, 2);
                txtLZW.add(Integer.toString(num));
                txt = "";
            }
        }
        return txtLZW;
    }
}
