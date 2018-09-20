package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecompressHuffResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;

    public static String txtDescompresion;
    public static String nombreArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decompress_huff_result);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        labelContenido.setText(DecompressHuffResult.txtDescompresion);
        labelNombre.setText(DecompressHuffResult.nombreArchivo);
    }

    @OnClick({R.id.btnGuardar, R.id.btnBorrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                File root = new File(Environment.getExternalStorageDirectory(), "Documentos");
                root.mkdirs();
                File archivoHuff = new File(root, labelNombre.getText().toString() + "Descomprimido.txt");
                try{
                    if(archivoHuff.exists()){
                        archivoHuff.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(archivoHuff, true);
                    fos.write(DecompressHuffResult.txtDescompresion.getBytes());
                    fos.flush();
                    fos.close();
                    archivoHuff.createNewFile();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (archivoHuff.exists()){
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
