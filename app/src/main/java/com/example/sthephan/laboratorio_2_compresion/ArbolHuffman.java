package com.example.sthephan.laboratorio_2_compresion;

import java.util.ArrayList;

public class ArbolHuffman {
    public static NodoHuffman nodoRaiz;

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
            lista.remove(0);
            lista.remove(1);
            lista.add(auxiliar);
            if (lista.size() == 1){
                nodoRaiz = auxiliar;
                completo = false;
            }else {
                ordenamientoAsc(lista);
            }
        }

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

    public static void inOrden (){
        
    }
}