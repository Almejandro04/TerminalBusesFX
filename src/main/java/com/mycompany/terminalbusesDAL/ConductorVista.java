package com.mycompany.terminalbusesDAL;

public class ConductorVista {

    private final int codConductor;
    private final int codTerminal;
    private final String cedulaConductor;
    private final String nombreConductor;
    private final String apellidoConductor;

    public ConductorVista(int codConductor, int codTerminal, String cedulaConductor,
                           String nombreConductor, String apellidoConductor) {
        this.codConductor = codConductor;
        this.codTerminal = codTerminal;
        this.cedulaConductor = cedulaConductor;
        this.nombreConductor = nombreConductor;
        this.apellidoConductor = apellidoConductor;
    }

    public int getCodConductor() {
        return codConductor;
    }

    public int getCodTerminal() {
        return codTerminal;
    }

    public String getCedulaConductor() {
        return cedulaConductor;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public String getApellidoConductor() {
        return apellidoConductor;
    }

    @Override
    public String toString() {
        return nombreConductor + " " + apellidoConductor + " (" + cedulaConductor + ")";
    }
}
