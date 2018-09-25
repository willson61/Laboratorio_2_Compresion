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

public class DecompressHuffResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static String txtDescompresion;
    public static String nombreArchivo;
    public static Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decompress_huff_result);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        labelContenido.setText(DecompressHuffResult.txtDescompresion);
        labelNombre.setText(DecompressHuffResult.nombreArchivo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345 && resultCode == RESULT_OK && data.getData().getPath().contains(".txt")) {
            try{
                Uri selectedFile = data.getData();
                DecompressHuffResult.file = selectedFile;
                ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(selectedFile, "w");
                FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
                Writer wr = new OutputStreamWriter(fos, UTF8);
                String textContent = DecompressHuffResult.txtDescompresion;
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
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .txt para guardar el archivo comprimido", Toast.LENGTH_LONG);
            message.show();
        }
        if (checkURIResource(DecompressHuffResult.this.getApplicationContext(), DecompressHuffResult.file)){
            Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
            message.show();
            finish();
            startActivity(new Intent(DecompressHuffResult.this, MainActivity.class));
        }
        else{
            Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
            message.show();
            finish();
            startActivity(new Intent(DecompressHuffResult.this, MainActivity.class));
        }
    }

    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
    }

    @OnClick({R.id.btnGuardar, R.id.btnBorrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TITLE, DecompressHuffResult.nombreArchivo.concat("Descomprimido.txt"));
                startActivityForResult(intent, 12345);
                break;
            case R.id.btnBorrar:
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a borrado exitosamente", Toast.LENGTH_LONG);
                message.show();
                finish();
                startActivity(new Intent(DecompressHuffResult.this, MainActivity.class));
                break;
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
                        startActivity(new Intent(DecompressHuffResult.this, MainActivity.class));
                    }
                }).create().show();
    }
}
