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
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompressLZWResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.labelLZW)
    TextView labelLZW;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static String codigoLZW;
    public static String textAscii;
    public static String caracteresOriginales;
    public static Uri file1;
    public static Uri file2;
    public static int cerosExtra;
    public static int longitudBinario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_lzwresult);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        labelLZW.setMovementMethod(new ScrollingMovementMethod());
        labelLZW.setText(CompressLZWResult.codigoLZW);
        labelContenido.setText(CompressLZWResult.textAscii);
        String[] prueba = CompressLZWResult.file1.getPath().split("/");
        labelNombre.setText(prueba[prueba.length - 1].replace(".txt", ".lzw"));
        
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
                        startActivity(new Intent(CompressLZWResult.this, MainActivity.class));
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
                startActivity(new Intent(CompressLZWResult.this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data.getData().getPath().contains(".lzw")) {
            try{
                Uri selectedFile = data.getData();
                CompressLZWResult.file2 = selectedFile;
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
        else if (!data.getData().getPath().contains(".lzw")){
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .lzw para guardar el archivo comprimido", Toast.LENGTH_LONG);
            message.show();
        }
        if (checkURIResource(CompressLZWResult.this.getApplicationContext(), CompressLZWResult.file2)) {
            Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
            message.show();
            escribirAHistorial(crearCompression());
            borrarTodo();
            finish();
            startActivity(new Intent(CompressLZWResult.this, MainActivity.class));
        } else {
            Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
            message.show();
            borrarTodo();
            finish();
            startActivity(new Intent(CompressLZWResult.this, MainActivity.class));
        }
    }

    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
    }
    
    public String createTextForFile(){
        String txtArchivo = "";
        txtArchivo += CompressLZWResult.caracteresOriginales;
        txtArchivo += "/¬/";
        txtArchivo += CompressLZWResult.textAscii;
        txtArchivo += "/¬/";
        txtArchivo += cerosExtra;
        txtArchivo += "/¬/";
        txtArchivo += longitudBinario;
        return  txtArchivo;
    }

    public Compress crearCompression(){
        String[] path = CompressLZWResult.file1.getPath().split("/");
        String[] path2 = CompressLZWResult.file2.getPath().split("/");
        Long file1Size;
        Long file2Size;
        Cursor returnCursor1 = getContentResolver().query(CompressLZWResult.file1, null, null, null, null);
        int sizeIndex1 = returnCursor1.getColumnIndex(OpenableColumns.SIZE);
        returnCursor1.moveToFirst();
        file1Size = returnCursor1.getLong(sizeIndex1);
        Cursor returnCursor2 = getContentResolver().query(CompressLZWResult.file2, null, null, null, null);
        int sizeIndex2 = returnCursor2.getColumnIndex(OpenableColumns.SIZE);
        returnCursor2.moveToFirst();
        file2Size = returnCursor2.getLong(sizeIndex2);
        Float raz = (float) file2Size/file1Size;
        Float fac = (float) file1Size/file2Size;
        Compress c = new Compress(path[path.length - 1], path2[path2.length - 1], CompressLZWResult.file2.getPath(), raz, fac, 0, "LZW");
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
    
    public void borrarTodo(){
        CompressLZWResult.caracteresOriginales = null;
        CompressLZWResult.textAscii = null;
        CompressLZWResult.codigoLZW = null;
        CompressLZWResult.file1 = null;
        CompressLZWResult.file2 = null;
    }
}
