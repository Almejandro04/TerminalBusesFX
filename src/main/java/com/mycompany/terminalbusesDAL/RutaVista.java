package com.mycompany.terminalbusesDAL;

public class RutaVista {
    private int codTerminal;
    private int codRuta;
    private String ciudadDestino;
    private double precio;

    public RutaVista(int codTerminal, int codRuta, String ciudadDestino, double precio) {
        this.codTerminal = codTerminal;
        this.codRuta = codRuta;
        this.ciudadDestino = ciudadDestino;
        this.precio = precio;
    }

    public int getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(int codTerminal) {
        this.codTerminal = codTerminal;
    }

    public int getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(int codRuta) {
        this.codRuta = codRuta;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return ciudadDestino + " - $" + precio;
    }
}
