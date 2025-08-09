/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

/**
 *
 * @author Jorge
 */
public class PasajeroVista {
    
    private int codPasajero;
    private int codViaje;
    private String nombrePasajero;
    private String apellidoPasajero;
    private String telefonoPasajero;
    private String cedulaPasajero;
    private String correoPasajero;

    public PasajeroVista(int codPasajero, int codViaje, String nombrePasajero, String apellidoPasajero, String telefonoPasajero, String cedulaPasajero, String correoPasajero) {
        this.codPasajero = codPasajero;
        this.codViaje = codViaje;
        this.nombrePasajero = nombrePasajero;
        this.apellidoPasajero = apellidoPasajero;
        this.telefonoPasajero = telefonoPasajero;
        this.cedulaPasajero = cedulaPasajero;
        this.correoPasajero = correoPasajero;
    }
    

    // Getters
    public int getCodPasajero() { return codPasajero; }
    public int getCodViaje() { return codViaje; }
    public String getNombrePasajero() { return nombrePasajero; }
    public String getApellidoPasajero() { return apellidoPasajero; }
    public String getTelefonoPasajero() { return telefonoPasajero; }
    public String getCedulaPasajero() { return cedulaPasajero; }
    public String getCorreoPasajero() { return correoPasajero; }
    
    public void setCodPasajero(int codPasajero) {
    this.codPasajero = codPasajero;
}

public void setCodViaje(int codViaje) {
    this.codViaje = codViaje;
}

public void setNombrePasajero(String nombrePasajero) {
    this.nombrePasajero = nombrePasajero;
}

public void setApellidoPasajero(String apellidoPasajero) {
    this.apellidoPasajero = apellidoPasajero;
}

public void setTelefonoPasajero(String telefonoPasajero) {
    this.telefonoPasajero = telefonoPasajero;
}

public void setCedulaPasajero(String cedulaPasajero) {
    this.cedulaPasajero = cedulaPasajero;
}

public void setCorreoPasajero(String correoPasajero) {
    this.correoPasajero = correoPasajero;
}
    
    
}
