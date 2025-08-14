package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.VehiculoProcedimientoDAO;
import com.mycompany.terminalbusesDAL.VehiculoVista;

public class VehiculoProcedimientoService {

    private final VehiculoProcedimientoDAO dao = new VehiculoProcedimientoDAO();

    /** Ingresar vehículo (bus) */
    public boolean crearVehiculo(VehiculoVista v) {
        return dao.insertarVehiculo(v);
    }

    /** Actualizar vehículo (capacidad; deja 0 si no deseas modificarla y el DAO enviará NULL) */
    public boolean actualizarVehiculo(VehiculoVista v) {
        return dao.actualizarVehiculo(v);
    }

    /** Borrar vehículo por (terminal, placa) */
    public boolean borrarVehiculo(int codTerminal, String placaVehiculo) {
        return dao.eliminarVehiculo(codTerminal, placaVehiculo);
    }
}
