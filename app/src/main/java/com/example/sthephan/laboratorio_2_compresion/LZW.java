package com.example.sthephan.laboratorio_2_compresion;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


public class LZW {
    //public static Dictionary<String, NodoDiccionario> DiccionarioOriginal;
    public static Map<String, NodoDiccionario> DiccionarioOriginal = new HashMap<>();
    public static Map<String, NodoDiccionario> DiccionarioInvverso = new HashMap<>();

    private static String Comprimir(String texto){
        armarDiccionario(texto);
        Map<String, NodoDiccionario> Diccionario = LZW.DiccionarioOriginal;
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
                output = Integer.toString(Diccionario.get(s).getIndex());
                s += contenido[i + 1];
                NodoDiccionario nodo = new NodoDiccionario();
                nodo.setValue(s);
                nodo.setIndex(index++);
                Diccionario.put(s, nodo);
                s = "";
                i++;
            }
        }
        return output;
    }

    private static String Descomprimir(String texto){
        String[] archivo = texto.split("/¬/");
        armarDiccioinarioDesc(archivo[0]);
        int index = LZW.DiccionarioInvverso.size();
        String output = "";
        String actual = "";
        String previo = "";
        String[] contendio = archivo[1].split("");
        int i = 0;
        boolean finarchivo = false;
        while (!finarchivo){
            if(i == contendio.length){
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

    }

    private static void Descomprimir(Map<Integer, NodoDiccionario> diccionario, String texto){

    }

    public static void setDiccionarioOriginal(Map<String, NodoDiccionario> diccionarioOriginal) {
        DiccionarioOriginal = diccionarioOriginal;
    }

    public static Map<String, NodoDiccionario> getDiccionarioOriginal() {
        return DiccionarioOriginal;
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
