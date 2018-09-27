package com.example.sthephan.laboratorio_2_compresion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.labelTitulo)
    TextView labelTitulo;
    @BindView(R.id.labelManual)
    TextView labelManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        labelManual.setMovementMethod(new ScrollingMovementMethod());
        labelManual.setText("Este laboratorio tiene 5 opciones de menu. La primera es la compresion de un archivo por el algoritmo de huffman." +
                "\nEsta primera opcion nos llevara a la ventana de compresion de archivos. En ella tenemos tres opciones: Buscar un archivo para " +
                "comprimir, cancelar la busqueda hecha para realizar una nueva busqueda y comprimir para iniciar la compresion del archivo." +
                "\nDespues de haber seleccionado el archivo a comprimir y haberle dado click al boton de comprimir se nos mostrara una ventana con la compresion " +
                "binaria del archivo que seleccionamos, la codificacion en codigo Ascii del codigo binario y los botones para guardar este archivo de compresion o para borrarlo.\n" +
                "Si le damos click en guardar se guardara este archivo comprimido .huff en el dispositivo y regresaremos a la ventana de inicio de la aplicacion; y si presionamos" +
                " el boton de borrar no se guardara este archivo comprimido y se regresara a la ventana de inicio.\n" +
                "La segunda opcion en el menu de inicio es la de descompresion de archivos con extension .huff. Dentro de esta opcion encontraremos las mismas opciones que " +
                "en la opcion de compresion, busqueda, cancular y en este caso descompresion.\nAl ya haber seleccionado un archivo y haberle dado click en descomprimir" +
                " se nos presentara una ventana con el texto descomprimido del archivo que seleccionamos anteriormente. En esta ventana tambien tenemos las mismas opciones de borrar y guardar" +
                " el archivo que se acaba de descomprimir. De ahí se encuentran las opciones de compresion y descompresion con el algoritmo LZW, " +
                "la funcionalidad de la aplicacion es la misma que cuando se selecciona el algoritmo huffma. \n La ultima opción es la del historial de compresiones que mostrará " +
                "todas las compresiones que se han realizado con la aplicacion."
                + "Con esto concluye el manual de usuario.\n Nota: En los procesos de compresion y descompresion es normal" +
                " que la aplicacion no reaccione por unos minuntos, esto se debe a la cantidad de datos que el programa esta comprimiendo o descomprimiendo.\nAutores:\n" +
                "Williams Monterroso Contreras #1021417\nJose Fuentes Araujo #1168315");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.opCompresionHuff:
                finish();
                startActivity(new Intent(MainActivity.this, CompressHuff1.class));
                return true;
            case R.id.opDescompresionHuff:
                finish();
                startActivity(new Intent(MainActivity.this, DecompressHuff1.class));
                return true;
            case R.id.opCompresionLZW:
                finish();
                startActivity(new Intent(MainActivity.this, CompressLZW1.class));
                return true;
            case R.id.opDescompresionLZW:
                finish();
                startActivity(new Intent(MainActivity.this, DecompressLZW1.class));
                return true;
            case R.id.opCompressHistory:
                finish();
                startActivity(new Intent(MainActivity.this, CompressHistory.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}
