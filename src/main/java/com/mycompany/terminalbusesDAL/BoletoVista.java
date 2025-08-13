package com.mycompany.terminalbusesDAL;

public class BoletoVista {

    private final int codTerminal;
    private final int codViaje;
    private final String cedulaPasajero;
    private final int numAsiento;

    public BoletoVista(int codTerminal, int codViaje, String cedulaPasajero, int numAsiento) {
        this.codTerminal = codTerminal;
        this.codViaje = codViaje;
        this.cedulaPasajero = cedulaPasajero;
        this.numAsiento = numAsiento;
    }

    public int getCodTerminal() {
        return codTerminal;
    }

    public int getCodViaje() {
        return codViaje;
    }

    public String getCedulaPasajero() {
        return cedulaPasajero;
    }

    public int getNumAsiento() {
        return numAsiento;
    }

    @Override
    public String toString() {
        return "Viaje #" + codViaje +
               " - Pasajero: " + cedulaPasajero +
               " - Asiento: " + numAsiento;
    }
}
