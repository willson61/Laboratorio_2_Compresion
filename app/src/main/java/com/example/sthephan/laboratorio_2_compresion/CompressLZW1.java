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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressLZW1 extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    private static Charset UTF8 = Charset.forName("UTF-8");
    private static Charset ISO = Charset.forName("ISO-8859-1");
    public static LZW diccionario = new LZW();
    public static Uri file;
    public static int cerosExtra;
    public static int longitudBinario;
    public static String ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_lzw1);
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
                        startActivity(new Intent(CompressLZW1.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringbuilder = new StringBuilder();
        byte[] inputData = getBytes(input);
        int line = 0;
        String text = new String(inputData, ISO);
        return text;
        /*line = input.read();
        while ((line = reader.read()) != -1) {
            Character val = (char) line;
            stringbuilder.append(val);
        }
        input.close();
        reader.close();*/
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnComprimir})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                if(CompressLZW1.file != null){
                    borrarCampos();
                }
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a File"), 123);
                break;
            case R.id.btnCancelar:
                borrarCampos();
                break;
            case R.id.btnComprimir:
                try{
                    if(CompressLZW1.diccionario.DiccionarioO != null){
                        CompressLZW1.diccionario = new LZW();
                    }
                    String compresion = CompressLZW1.diccionario.Comprimir(readTextFromUri(CompressLZW1.file));
                    CompressLZW1.longitudBinario = CompressLZW1.diccionario.longitudMax;
                    CompressLZWResult.caracteresOriginales = CompressLZW1.diccionario.DiccionarioO;
                    CompressLZWResult.codigoLZW = compresion;
                    CompressLZWResult.textAscii = textoToAscii(compresion);
                    CompressLZWResult.file1 = CompressLZW1.file;
                    CompressLZWResult.cerosExtra = CompressLZW1.cerosExtra;
                    CompressLZWResult.longitudBinario = CompressLZW1.longitudBinario;
                    borrarCampos();
                    finish();
                    startActivity(new Intent(CompressLZW1.this, CompressLZWResult.class));

                }catch(Exception e){
                    e.printStackTrace();
                    borrarCampos();
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
                String[] ext = prueba.split("\\.");
                CompressLZW1.ext = ext[1];
                MainActivity.ext = ext[1];
                CompressLZWResult.ext = ext[1];
                Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                message.show();
                labelNombre.setText(prueba);
                labelContenido.setText(readTextFromUri(selectedFile));
                CompressLZW1.file = selectedFile;
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con la compresion", Toast.LENGTH_LONG);
            message.show();
            borrarCampos();
        }
    }

    public void borrarCampos(){
        labelContenido.setText(null);
        labelNombre.setText(null);
        CompressLZW1.diccionario = new LZW();
        CompressLZW1.file = null;
    }

    public String textoToAscii(String txtLZW){
        cerosExtra = 8 - txtLZW.length() % 8;
        if(cerosExtra != 8){
            for(int i = 0; i < cerosExtra; i++){
                txtLZW = "0" + txtLZW;
            }
        }
        else{
            cerosExtra = 0;
        }
        String txtAscii = "";
        int cont = 0;
        String ascii = "";
        int num = 0;
        for (int i = 0; i < txtLZW.length(); i++) {
            cont++;
            if (cont <= 8) {
                ascii = ascii + txtLZW.charAt(i);
                if ((cont == 8)||(i == txtLZW.length() - 1)){
                    num = Integer.parseInt(ascii,2);
                    txtAscii += (char)Integer.valueOf(num).intValue();
                    cont = 0;
                    ascii = "";
                }
            }
        }
        return txtAscii;
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
}
