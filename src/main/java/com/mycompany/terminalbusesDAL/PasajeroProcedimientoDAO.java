/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author Jorge
 */
public class PasajeroProcedimientoDAO {
    public int insertarPasajeroCompleto(PasajeroVista p, String ciudad) throws SQLException {
        String sql;
        switch (ciudad.toLowerCase()) {
            case "ibarra":
                sql = "{ CALL dbo.InsertarPasajeroCompleto(?, ?, ?, ?, ?, ?, ?) }";
                break;
            case "quito":
                sql = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[InsertarPasajeroCompleto](?, ?, ?, ?, ?, ?, ?) }";
                break;
            default:
                throw new IllegalArgumentException("Ciudad no reconocida: " + ciudad);
        }
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {
                cs.setInt(1, p.getCodViaje());
                cs.setString(2, p.getNombrePasajero());
                cs.setString(3, p.getApellidoPasajero());
                cs.setString(4, p.getCedulaPasajero());
                cs.setString(5, p.getTelefonoPasajero());
                cs.setString(6, p.getCorreoPasajero());
                cs.registerOutParameter(7, java.sql.Types.INTEGER);
                cs.execute();
            return cs.getInt(7);
        }
    }

    

 public boolean eliminarPasajero(int codPasajero, String ciudad) throws SQLException {
        String sql;
        switch (ciudad.toLowerCase()) {
            case "ibarra":
                sql = "{ CALL dbo.EliminarPasajeroCompleto(?) }";
                break;
            case "quito":
                sql = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[EliminarPasajeroCompleto](?) }";
                break;
            default:
                throw new IllegalArgumentException("Ciudad no reconocida: " + ciudad);
        }

        try (Connection cn = ConexionBD.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, codPasajero);
            int filas = ps.executeUpdate();
            return filas > 0;
        }catch (SQLException e) {
            System.out.println("Error SQL al borrar pasajero: " + e.getMessage());
            e.printStackTrace();  // Esto mostrará el error completo en consola
            throw new SQLException("No se pudo borrar el pasajero.");
        }
    }


     public boolean actualizarPasajeroCompleto(PasajeroVista p, String ciudad) throws SQLException {
        String sql;
        switch (ciudad.toLowerCase()) {
            case "ibarra":
                sql = "{ CALL dbo.ActualizarPasajeroCompleto(?, ?, ?, ?, ?, ?, ?) }";
                break;
            case "quito":
                sql = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[ActualizarPasajeroCompleto](?, ?, ?, ?, ?, ?, ?) }";
                break;
            default:
                throw new IllegalArgumentException("Ciudad no reconocida: " + ciudad);
        }

        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, p.getCodPasajero());
            cs.setInt(2, p.getCodViaje());
            cs.setString(3, p.getNombrePasajero());
            cs.setString(4, p.getApellidoPasajero());
            cs.setString(5, p.getCedulaPasajero());
            cs.setString(6, p.getTelefonoPasajero());
            cs.setString(7, p.getCorreoPasajero());

            cs.execute();            // <<-- ejecuta el SP, no nos importa el row-count
            return true;    
        }
    }
}


