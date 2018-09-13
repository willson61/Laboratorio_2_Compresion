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
}
