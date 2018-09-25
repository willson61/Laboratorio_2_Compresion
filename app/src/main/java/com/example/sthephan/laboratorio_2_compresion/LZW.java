package com.example.sthephan.laboratorio_2_compresion;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class LZW {
    //public static Dictionary<String, NodoDiccionario> DiccionarioOriginal;
    public static Map<String, NodoDiccionario> DiccionarioOriginal = new HashMap<>();
    public static Map<String, NodoDiccionario> DiccionarioInvverso = new HashMap<>();
    public static String DiccionarioO = "";
    public static int longitudMax;

    public static String Comprimir(String texto){
        armarDiccionario(texto);
        Map<String, NodoDiccionario> Diccionario = LZW.DiccionarioOriginal;
        ArrayList<String> elementos = new ArrayList<>();
        String output = "";
        String input = "";
        String s = "";
        char[] contenido = texto.toCharArray();
        int tamano = contenido.length;
        int i = 0;
        int index = LZW.DiccionarioOriginal.size();
        while(i < tamano) {
            s += contenido[i];
            if (Diccionario.containsKey(s)) {
                i++;
            } else {
                elementos.add(Integer.toString(Diccionario.get(s).getIndex()));
                s += contenido[i + 1];
                NodoDiccionario nodo = new NodoDiccionario();
                nodo.setValue(s);
                nodo.setIndex(index++);
                Diccionario.put(s, nodo);
                s = "";
                i++;
            }
        }
        int lon = Integer.toBinaryString(Diccionario.size()).length();
        LZW.longitudMax = lon;
        for (int x = 0; x < elementos.size(); x++) {
            int cerosExtra = 0;
            String val = null;
            val = Integer.toBinaryString(Integer.parseInt(elementos.get(x)));
            cerosExtra = lon - val.length() % lon;
            if(cerosExtra != lon){
                for(int y = 0; y < cerosExtra; y++){
                    val = "0" + val;
                }
            }
            else{
                cerosExtra = 0;
            }
            elementos.set(x, val);
        }
        for(int z = 0; z<elementos.size(); z++){
            output += elementos.get(z);
        }
        return output;
    }

    private static String getDiccionarioO(){
        return DiccionarioO;
    }

    public static String Descomprimir(String texto, String caracteres, String ceros){
        //String[] archivo = texto.split("/¬/");
        armarDiccioinarioDesc(caracteres);
        int index = LZW.DiccionarioInvverso.size();
        String output = "";
        String actual = "";
        String previo = "";
        char[] contendio = texto.toCharArray();
        int i = 0;
        boolean finarchivo = false;
        while (!finarchivo){
            if(i == contendio.length - 1){
                finarchivo = true;
            }
            if(i == 0){
                actual = LZW.DiccionarioInvverso.get(contendio[i]).getValue();
                output += actual;
                i++;
            }else {
                actual = LZW.DiccionarioInvverso.get(contendio[i]).getValue();
                previo = LZW.DiccionarioInvverso.get(contendio[i]).getValue();
                output += actual;
                index++;
                NodoDiccionario nodo = new NodoDiccionario();
                nodo.setValue(previo + actual.substring(0, 1));
                nodo.setIndex(index);
                LZW.DiccionarioInvverso.put(Integer.toString(index), nodo);
                i++;
            }

        }
        return output;
        /*for (int i = 0; i < contendio.length; i++){
            if (i == 0){
                output += LZW.DiccionarioInvverso.get(String.valueOf(contendio[i]));
            }else{
                output += LZW.DiccionarioInvverso.get(String.valueOf(contendio[i]));
                NodoDiccionario nodo = new NodoDiccionario();
                nodo.setIndex(index++);
                nodo.setValue(LZW.DiccionarioInvverso.get(String.valueOf(contendio[i-1])).getValue());
            }
        }*/

    }

    private static void armarDiccioinarioDesc(String texto){
        char[] cadena = texto.toCharArray();
        int index = 1;
        for (int i = 0; i < cadena.length; i++){
            NodoDiccionario nodo = new NodoDiccionario();
            nodo.setIndex(index);
            nodo.setValue(String.valueOf(cadena[i]));
            LZW.DiccionarioInvverso.put(Integer.toString(index), nodo);
            index++;
        }
    }

    public static void armarDiccionario(String texto) {
        char[] cadena = texto.toCharArray();
        int index = 1;
        String caracteres = "";
        for (int i = 0; i < cadena.length; i++){
            String analisis = "";
            analisis += cadena[i];
            if (caracteres.contains(analisis)){

            }else{
                caracteres += analisis;
                NodoDiccionario nodo = new NodoDiccionario();
                nodo.setIndex(index);
                nodo.setValue(analisis);
                index++;
                LZW.DiccionarioOriginal.put(analisis, nodo);
            }
        }
        LZW.DiccionarioO = caracteres;

    }

    public static void setDiccionarioOriginal(Map<String, NodoDiccionario> diccionarioOriginal) {
        DiccionarioOriginal = diccionarioOriginal;
    }

    public static Map<String, NodoDiccionario> getDiccionarioOriginal() {
        return DiccionarioOriginal;
    }

    public static void ResetearCampos(){
        LZW.DiccionarioO = "";
        LZW.DiccionarioOriginal.clear();
        LZW.DiccionarioInvverso.clear();
    }
}

class NodoDiccionario {
    public static int index;
    public static String Value;

    public static int getIndex() {
        return index;
    }

    public static String getValue() {
        return Value;
    }

    public static void setIndex(int index) {
        NodoDiccionario.index = index;
    }

    public static void setValue(String value) {
        Value = value;
    }
}
