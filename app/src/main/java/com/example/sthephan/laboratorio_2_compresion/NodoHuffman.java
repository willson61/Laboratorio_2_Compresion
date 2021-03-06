package com.example.sthephan.laboratorio_2_compresion;

public class NodoHuffman {

    public double probabilidad;
    public char caracter;
    public String codigo;
    public NodoHuffman hijoIzquierdo;
    public NodoHuffman hijoDerecho;
    public NodoHuffman nodoPadre;

    public double getProbabilidad(){
        return probabilidad;
    }
    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public char getCaracter(){
        return caracter;
    }

    public void setCaracter(char caracter){
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
