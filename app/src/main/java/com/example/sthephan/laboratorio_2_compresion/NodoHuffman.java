package com.example.sthephan.laboratorio_2_compresion;

import java.util.Comparator;

public class NodoHuffman {

    public float probabilidad;
    public Character caracter;
    public String codigo;
    public NodoHuffman hijoIzquierdo;
    public NodoHuffman hijoDerecho;
    public NodoHuffman nodoPadre;

    public float getProbabilidad(){
        return probabilidad;
    }
    public void setProbabilidad(float probabilidad) {
        this.probabilidad = probabilidad;
    }

    public Character getCaracter(){
        return caracter;
    }

    public void setCaracter(Character caracter){
        this.caracter = caracter;
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public NodoHuffman getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoHuffman hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoHuffman getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(NodoHuffman hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    public NodoHuffman getNodoPadre() {
        return nodoPadre;
    }

    public void setNodoPadre(NodoHuffman nodoPadre) {
        this.nodoPadre = nodoPadre;
    }

    /*public static boolean esHoja(NodoHuffman aux){
        if (aux.hijoDerecho == null && aux.hijoIzquierdo == null){
            return true;
        }
        else{
            return false;
        }
    }*/
}
class CompareByProbabilidad implements Comparator<NodoHuffman> {

    @Override
    public int compare(NodoHuffman c1, NodoHuffman c2) {

        if (c1.getProbabilidad() == c2.getProbabilidad()) {
            if((c1.getCaracter() != null) && (c2.getCaracter() != null)){
                return c1.getCaracter().compareTo(c2.getCaracter());
            }
            else{
                return 0;
            }
        }
        else if (c1.getProbabilidad() < c2.getProbabilidad()) {
            return -1;
        }
        else{
            return 1;
        }

    }

}