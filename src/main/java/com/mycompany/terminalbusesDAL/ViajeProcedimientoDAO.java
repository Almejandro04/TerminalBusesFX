package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ViajeProcedimientoDAO {

    /** Inserta un viaje usando el SP actual */
    public boolean insertarViaje(ViajeVista v) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.InsertarViaje(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, v.getCodTerminal());
            cs.setInt(2, v.getCodViaje());
            cs.setString(3, v.getPlaca());
            cs.setInt(4, v.getCodRuta());
            cs.setInt(5, v.getCodConductor());
            cs.setDate(6, java.sql.Date.valueOf(v.getFechaViaje()));
            cs.setTime(7, java.sql.Time.valueOf(v.getHoraViaje()));

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarViaje(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza un viaje usando el SP actual */
    public boolean actualizarViaje(ViajeVista v) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.ActualizarViaje(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, v.getCodTerminal());
            cs.setInt(2, v.getCodViaje());

            if (v.getPlaca() != null) {
                cs.setString(3, v.getPlaca());
            } else {
                cs.setNull(3, java.sql.Types.VARCHAR);
            }

            if (v.getCodRuta() != 0) {
                cs.setInt(4, v.getCodRuta());
            } else {
                cs.setNull(4, java.sql.Types.INTEGER);
            }

            if (v.getCodConductor() != 0) {
                cs.setInt(5, v.getCodConductor());
            } else {
                cs.setNull(5, java.sql.Types.INTEGER);
            }

            if (v.getFechaViaje() != null) {
                cs.setDate(6, java.sql.Date.valueOf(v.getFechaViaje()));
            } else {
                cs.setNull(6, java.sql.Types.DATE);
            }

            if (v.getHoraViaje() != null) {
                cs.setTime(7, java.sql.Time.valueOf(v.getHoraViaje()));
            } else {
                cs.setNull(7, java.sql.Types.TIME);
            }

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarViaje(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina un viaje usando el SP actual */
    public boolean eliminarViaje(int codTerminal, int codViaje) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.EliminarViaje(?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, codTerminal);
            cs.setInt(2, codViaje);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarViaje(): " + e.getMessage());
            return false;
        }
    }
}
