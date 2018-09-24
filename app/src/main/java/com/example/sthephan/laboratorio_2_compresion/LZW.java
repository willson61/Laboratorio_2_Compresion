package com.example.sthephan.laboratorio_2_compresion;

import java.util.Dictionary;

public class LZW {
    public static Dictionary<Integer, String> DiccionarioOriginal;

    private static void Comprimir(){
        String s = "";
        int index = 0;
    }

    private static void Descomprimir(Dictionary<Integer, String> diccionario, String texto){

    }

    public static void setDiccionarioOriginal(Dictionary<Integer, String> diccionarioOriginal) {
        DiccionarioOriginal = diccionarioOriginal;
    }

    public static Dictionary<Integer, String> getDiccionarioOriginal() {
        return DiccionarioOriginal;
    }
}
