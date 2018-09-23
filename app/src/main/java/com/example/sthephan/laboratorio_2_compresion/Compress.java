package com.example.sthephan.laboratorio_2_compresion;

public class Compress {
    public String nombreOriginal;
    public String nombreComprimido;
    public String rutaArchivoComprimido;
    public float razonCompresion;
    public float factorCompresion;
    public float porcentajeReduccion;
    public String tipoCompresion;

    public Compress(String nombreOriginal, String nombreComprimido, String rutaArchivoComprimido, float razonCompresion, float factorCompresion, float porcentajeReduccion, String tipoCompresion) {
        this.nombreOriginal = nombreOriginal;
        this.nombreComprimido = nombreComprimido;
        this.rutaArchivoComprimido = rutaArchivoComprimido;
        this.razonCompresion = razonCompresion;
        this.factorCompresion = factorCompresion;
        this.porcentajeReduccion = porcentajeReduccion;
        this.tipoCompresion = tipoCompresion;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getNombreComprimido() {
        return nombreComprimido;
    }

    public void setNombreComprimido(String nombreComprimido) {
        this.nombreComprimido = nombreComprimido;
    }

    public String getRutaArchivoComprimido() {
        return rutaArchivoComprimido;
    }

    public void setRutaArchivoComprimido(String rutaArchivoComprimido) {
        this.rutaArchivoComprimido = rutaArchivoComprimido;
    }

    public float getRazonCompresion() {
        return razonCompresion;
    }

    public void setRazonCompresion(float razonCompresion) {
        this.razonCompresion = razonCompresion;
    }

    public float getFactorCompresion() {
        return factorCompresion;
    }

    public void setFactorCompresion(float factorCompresion) {
        this.factorCompresion = factorCompresion;
    }

    public float getPorcentajeReduccion() {
        return porcentajeReduccion;
    }

    public void setPorcentajeReduccion(float porcentajeReduccion) {
        this.porcentajeReduccion = porcentajeReduccion;
    }

    public String getTipoCompresion() {
        return tipoCompresion;
    }

    public void setTipoCompresion(String tipoCompresion) {
        this.tipoCompresion = tipoCompresion;
    }
}
