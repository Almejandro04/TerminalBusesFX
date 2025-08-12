package com.mycompany.terminalbusesDAL;

public class ConductorVista {

    private final int codTerminal;
    private final String nombreConductor;
    private final String apellidoConductor;
    private final String licenciaConductor;

    public ConductorVista(int codTerminal, String nombreConductor, String apellidoConductor, String licenciaConductor) {
        this.codTerminal = codTerminal;
        this.nombreConductor = nombreConductor;
        this.apellidoConductor = apellidoConductor;
        this.licenciaConductor = licenciaConductor;
    }

    public int getCodTerminal() {
        return codTerminal;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public String getApellidoConductor() {
        return apellidoConductor;
    }

    public String getLicenciaConductor() {
        return licenciaConductor;
    }

    @Override
    public String toString() {
        return nombreConductor + " " + apellidoConductor + " (" + licenciaConductor + ")";
    }

}
