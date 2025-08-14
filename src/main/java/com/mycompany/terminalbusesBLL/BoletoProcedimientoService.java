package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.BoletoProcedimientoDAO;
import com.mycompany.terminalbusesDAL.BoletoVista;

public class BoletoProcedimientoService {

    private final BoletoProcedimientoDAO dao = new BoletoProcedimientoDAO();

    /** Inserta un nuevo boleto */
    public boolean insertarBoleto(BoletoVista b) {
        return dao.insertarBoleto(b);
    }

    /** Actualiza un boleto existente */
    public boolean actualizarBoleto(BoletoVista b) {
        return dao.actualizarBoleto(b);
    }

    // /** Elimina un boleto */
    // public boolean eliminarBoleto(int codTerminal, int codViaje, String cedulaPasajero) {
    //     return dao.eliminarBoleto(codTerminal, codViaje, cedulaPasajero);
    // }
    /** Borrar boleto por (terminal, viaje, cédula pasajero) */
    public boolean borrarBoleto(int codTerminal, int codViaje, String cedulaPasajero) {
        return dao.eliminarBoleto(codTerminal, codViaje, cedulaPasajero);
    }
    
}
