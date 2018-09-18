package com.example.sthephan.laboratorio_2_compresion;

import java.util.ArrayList;

public class ArbolHuffman {
    public static NodoHuffman nodoRaiz;
    //public static NodoHuffman nodoAux;
    public static String Num = "";
    public static ArrayList<NodoHuffman> retornar = new ArrayList<>();

    public static void armarArbol(ArrayList<NodoHuffman> lista) {
        ordenamientoAsc(lista);
        boolean completo = true;
        NodoHuffman auxiliar = new NodoHuffman();
        while (completo){
            auxiliar.hijoIzquierdo = lista.get(0);
            auxiliar.hijoDerecho = lista.get(1);
            auxiliar.probabilidad = auxiliar.hijoDerecho.probabilidad + auxiliar.hijoIzquierdo.probabilidad;
            auxiliar.hijoIzquierdo.nodoPadre = auxiliar;
            auxiliar.hijoDerecho.nodoPadre = auxiliar;
            lista.add(auxiliar);
            ordenamientoAsc(lista);
            lista.remove(0);
            lista.remove(0);
            if (lista.size() == 1){
                nodoRaiz = auxiliar;
                nodoRaiz.nodoPadre = null;
                //nodoAux = nodoRaiz;
                completo = false;
            }else {
                ordenamientoAsc(lista);
            }
            auxiliar = new NodoHuffman();
        }

    }

    public static ArrayList<NodoHuffman> CreacionArbolFinal(ArrayList<NodoHuffman> listaProbabilidades){
        armarArbol(listaProbabilidades);
        //inOrdenC(nodoRaiz, Num);
        setBinario(nodoRaiz, "");
        ordenamientoAsc(retornar);
        return retornar;
    }

    public static void ordenamientoAsc(ArrayList<NodoHuffman> lista) {
        int n = lista.size();
        int i = 1;
        boolean ordenado = false;
        while (i < n && !ordenado) {
            i++;
            ordenado = true;
            for (int j = 0; j < n - 1; j++) {
                if (lista.get(j).probabilidad > lista.get(j + 1).probabilidad) {
                    ordenado = false;
                    NodoHuffman aux = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, aux);
                }
            }
        }
    }

    public static NodoHuffman getArbolI(NodoHuffman nodo){
        if (nodo.getHijoIzquierdo() == null){
            return null;
        }
        else{
            return nodo.getHijoIzquierdo();
        }
    }

    public static NodoHuffman getArbolIC(NodoHuffman nodo, String s){
        if (nodo.getHijoIzquierdo() == null){
            s += "0";
            nodo.setCodigo(s);
            //s.replace(s, s.substring(0, s.length() - 2));
            retornar.add(nodo);
            return null;
        }
        else{
            s += "0";
            return nodo.getHijoIzquierdo();
        }
    }

    public static NodoHuffman getArbolD(NodoHuffman nodo){
        if (nodo.getHijoDerecho() == null){
            return null;
        }
        else{
            return nodo.getHijoDerecho();
        }
    }

    public static NodoHuffman getArbolDC(NodoHuffman nodo, String s){
        if (nodo.getHijoDerecho() == null){
            s += "1";
            nodo.setCodigo(s);
            //s.replace(s, s.substring(0, s.length() - 2));
            retornar.add(nodo);
            return null;
        }
        else{
            s += "1";
            return nodo.getHijoDerecho();
        }
    }

    public static void inOrdenC (NodoHuffman nodo, String s){
        //NodoHuffman aux = nodo;
        if(!esHoja(nodo)){
            inOrden(getArbolIC(nodo, s));
            s.replace(s, s.substring(0, s.length() - 2));
            inOrden(getArbolDC(nodo, s));
            s.replace(s, s.substring(0, s.length() - 2));
        }
    }

    public static void inOrden (NodoHuffman nodo){
        //NodoHuffman aux = nodo;
        if(!esHoja(nodo)){
            inOrden(getArbolI(nodo));

            inOrden(getArbolD(nodo));
        }
    }

    public static boolean esHoja(NodoHuffman aux){
        if (aux.hijoDerecho == null && aux.hijoIzquierdo == null){
            return true;
        }
        else{
            return false;
        }
    }

    public static void setBinario(NodoHuffman nodo, String n){
        if(nodo.getCaracter()==('\u0000')){
            setBinario(nodo.getHijoIzquierdo(), n+"0");
            setBinario(nodo.getHijoDerecho(), n + "1");
        }
        else{
            nodo.setCodigo(n);
            retornar.add(nodo);
        }
    }
}