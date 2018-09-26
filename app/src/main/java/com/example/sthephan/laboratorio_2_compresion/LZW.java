package com.example.sthephan.laboratorio_2_compresion;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

public class LZW {
    //public static Dictionary<String, NodoDiccionario> DiccionarioOriginal;
    public static LinkedHashMap<String, NodoDiccionario> DiccionarioOriginal = new LinkedHashMap<>();
    public static LinkedHashMap<String, NodoDiccionario> DiccionarioInvverso = new LinkedHashMap<>();
    public static String DiccionarioO = "";
    public static int longitudMax;

    public static String Comprimir(String texto){
        LinkedHashMap<String, NodoDiccionario> DiccionarioPrueba = new LinkedHashMap<>();
        char[] cadena = texto.toCharArray();
        //LinkedHashMap<String, NodoDiccionario> dicprueba = new LinkedHashMap<>();
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
                //int prueba = nodo.getIndex();
                LZW.DiccionarioOriginal.put(analisis, nodo);
                DiccionarioPrueba.put(analisis, nodo);
                int asdf = DiccionarioPrueba.get(analisis).getIndice();
                int asdf2 = nodo.getIndice();
            }
        }
        LZW.DiccionarioO = caracteres;
        //armarDiccionario(texto);
        //LinkedHashMap<String, NodoDiccionario> Diccionario = LZW.DiccionarioOriginal;
        ArrayList<String> elementos = new ArrayList<>();
        String output = "";
        String input = "";
        String s = "";
        char[] contenido = texto.toCharArray();
        int tamano = contenido.length;
        int i = 0;
        //int index = LZW.DiccionarioOriginal.size();
        int index2 = DiccionarioPrueba.size();
        boolean Final = false;
        while(i < tamano) {
            s += contenido[i];
            NodoDiccionario nodo = new NodoDiccionario();
            //if (Diccionario.containsKey((s += contenido[i+1]) == null)){
            //    s = s.substring(0, s.length()-1);
            //    elementos.add(Integer.toString(Diccionario.get(s).getIndice()));
            //}else{
            //    s = s.substring(0, s.length()-1);
            //}
            if (i == tamano - 1){
                //elementos.add(Integer.toString(Diccionario.get(s).getIndice()));
                elementos.add(Integer.toString(DiccionarioPrueba.get(s).indice));
                i++;
            }else if (DiccionarioPrueba.containsKey(s += contenido[i+1])) {
                i++;
                s = s.substring(0, s.length()-1);
            } else {
                s = s.substring(0, s.length()-1);
                nodo = DiccionarioPrueba.get(s);
                int prueba = nodo.indice;
                elementos.add(Integer.toString(DiccionarioPrueba.get(s).indice));
                s += contenido[i + 1];
                NodoDiccionario nodoN = new NodoDiccionario();
                nodoN.setValue(s);
                index2++;
                nodoN.setIndex(index2);
                DiccionarioPrueba.put(s, nodoN);
                s = "";
                i++;
            }

        }
        int lon = Integer.toBinaryString(DiccionarioPrueba.size()).length();
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

    public static String Descomprimir(ArrayList<String> texto, String caracteres, String ceros){
        //String[] archivo = texto.split("/¬/");
        armarDiccioinarioDesc(caracteres);
        int index = LZW.DiccionarioInvverso.size();
        String output = "";
        String actual = "";
        String previo = "";
        ArrayList<String> contendio = texto;
        int i = 0;
        boolean finarchivo = false;
        while (!finarchivo){
            if(i == contendio.size() - 1){
                finarchivo = true;
            }
            if(i == 0){
                actual = LZW.DiccionarioInvverso.get(contendio.get(i)).getValue();
                output += actual;
                i++;
            }else {
                actual = LZW.DiccionarioInvverso.get(contendio.get(i)).getValue();
                previo = LZW.DiccionarioInvverso.get(contendio.get(i - 1)).getValue();
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

    public static LinkedHashMap<String, NodoDiccionario>  armarDiccionario(String texto) {
        char[] cadena = texto.toCharArray();
        LinkedHashMap<String, NodoDiccionario> dicprueba = new LinkedHashMap<>();
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
                //int prueba = nodo.getIndex();
                LZW.DiccionarioOriginal.put(analisis, nodo);
                dicprueba.put(analisis, nodo);
            }
        }
        LZW.DiccionarioO = caracteres;
        return dicprueba;

    }

    public static void setDiccionarioOriginal(LinkedHashMap<String, NodoDiccionario> diccionarioOriginal) {
        DiccionarioOriginal = diccionarioOriginal;
    }

    public static LinkedHashMap<String, NodoDiccionario> getDiccionarioOriginal() {
        return DiccionarioOriginal;
    }

    public static void ResetearCampos(){
        LZW.DiccionarioO = "";
        LZW.DiccionarioOriginal.clear();
        LZW.DiccionarioInvverso.clear();
    }
}

/*class NodoDiccionario {
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
}*/
