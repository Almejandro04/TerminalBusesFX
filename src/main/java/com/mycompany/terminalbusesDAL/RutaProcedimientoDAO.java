package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RutaProcedimientoDAO {

    /** Inserta una nueva ruta */
    public boolean insertarRuta(RutaVista r) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.InsertarRuta(?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, r.getCodTerminal());
            cs.setInt(2, r.getCodRuta());
            cs.setString(3, r.getCiudadDestino());
            cs.setDouble(4, r.getPrecio());

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarRuta(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza una ruta existente */
    public boolean actualizarRuta(RutaVista r) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.ActualizarRuta(?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, r.getCodTerminal());
            cs.setInt(2, r.getCodRuta());

            if (r.getCiudadDestino() != null) {
                cs.setString(3, r.getCiudadDestino());
            } else {
                cs.setNull(3, java.sql.Types.VARCHAR);
            }

            if (r.getPrecio() != null) {
                cs.setDouble(4, r.getPrecio());
            } else {
                cs.setNull(4, java.sql.Types.DECIMAL);
            }

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarRuta(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina una ruta */
    public boolean eliminarRuta(int codTerminal, int codRuta) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.EliminarRuta(?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, codTerminal);
            cs.setInt(2, codRuta);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarRuta(): " + e.getMessage());
            return false;
        }
    }
}
