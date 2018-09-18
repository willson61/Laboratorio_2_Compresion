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
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressHuff1 extends AppCompatActivity {


    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    public static Uri file;
    public static ArrayList<Character> ListaCaracteres = new ArrayList<>();
    public static ArrayList<NodoHuffman> ListaNodos = new ArrayList<>();
    public static ArrayList<NodoHuffman> ListaNodosConCodigos = new ArrayList<>();
    public static ArbolHuffman arbol = new ArbolHuffman();
    public static int cerosExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_huff1);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
    }


    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnComprimir})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                if(CompressHuff1.file != null){
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
                if(CompressHuff1.file != null){
                    try{
                        if((CompressHuff1.ListaNodos.size() == 0) && (CompressHuff1.ListaCaracteres.size() == 0)){
                            generarProbabilidades(CompressHuff1.file);
                            CompressHuff1.ListaNodosConCodigos = CompressHuff1.arbol.CreacionArbolFinal(CompressHuff1.ListaNodos);
                            labelContenido.setText(crearBinario(readTextFromUri(CompressHuff1.file)));
                        }
                        else{
                            CompressHuff1.ListaCaracteres = new ArrayList<>();
                            CompressHuff1.ListaNodos = new ArrayList<>();
                            generarProbabilidades(CompressHuff1.file);
                            CompressHuff1.ListaNodosConCodigos = CompressHuff1.arbol.CreacionArbolFinal(CompressHuff1.ListaNodos);
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
                if(prueba[prueba.length - 1].contains(".txt")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelNombre.setText(prueba[prueba.length - 1]);
                    labelContenido.setText(readTextFromUri(selectedFile));
                    CompressHuff1.file = selectedFile;
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(CompressHuff1.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringbuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringbuilder.append(line);
        }
        input.close();
        reader.close();
        return stringbuilder.toString();
    }

        private void generarProbabilidades(Uri uri) throws IOException {
            String texto = readTextFromUri(uri);
            char[] txt = texto.toCharArray();
            for (int i = 0; i < txt.length; i++) {
                if (!CompressHuff1.ListaCaracteres.contains(txt[i])) {
                    double cantCar = contarCaracteres(txt[i], txt);
                    double prob = cantCar / (double) txt.length;
                    NodoHuffman nodo = new NodoHuffman();
                    nodo.setCaracter(txt[i]);
                    nodo.setProbabilidad(prob);
                    CompressHuff1.ListaCaracteres.add(txt[i]);
                    CompressHuff1.ListaNodos.add(nodo);
                } else {
                    continue;
                }
            }
        }

        private int contarCaracteres ( char c, char[] text){
            int cont = 0;
            for (int i = 0; i < text.length; i++) {
                if (c == text[i]) {
                    cont++;
                }
            }
            return cont;
        }

        public String pruebaProbabilidades () {
            if((CompressHuff1.ListaNodosConCodigos.size() != 0)){
                CompressHuff1.ListaNodosConCodigos = new ArrayList<>();
                CompressHuff1.arbol = new ArbolHuffman();
                labelContenido.setText(null);
            }
            CompressHuff1.ListaNodosConCodigos = CompressHuff1.arbol.CreacionArbolFinal(CompressHuff1.ListaNodos);
            String st = "";
            for (int i = 0; i < CompressHuff1.ListaNodosConCodigos.size(); i++) {
                st = st.concat(CompressHuff1.ListaNodosConCodigos.get(i).getCaracter() + " = " + CompressHuff1.ListaNodosConCodigos.get(i).getCodigo() + "\n");
            }
            return st;
        }

        public void borrarCampos(){
            labelNombre.setText(null);
            labelContenido.setText(null);
            CompressHuff1.file = null;
            CompressHuff1.ListaCaracteres = new ArrayList<>();
            CompressHuff1.ListaNodos = new ArrayList<>();
            CompressHuff1.arbol = new ArbolHuffman();
            CompressHuff1.ListaNodosConCodigos = new ArrayList<>();
        }

        public String crearBinario(String input){
            String txt = "";
            for(int i = 0; i < input.length(); i++){
                for(NodoHuffman x: CompressHuff1.ListaNodosConCodigos){
                    if(input.charAt(i) == x.getCaracter()){
                        txt += x.getCodigo();
                        break;
                    }
                }
            }
            return txt;
        }

}
