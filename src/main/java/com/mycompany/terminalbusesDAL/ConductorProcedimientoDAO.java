package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ConductorProcedimientoDAO {

    /** Inserta un nuevo conductor usando el procedimiento almacenado */
    public boolean insertarConductor(ConductorVista c) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.InsertarConductor(?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {
                
            cs.setInt(1, c.getCodConductor());     // @cod_conductor
            cs.setInt(2, c.getCodTerminal());      // @cod_terminal
            cs.setString(3, c.getCedulaConductor());// @cedula_conductor
            cs.setString(4, c.getNombreConductor());// @nombre_conductor
            cs.setString(5, c.getApellidoConductor());// @apellido_conductor

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarConductor(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza un conductor existente */
    public boolean actualizarConductor(ConductorVista c,
                                        Integer codTerminalNuevo, // null si no cambia
                                        String cedulaNueva) {     // null si no cambia
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.ActualizarConductor(?, ?, ?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, c.getCodConductor());
            cs.setString(2, c.getNombreConductor());   // puede ser null
            cs.setString(3, c.getApellidoConductor()); // puede ser null
            if (codTerminalNuevo != null) {
                cs.setInt(4, codTerminalNuevo);
            } else {
                cs.setNull(4, java.sql.Types.INTEGER);
            }
            if (cedulaNueva != null) {
                cs.setString(5, cedulaNueva);
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarConductor(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina un conductor por su código */
    public boolean eliminarConductor(int codConductor) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.EliminarConductor(?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, codConductor);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarConductor(): " + e.getMessage());
            return false;
        }
    }
}
