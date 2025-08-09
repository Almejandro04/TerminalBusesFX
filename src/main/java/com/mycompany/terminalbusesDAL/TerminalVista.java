/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

public class TerminalVista {
    private int codTerminal;
    private String ciudadTerminal;
    private String nombreTerminal;
    private String direccionTerminal;

    public TerminalVista(int codTerminal, String ciudadTerminal, String nombreTerminal, String direccionTerminal) {
        this.codTerminal = codTerminal;
        this.ciudadTerminal = ciudadTerminal;
        this.nombreTerminal = nombreTerminal;
        this.direccionTerminal = direccionTerminal;
    }

    // Getters
    public int getCodTerminal() { return codTerminal; }
    public String getCiudadTerminal() { return ciudadTerminal; }
    public String getNombreTerminal() { return nombreTerminal; }
    public String getDireccionTerminal() { return direccionTerminal; }
}
