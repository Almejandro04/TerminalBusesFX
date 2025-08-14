package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class PasajeroProcedimientoDAO {

    /** Inserta un pasajero */
    public boolean insertarPasajero(PasajeroVista p) {
        String sql = "{CALL dbo.InsertarPasajero(?, ?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, p.getCedulaPasajero());
            cs.setString(2, p.getNombrePasajero());
            cs.setString(3, p.getApellidoPasajero());
            cs.setString(4, p.getTelefonoPasajero());
            cs.setString(5, p.getCorreoPasajero());
            cs.setNull(6, java.sql.Types.VARCHAR); // rowguid → lo genera el SP si es null

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarPasajero(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza un pasajero existente */
    public boolean actualizarPasajero(PasajeroVista p) {
        String sql = "{CALL dbo.ActualizarPasajero(?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, p.getCedulaPasajero());
            cs.setString(2, p.getNombrePasajero());   // puede ser null
            cs.setString(3, p.getApellidoPasajero()); // puede ser null
            cs.setString(4, p.getTelefonoPasajero()); // puede ser null
            cs.setString(5, p.getCorreoPasajero());   // puede ser null

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarPasajero(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina un pasajero por cédula */
    public boolean eliminarPasajero(String cedulaPasajero) {
        String sql = "{CALL dbo.EliminarPasajero(?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, cedulaPasajero);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarPasajero(): " + e.getMessage());
            return false;
        }
    }
}
