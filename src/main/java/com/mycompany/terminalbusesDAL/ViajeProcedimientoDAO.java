/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Jorge
 */

package com.mycompany.terminalbusesDAL;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**  
 * DAO que ejecuta el procedimiento almacenado InsertarViajeCompleto  
 */
public class ViajeProcedimientoDAO {

    public int insertarViajeCompleto(ViajeVista v, String ciudad) throws SQLException {
        String procedimiento = "";
        switch (ciudad.toLowerCase()) {
            case "ibarra":
                procedimiento = "{ CALL dbo.InsertarViajeCompleto(?, ?, ?, ?, ?, ?, ?, ?) }";
                break;
            case "quito":
                // Si el SP está en la base de Vladimirjon usa el nombre completo
                procedimiento = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[InsertarViajeCompleto](?, ?, ?, ?, ?, ?, ?, ?) }";
                break;
            default:
                throw new IllegalStateException("Ciudad de usuario desconocida: " + ciudad);
        }
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(procedimiento)) {
            cs.setInt   (1, v.getCodVehiculo());
            cs.setInt   (2, v.getCodConductor());
            cs.setInt   (3, v.getCodTerminal());
            cs.setString(4, v.getCiudadDestino());
            cs.setBigDecimal(5, java.math.BigDecimal.valueOf(v.getPrecioViaje()));
            cs.setTime  (6, java.sql.Time.valueOf(v.getHoraViaje()));
            cs.setDate  (7, java.sql.Date.valueOf(v.getFechaViaje()));
            cs.registerOutParameter(8, Types.INTEGER);
            cs.execute();
            return cs.getInt(8);
        }
    }
    
    
    public boolean eliminarViaje(int codViaje, String ciudad) throws SQLException {
    String sql;
    switch (ciudad.toLowerCase()) {
        case "ibarra":
            sql = "{ CALL dbo.EliminarViajeCompleto(?) }";
            break;
        case "quito":
            sql = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[EliminarViajeCompleto](?) }";
            break;
        default:
            throw new IllegalArgumentException("Ciudad no reconocida: " + ciudad);
    }

    try (Connection cn = ConexionBD.conectar();
         PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setInt(1, codViaje);
        int filas = ps.executeUpdate();
        return filas > 0;
    }
}

public void actualizarViajeCompleto(ViajeVista v, String ciudad) throws SQLException {
        String procedimiento;
        switch (ciudad.toLowerCase()) {
            case "ibarra":
                procedimiento = "{ CALL dbo.ActualizarViajeCompleto(?, ?, ?, ?, ?, ?, ?, ?) }";
                break;
            case "quito":
                procedimiento = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[ActualizarViajeCompleto](?, ?, ?, ?, ?, ?, ?, ?) }";
                break;
            default:
                throw new IllegalStateException("Ciudad de usuario desconocida: " + ciudad);
        }

        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(procedimiento)) {

            cs.setInt      (1, v.getCodViaje());
            cs.setInt      (2, v.getCodVehiculo());
            cs.setInt      (3, v.getCodConductor());
            cs.setInt      (4, v.getCodTerminal());
            cs.setString   (5, v.getCiudadDestino());
            cs.setBigDecimal(6, BigDecimal.valueOf(v.getPrecioViaje()));
            cs.setDate     (7, java.sql.Date.valueOf(v.getFechaViaje()));
            cs.setTime     (8, java.sql.Time.valueOf(v.getHoraViaje()));

            cs.execute();  // si no lanza excepción, asumimos éxito
        }
    }

}