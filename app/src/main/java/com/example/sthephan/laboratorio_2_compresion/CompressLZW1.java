package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressLZW1 extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    public static LZW diccionario = new LZW();
    public static Uri file;
    public static int cerosExtra;

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
        int line = 0;
        while ((line = reader.read()) != -1) {
            char val = (char) line;
            stringbuilder.append(val);
        }
        input.close();
        reader.close();
        return stringbuilder.toString();
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnComprimir})
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
                borrarCampos();
                break;
            case R.id.btnComprimir:
                try{
                    String compresion = CompressLZW1.diccionario.Comprimir(readTextFromUri(CompressLZW1.file));
                    CompressLZWResult.caracteresOriginales = CompressLZW1.diccionario.DiccionarioO;
                    CompressLZWResult.codigoLZW = compresion;
                    CompressLZWResult.textAscii = textoToAscii(compresion);
                    CompressLZWResult.file1 = CompressLZW1.file;
                    CompressLZWResult.cerosExtra = CompressLZW1.cerosExtra;
                    borrarCampos();
                    finish();
                    startActivity(new Intent(CompressLZW1.this, CompressLZWResult.class));

                }catch(Exception e){
                    e.printStackTrace();
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
                String[] prueba = selectedFile.getPath().split("/");
                if(prueba[prueba.length - 1].contains(".txt")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelNombre.setText(prueba[prueba.length - 1]);
                    labelContenido.setText(readTextFromUri(selectedFile));
                    CompressLZW1.file = selectedFile;
                }
                else{
                    borrarCampos();
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .txt para compresion. Por favor seleccione un archivo de extension .txt", Toast.LENGTH_LONG);
                    message.show();
                }
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

    public String textoToAscii(String txtBinario){
        String txtAscii = txtBinario;
        String ascii = "";
        int num = 0;
        char txt;
        for (int i = 0; i < txtBinario.length(); i++) {
            txt = Character.forDigit(Character.getNumericValue(txtAscii.charAt(i)), 10);
            ascii += (int)txt;
        }
        return ascii;
    }
}
