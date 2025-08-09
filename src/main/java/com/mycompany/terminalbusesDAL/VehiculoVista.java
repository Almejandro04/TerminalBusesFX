/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

/**
 *
 * @author Jorge
 */
public class VehiculoVista {
    private int codVehiculo;
    private int codTerminal;
    private String placaVehiculo;
    private int capacidadVehiculo;
    private String compañiaVehiculo;
    
   public VehiculoVista(int codVehiculo, int codTerminal, String placaVehiculo, int capacidadVehiculo, String compañiaVehiculo) {
       this.codVehiculo = codVehiculo; 
       this.codTerminal = codTerminal;
       this.placaVehiculo = placaVehiculo;
       this.capacidadVehiculo = capacidadVehiculo; 
       this.compañiaVehiculo = compañiaVehiculo;
    }
    
    public int getCodVehiculo() { return codVehiculo; }
    public int getCodTerminal() { return codTerminal; }
    public String getPlacaVehiculo() { return placaVehiculo; }
    public int getCapacidadVehiculo() { return capacidadVehiculo; }
    public String getCompañiaVehiculo() { return compañiaVehiculo; }

    @Override
public String toString() {
    return placaVehiculo;  // o "Placa: " + placaVehiculo
}

}
