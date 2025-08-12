/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

public class PasajeroVista {
    private final String cedulaPasajero;
    private final String nombrePasajero;
    private final String apellidoPasajero;
    private final String telefonoPasajero;
    private final String correoPasajero;

    public PasajeroVista(String cedulaPasajero,
                         String nombrePasajero,
                         String apellidoPasajero,
                         String telefonoPasajero,
                         String correoPasajero) {
        this.cedulaPasajero = cedulaPasajero;
        this.nombrePasajero = nombrePasajero;
        this.apellidoPasajero = apellidoPasajero;
        this.telefonoPasajero = telefonoPasajero;
        this.correoPasajero = correoPasajero;
    }

    public String getCedulaPasajero()   { return cedulaPasajero; }
    public String getNombrePasajero()   { return nombrePasajero; }
    public String getApellidoPasajero() { return apellidoPasajero; }
    public String getTelefonoPasajero() { return telefonoPasajero; }
    public String getCorreoPasajero()   { return correoPasajero; }

    @Override public String toString() {
        return nombrePasajero + " " + apellidoPasajero + " (" + cedulaPasajero + ")";
    }


// public void setCodViaje(int codViaje) {
//     this.codViaje = codViaje;
// }

// public void setNombrePasajero(String nombrePasajero) {
//     this.nombrePasajero = nombrePasajero;
// }

// public void setApellidoPasajero(String apellidoPasajero) {
//     this.apellidoPasajero = apellidoPasajero;
// }

// public void setTelefonoPasajero(String telefonoPasajero) {
//     this.telefonoPasajero = telefonoPasajero;
// }

// public void setCedulaPasajero(String cedulaPasajero) {
//     this.cedulaPasajero = cedulaPasajero;
// }

// public void setCorreoPasajero(String correoPasajero) {
//     this.correoPasajero = correoPasajero;
// }
    
    
}
