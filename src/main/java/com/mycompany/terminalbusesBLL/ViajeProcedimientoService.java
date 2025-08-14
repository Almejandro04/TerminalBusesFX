package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.ViajeProcedimientoDAO;
import com.mycompany.terminalbusesDAL.ViajeVista;

public class ViajeProcedimientoService {

    private final ViajeProcedimientoDAO dao = new ViajeProcedimientoDAO();

    /** Ingresar viaje */
    public boolean crearViaje(ViajeVista v) {
        return dao.insertarViaje(v);
    }

    /** Actualizar viaje (deja null/0 en campos que no quieras modificar) */
    public boolean actualizarViaje(ViajeVista v) {
        return dao.actualizarViaje(v);
    }

    /** Borrar viaje por (terminal, cod_viaje) */
    public boolean borrarViaje(int codTerminal, int codViaje) {
        return dao.eliminarViaje(codTerminal, codViaje);
    }
}
