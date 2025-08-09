/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;

import java.sql.SQLException;

import com.mycompany.terminalbusesDAL.PasajeroProcedimientoDAO;
import com.mycompany.terminalbusesDAL.PasajeroVista;

/**
 *
 * @author Jorge
 */
public class PasajeroProcedimientoService {
        // ➡ Aquí usa el DAO correcto
    private final PasajeroProcedimientoDAO dao = new PasajeroProcedimientoDAO();

    public int crearPasajero(PasajeroVista pasajero, String ciudad) {
        try {
            //return dao.insertarViajeCompleto(viaje); 
            return dao.insertarPasajeroCompleto(pasajero, ciudad);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pasajero", e);
        }
    }

        public boolean borrarPasajero(int codPasajero, String ciudad) {
        try {
            return dao.eliminarPasajero(codPasajero, ciudad);
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar pasajero", e);
        }
    }

        public boolean actualizarPasajero(PasajeroVista p, String ciudad) {
        try {
            return dao.actualizarPasajeroCompleto(p, ciudad);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pasajero", e);
        }
    }
}
