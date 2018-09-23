package com.example.sthephan.laboratorio_2_compresion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompressHistory extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static ArrayList<Compress> ListaCompress = new ArrayList<>();
    public AdapterCompress adapter = new AdapterCompress(this, ListaCompress);

    @BindView(R.id.listHistorial)
    ListView listHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_history);
        ButterKnife.bind(this);
        leerHistorial();
        if(CompressHistory.ListaCompress.size() == 0){
            Toast message = Toast.makeText(getApplicationContext(), "El historial esta vacio", Toast.LENGTH_LONG);
            message.show();
        }
        else{
            listHistorial.setAdapter(adapter);
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
                        CompressHistory.ListaCompress = new ArrayList<Compress>();
                        finish();
                        startActivity(new Intent(CompressHistory.this, MainActivity.class));
                    }
                }).create().show();
    }

    public void leerHistorial() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File historial = new File(root, "HistorialCompresion.txt");
        StringBuilder stringbuilder = new StringBuilder();
        if(historial.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(historial));
                int line = 0;
                while ((line = reader.read()) != -1) {
                    char val = (char) line;
                    stringbuilder.append(val);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] compresiones = stringbuilder.toString().split("%~%");
            for (int i = 0; i < compresiones.length; i++) {
                String[] val = compresiones[i].split("#~#");
                Compress c = new Compress(val[0], val[1], val[2], Float.parseFloat(val[3]), Float.parseFloat(val[4]), Float.parseFloat(val[5]), val[6]);
                CompressHistory.ListaCompress.add(c);
            }
        }
    }
}
