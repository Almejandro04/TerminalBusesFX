package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class BoletoProcedimientoDAO {

    /** Inserta un nuevo boleto */
    public boolean insertarBoleto(BoletoVista b) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.InsertarBoleto(?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, b.getCodTerminal());
            cs.setInt(2, b.getCodViaje());
            cs.setString(3, b.getCedulaPasajero());
            cs.setInt(4, b.getNumAsiento());

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarBoleto(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza un boleto existente */
    public boolean actualizarBoleto(BoletoVista b) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.ActualizarBoleto(?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, b.getCodTerminal());
            cs.setInt(2, b.getCodViaje());
            cs.setString(3, b.getCedulaPasajero());

            if (b.getNumAsiento() > 0) { // si es >0 lo actualizamos
                cs.setInt(4, b.getNumAsiento());
            } else {
                cs.setNull(4, java.sql.Types.INTEGER);
            }

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarBoleto(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina un boleto */
    public boolean eliminarBoleto(int codTerminal, int codViaje, String cedulaPasajero) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.EliminarBoleto(?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, codTerminal);
            cs.setInt(2, codViaje);
            cs.setString(3, cedulaPasajero);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarBoleto(): " + e.getMessage());
            return false;
        }
    }
}
