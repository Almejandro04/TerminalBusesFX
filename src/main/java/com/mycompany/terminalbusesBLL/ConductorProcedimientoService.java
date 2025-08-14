// package com.mycompany.terminalbusesBLL;

// import com.mycompany.terminalbusesDAL.ConductorProcedimientoDAO;
// import com.mycompany.terminalbusesDAL.ConductorVista;

// public class ConductorProcedimientoService {

//     private final ConductorProcedimientoDAO dao = new ConductorProcedimientoDAO();

//     /** Ingresar */
//     public boolean crearConductor(ConductorVista c) {
//         return dao.insertarConductor(c);
//     }

//     /** Actualizar (pasa null para no cambiar terminal o cédula) */
//     public boolean actualizarConductor(ConductorVista c, Integer codTerminalNuevo, String cedulaNueva) {
//         return dao.actualizarConductor(c, codTerminalNuevo, cedulaNueva);
//     }

//     /** Borrar */
//     public boolean borrarConductor(int codConductor) {
//         return dao.eliminarConductor(codConductor);
//     }
// }

package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.ConductorProcedimientoDAO;
import com.mycompany.terminalbusesDAL.ConductorVista;

public class ConductorProcedimientoService {

    private final ConductorProcedimientoDAO dao = new ConductorProcedimientoDAO();

    /** Ingresar */
    public boolean crearConductor(ConductorVista c) {
        return dao.insertarConductor(c);
    }

    /** Actualizar (pasa null para no cambiar terminal o cédula) */
    public boolean actualizarConductor(ConductorVista c, Integer codTerminalNuevo, String cedulaNueva) {
        return dao.actualizarConductor(c, codTerminalNuevo, cedulaNueva);
    }

    /** Borrar */
    public boolean borrarConductor(int codConductor) {
        return dao.eliminarConductor(codConductor);
    }
}