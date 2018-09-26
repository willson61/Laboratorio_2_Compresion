package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressHuffResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static Uri file1;
    public static Uri file2;
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
        String[] prueba = CompressHuffResult.file1.getPath().split("/");
        fileName = prueba[prueba.length - 1].replace(".txt", "");
        String prueba2 = prueba[prueba.length - 1].replace(".txt", ".huff");
        labelNombre.setText(prueba2);
        labelContenido.setText(txtAscii);
        labelBinario.setText(txtBinario);
        filePath = CompressHuffResult.file1.getPath().replace(prueba[prueba.length - 1], prueba2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data.getData().getPath().contains(".huff")) {
            try{
                Uri selectedFile = data.getData();
                CompressHuffResult.file2 = selectedFile;
                ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(selectedFile, "w");
                FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
                Writer wr = new OutputStreamWriter(fos, UTF8);
                String textContent = createTextForFile();
                wr.write(textContent);
                wr.flush();
                wr.close();
                fos.close();
                file.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con la compresion", Toast.LENGTH_LONG);
            message.show();
        }
        else if (!data.getData().getPath().contains(".huff")){
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .huff para guardar el archivo comprimido", Toast.LENGTH_LONG);
            message.show();
        }
        if (checkURIResource(CompressHuffResult.this.getApplicationContext(), CompressHuffResult.file2)) {
            Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
            message.show();
            escribirAHistorial(crearCompression());
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
    }

    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
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
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TITLE, labelNombre.getText().toString());
                startActivityForResult(intent, 1234);
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
        CompressHuffResult.file1 = null;
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

    public Compress crearCompression(){
        String[] path = CompressHuffResult.file1.getPath().split("/");
        String[] path2 = CompressHuffResult.file2.getPath().split("/");
        Long file1Size;
        Long file2Size;
        Cursor returnCursor1 = getContentResolver().query(CompressHuffResult.file1, null, null, null, null);
        int sizeIndex1 = returnCursor1.getColumnIndex(OpenableColumns.SIZE);
        returnCursor1.moveToFirst();
        file1Size = returnCursor1.getLong(sizeIndex1);
        Cursor returnCursor2 = getContentResolver().query(CompressHuffResult.file2, null, null, null, null);
        int sizeIndex2 = returnCursor2.getColumnIndex(OpenableColumns.SIZE);
        returnCursor2.moveToFirst();
        file2Size = returnCursor2.getLong(sizeIndex2);
        Float raz = (float) file2Size/file1Size;
        Float fac = (float) file1Size/file2Size;
        Compress c = new Compress(path[path.length - 1], path2[path2.length - 1], CompressHuffResult.file2.getPath(), raz * 100, fac, 1 - (raz * 100), "Huffman");
        return c;
    }

    public void escribirAHistorial(Compress com){
        String texto = "";
        String div = "#~#";
        String fin = "%~%";
        texto += com.getNombreOriginal();
        texto += div;
        texto += com.getNombreComprimido();
        texto += div;
        texto += com.getRutaArchivoComprimido();
        texto += div;
        texto += Float.toString(com.getRazonCompresion());
        texto += div;
        texto += Float.toString(com.getFactorCompresion());
        texto += div;
        texto += Float.toString(com.getPorcentajeReduccion());
        texto += div;
        texto += com.getTipoCompresion();
        texto += fin;
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        root.mkdirs();
        File historial = new File(root, "HistorialCompresion.txt");
        try{
            if(!historial.exists()){
                historial.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(historial, true);
            fos.write(texto.toString().getBytes());
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
