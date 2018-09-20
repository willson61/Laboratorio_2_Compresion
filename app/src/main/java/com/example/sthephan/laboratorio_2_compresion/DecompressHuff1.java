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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecompressHuff1 extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    public static Uri file;
    public static ArrayList<NodoHuffman> ListaNodos = new ArrayList<>();
    public static ArrayList<NodoHuffman> ListaNodosConCodigo = new ArrayList<>();
    public static int Salto;
    public static String Texto;
    public static ArbolHuffman arbol = new ArbolHuffman();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decompress_huff1);
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
                        startActivity(new Intent(DecompressHuff1.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringbuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringbuilder.append(line);
        }
        input.close();
        reader.close();
        return stringbuilder.toString();
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
                DecompressHuff1.file = null;
                break;
            case R.id.btnDescomprimir:
                if (DecompressHuff1.file != null){
                    try{
                        if(DecompressHuff1.file != null){
                            DescompresionFinal();
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
                String[] prueba = selectedFile.getPath().split("/");
                if(prueba[prueba.length - 1].contains(".huff")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelNombre.setText(prueba[prueba.length - 1]);
                    labelContenido.setText(readTextFromUri(selectedFile));
                    DecompressHuff1.file = selectedFile;
                }
                else{
                    labelNombre.setText(null);
                    labelContenido.setText(null);
                    DecompressHuff1.file = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .huff para descompresion. Por favor seleccione un archivo de extension .huff", Toast.LENGTH_LONG);
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

    private void obtenerTabla(Uri uri) throws IOException{
        String texto = readTextFromUri(uri);
        String[] tablas = texto.split("%~%");
        String[] tablaProb = tablas[0].split("/¬/");
        for (int i = 0; i < tablaProb.length; i++) {
            String[] probabilidades = tablaProb[i].split("#~#");
            NodoHuffman nuevo = new NodoHuffman();
            nuevo.setCaracter(probabilidades[0].charAt(0));
            nuevo.setProbabilidad(Double.parseDouble(probabilidades[1]));
            DecompressHuff1.ListaNodos.add(nuevo);
        }
        DecompressHuff1.Salto = Integer.parseInt(tablas[1]);
        DecompressHuff1.Texto = tablas[2];
    }

    private void DescompresionFinal() throws IOException{
        obtenerTabla(DecompressHuff1.file);
        DecompressHuff1.ListaNodosConCodigo = DecompressHuff1.arbol.CreacionArbolFinal(DecompressHuff1.ListaNodos);
        String texto = Descomprimir(DecompressHuff1.Texto);
        String[] pr = DecompressHuff1.file.getPath().split("/");
        DecompressHuffResult.txtDescompresion = texto;
        DecompressHuffResult.nombreArchivo = pr[pr.length - 1].replace(".huff", "");
        finish();
        startActivity(new Intent(DecompressHuff1.this, DecompressHuffResult.class));
    }

    private String Descomprimir(String texto){
        char[] txt = extraerBinarioDeAscii(texto).toCharArray();
        String caracter = "";
        String TextoDesc = "";
        for (int i = DecompressHuff1.Salto; i < txt.length; i++){
            caracter += txt[i];
            int j = 0;
            boolean existe = false;
            while (j < DecompressHuff1.ListaNodosConCodigo.size() && !existe){
                if (DecompressHuff1.ListaNodosConCodigo.get(j).getCodigo().equals(caracter)){
                    TextoDesc += DecompressHuff1.ListaNodosConCodigo.get(j).getCaracter();
                    caracter = "";
                    existe = true;
                }
                else{
                    j++;
                }
            }
            /*for (int j = 0; j < DecompressHuff1.ListaNodos.size(); j++){
                if (DecompressHuff1.ListaNodos.get(j).getCodigo().equals(caracter)){
                    TextoDesc += caracter;
                    caracter = "";
                }
            }*/
        }
        return TextoDesc;
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
}
