package com.mycompany.terminalbusesDAL;

import java.time.LocalDate;
import java.time.LocalTime;

public class ViajeVista {
    private int codViaje;
    private int codConductor;
    private int codVehiculo; // Ya no se usará en las vistas, pero lo dejamos para compatibilidad si lo usas en otro lado
    private int codTerminal;
    private String placa;
    private int codRuta;
    private LocalDate fechaViaje;
    private LocalTime horaViaje;
    private String ciudadDestino; // Solo si en otra parte la usas
    private double precioViaje;   // Solo si en otra parte la usas

    // Constructor nuevo para las vistas Viaje_Ibarra_Vista y Viaje_Quito_Vista
    public ViajeVista(int codTerminal, int codViaje, String placa, int codRuta,
                      int codConductor, LocalDate fechaViaje, LocalTime horaViaje) {
        this.codTerminal = codTerminal;
        this.codViaje = codViaje;
        this.placa = placa;
        this.codRuta = codRuta;
        this.codConductor = codConductor;
        this.fechaViaje = fechaViaje;
        this.horaViaje = horaViaje;
    }

    // Constructor original (por compatibilidad si en otro lado lo usas)
    public ViajeVista(int codViaje, int codConductor, int codVehiculo, int codTerminal,
                      LocalDate fechaViaje, LocalTime horaViaje, String ciudadDestino, double precioViaje) {
        this.codViaje = codViaje;
        this.codConductor = codConductor;
        this.codVehiculo = codVehiculo;
        this.codTerminal = codTerminal;
        this.fechaViaje = fechaViaje;
        this.horaViaje = horaViaje;
        this.ciudadDestino = ciudadDestino;
        this.precioViaje = precioViaje;
    }

    // Getters y Setters...
    public int getCodViaje() { return codViaje; }
    public int getCodConductor() { return codConductor; }
    public int getCodVehiculo() { return codVehiculo; }
    public int getCodTerminal() { return codTerminal; }
    public String getPlaca() { return placa; }
    public int getCodRuta() { return codRuta; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public LocalTime getHoraViaje() { return horaViaje; }
    public String getCiudadDestino() { return ciudadDestino; }
    public double getPrecioViaje() { return precioViaje; }

    public void setCodViaje(int codViaje)             { this.codViaje = codViaje; }
    public void setCodConductor(int codConductor)     { this.codConductor = codConductor; }
    public void setCodVehiculo(int codVehiculo)       { this.codVehiculo = codVehiculo; }
    public void setCodTerminal(int codTerminal)       { this.codTerminal = codTerminal; }
    public void setPlaca(String placa)                { this.placa = placa; }
    public void setCodRuta(int codRuta)               { this.codRuta = codRuta; }
    public void setFechaViaje(LocalDate fechaViaje)   { this.fechaViaje = fechaViaje; }
    public void setHoraViaje(LocalTime horaViaje)     { this.horaViaje = horaViaje; }
    public void setCiudadDestino(String ciudadDestino){ this.ciudadDestino = ciudadDestino; }
    public void setPrecioViaje(double precioViaje)    { this.precioViaje = precioViaje; }

    @Override
    public String toString() {
        return "Viaje #" + codViaje + " - Placa: " + placa + " - Fecha: " + fechaViaje;
    }
}
