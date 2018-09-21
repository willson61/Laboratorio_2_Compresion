package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressHuffResult extends AppCompatActivity {

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
    public static String txtBinario;
    public static String txtAscii;
    public static String filePath;
    public static String fileName;

    @BindView(R.id.labelBinario)
    TextView labelBinario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_huff_result);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        labelBinario.setMovementMethod(new ScrollingMovementMethod());
        String[] prueba = CompressHuffResult.file.getPath().split("/");
        fileName = prueba[prueba.length - 1].replace(".txt", "");
        String prueba2 = prueba[prueba.length - 1].replace(".txt", ".huff");
        labelNombre.setText(prueba2);
        labelContenido.setText(txtAscii);
        labelBinario.setText(txtBinario);
        filePath = CompressHuffResult.file.getPath().replace(prueba[prueba.length - 1], prueba2);
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
                        startActivity(new Intent(CompressHuffResult.this, MainActivity.class));
                    }
                }).create().show();
    }

    @OnClick({R.id.btnGuardar, R.id.btnBorrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                /*Context context = getApplicationContext();
                File path = context.getExternalFilesDir(null);
                if (null == path) {
                    path = context.getFilesDir();
                }
                String[] pr = CompressHuffResult.file.getPath().split("/");
                String[] pr2 = pr[2].split(":");
                String pt = Environment.getExternalStorageDirectory().toString().replace("0", pr2[0]);
                File Dir = new File(path,labelNombre.getText().toString());*/
                File root = new File(Environment.getExternalStorageDirectory(), "Documentos");
                root.mkdirs();
                File archivoHuff = new File(root, labelNombre.getText().toString());
                try {
                    if (archivoHuff.exists()) {
                        archivoHuff.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(archivoHuff, true);
                    fos.write(createTextForFile().getBytes());
                    fos.flush();
                    fos.close();
                    archivoHuff.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (archivoHuff.exists()) {
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    borrarTodo();
                    finish();
                    startActivity(new Intent(CompressHuffResult.this, MainActivity.class));
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
                    message.show();
                    borrarTodo();
                    finish();
                    startActivity(new Intent(CompressHuffResult.this, MainActivity.class));
                }
                break;
            case R.id.btnBorrar:
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a borrado exitosamente", Toast.LENGTH_LONG);
                message.show();
                borrarTodo();
                finish();
                startActivity(new Intent(CompressHuffResult.this, MainActivity.class));
                break;
        }
    }

    public void borrarTodo() {
        CompressHuffResult.txtAscii = null;
        CompressHuffResult.cerosExtra = 0;
        CompressHuffResult.ListaNodosConCodigos = new ArrayList<>();
        CompressHuffResult.ListaNodos = new ArrayList<>();
        CompressHuffResult.file = null;
        CompressHuffResult.arbol = new ArbolHuffman();
        CompressHuffResult.txtBinario = null;
        CompressHuffResult.fileName = null;
        CompressHuffResult.filePath = null;
        CompressHuffResult.ListaCaracteres = new ArrayList<>();
    }

    public String createTextForFile() {
        String txtArchivo = "";
        for (int i = 0; i < CompressHuffResult.ListaNodosConCodigos.size(); i++) {
            txtArchivo += CompressHuffResult.ListaNodosConCodigos.get(i).getCaracter() + "#~#" + Float.toString(CompressHuffResult.ListaNodosConCodigos.get(i).getProbabilidad());
            if (i != CompressHuffResult.ListaNodosConCodigos.size() - 1) {
                txtArchivo += "/¬/";
            }
        }
        txtArchivo += "%~%";
        txtArchivo += cerosExtra;
        txtArchivo += "%~%" + CompressHuffResult.txtAscii;
        return txtArchivo;
    }
}
