package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.RutaProcedimientoDAO;
import com.mycompany.terminalbusesDAL.RutaVista;

public class RutaProcedimientoService {

    private final RutaProcedimientoDAO dao = new RutaProcedimientoDAO();

    /** Ingresar ruta */
    public boolean crearRuta(RutaVista r) {
        return dao.insertarRuta(r);
    }

    /** Actualizar ruta (deja null en destino/precio para no modificarlos) */
    public boolean actualizarRuta(RutaVista r) {
        return dao.actualizarRuta(r);
    }

    /** Borrar ruta por (terminal, ruta) */
    public boolean borrarRuta(int codTerminal, int codRuta) {
        return dao.eliminarRuta(codTerminal, codRuta);
    }
}
