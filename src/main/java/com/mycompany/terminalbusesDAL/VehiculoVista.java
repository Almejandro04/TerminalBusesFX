package com.mycompany.terminalbusesDAL;

import java.util.Objects;

public class VehiculoVista {
    private final int codTerminal;
    private final String placaVehiculo;
    private final int capacidadVehiculo;

    public VehiculoVista(int codTerminal, String placaVehiculo, int capacidadVehiculo) {
        this.codTerminal = codTerminal;
        this.placaVehiculo = placaVehiculo;
        this.capacidadVehiculo = capacidadVehiculo;
    }

    public int getCodTerminal() { return codTerminal; }
    public String getPlacaVehiculo() { return placaVehiculo; }
    public int getCapacidadVehiculo() { return capacidadVehiculo; }

    @Override
    public String toString() {
        return placaVehiculo; // útil para ComboBox/Logs
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehiculoVista)) return false;
        VehiculoVista that = (VehiculoVista) o;
        // placa suele ser única por terminal
        return codTerminal == that.codTerminal &&
               Objects.equals(placaVehiculo, that.placaVehiculo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTerminal, placaVehiculo);
    }
}
