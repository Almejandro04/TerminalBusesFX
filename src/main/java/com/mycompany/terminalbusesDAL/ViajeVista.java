/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

import java.time.LocalDate;
import java.time.LocalTime;

public class ViajeVista {
    private int codViaje;
    private int codConductor;
    private int codVehiculo;
    private int codTerminal;
    private LocalDate fechaViaje;
    private LocalTime horaViaje;
    private String ciudadDestino;
    private double precioViaje;

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

    // Getters
    public int getCodViaje() { return codViaje; }
    public int getCodConductor() { return codConductor; }
    public int getCodVehiculo() { return codVehiculo; }
    public int getCodTerminal() { return codTerminal; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public LocalTime getHoraViaje() { return horaViaje; }
    public String getCiudadDestino() { return ciudadDestino; }
    public double getPrecioViaje() { return precioViaje; }
    
        // 3) Setters
    public void setCodViaje(int codViaje)             { this.codViaje = codViaje; }
    public void setCodConductor(int codConductor)     { this.codConductor = codConductor; }
    public void setCodVehiculo(int codVehiculo)       { this.codVehiculo = codVehiculo; }
    public void setCodTerminal(int codTerminal)       { this.codTerminal = codTerminal; }
    public void setFechaViaje(LocalDate fechaViaje)   { this.fechaViaje = fechaViaje; }
    public void setHoraViaje(LocalTime horaViaje)     { this.horaViaje = horaViaje; }
    public void setCiudadDestino(String ciudadDestino){ this.ciudadDestino = ciudadDestino; }
    public void setPrecioViaje(double precioViaje)    { this.precioViaje = precioViaje; }


    @Override
    public String toString() {
    return String.valueOf(this.getCodViaje());
    // O puedes mostrar más info:
    // return "Viaje #" + this.getCodViaje() + " (" + this.getCiudadDestino() + ")";
    }
}
