/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;
import java.sql.SQLException;

import com.mycompany.terminalbusesDAL.ViajeProcedimientoDAO;
import com.mycompany.terminalbusesDAL.ViajeVista;


/**
 *
 * @author Jorge
 */
public class ViajeProcedimientoService {
    // ➡ Aquí usa el DAO correcto
    private final ViajeProcedimientoDAO dao = new ViajeProcedimientoDAO();

    public int crearViaje(ViajeVista viaje, String ciudad) {
        try {
            //return dao.insertarViajeCompleto(viaje); 
            return dao.insertarViajeCompleto(viaje, ciudad);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear viaje", e);
        }
    }


    public boolean borrarViaje(int codViaje, String ciudad) {
    try {
        return dao.eliminarViaje(codViaje, ciudad);
    } catch (SQLException e) {
        throw new RuntimeException("Error al borrar viaje", e);
    }
}    /** Actualiza un viaje existente */
    
public boolean actualizarViaje(ViajeVista v, String ciudad) {
        try {
            dao.actualizarViajeCompleto(v, ciudad);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar viaje", e);
        }
    }
}

