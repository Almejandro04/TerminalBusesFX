package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.PasajeroProcedimientoDAO;
import com.mycompany.terminalbusesDAL.PasajeroVista;

public class PasajeroProcedimientoService {

    private final PasajeroProcedimientoDAO dao = new PasajeroProcedimientoDAO();

    /** Ingresar */
    public boolean crearPasajero(PasajeroVista p) {
        return dao.insertarPasajero(p);
    }

    /** Actualizar (deja en null los campos que no quieras modificar) */
    public boolean actualizarPasajero(PasajeroVista p) {
        return dao.actualizarPasajero(p);
    }

    /** Borrar por cédula */
    public boolean borrarPasajero(String cedulaPasajero) {
        return dao.eliminarPasajero(cedulaPasajero);
    }
}
