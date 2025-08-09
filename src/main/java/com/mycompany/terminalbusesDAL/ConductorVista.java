/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

/**
 *
 * @author Jorge
 */
public class ConductorVista {

    private int codConductor;
 

    private int codTerminal;
    private String nombreConductor;

    private String apellidoConductor;

    private String licenciaConductor;
    

    public ConductorVista(int codConductor, int codTerminal, String nombreConductor, String apellidoConductor, String licenciaConductor) {
        this.codConductor = codConductor;
        this.codTerminal = codTerminal;
        this.nombreConductor = nombreConductor;
        this.apellidoConductor = apellidoConductor;
        this.licenciaConductor = licenciaConductor;
    }

    public int getCodConductor() { return codConductor; }
    public int getCodTerminal() { return codTerminal; }
    public String getNombreConductor() { return nombreConductor; }
    public String getApellidoConductor() { return apellidoConductor; }
    public String getLicenciaConductor() { return licenciaConductor; }
    
    @Override
public String toString() {
    return nombreConductor + " " + apellidoConductor; 
    // o cualquier otro campo que quieras mostrar
}

        public void setCodConductor(int codConductor) {
        this.codConductor = codConductor;
    }

       public void setCodTerminal(int codTerminal) {
        this.codTerminal = codTerminal;
    }



        public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }


        public void setApellidoConductor(String apellidoConductor) {
        this.apellidoConductor = apellidoConductor;
    }

       public void setLicenciaConductor(String licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
    }

}
