package com.mycompany.terminalbusesDAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class VehiculoProcedimientoDAO {

    /** Inserta un nuevo vehículo (bus) */
    public boolean insertarVehiculo(VehiculoVista v) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.InsertarBus(?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, v.getCodTerminal());
            cs.setString(2, v.getPlacaVehiculo());
            cs.setInt(3, v.getCapacidadVehiculo());

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertarVehiculo(): " + e.getMessage());
            return false;
        }
    }

    /** Actualiza un vehículo existente */
    public boolean actualizarVehiculo(VehiculoVista v) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.ActualizarBus(?, ?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, v.getCodTerminal());
            cs.setString(2, v.getPlacaVehiculo());

            if (v.getCapacidadVehiculo() > 0) { // 0 no válido
                cs.setInt(3, v.getCapacidadVehiculo());
            } else {
                cs.setNull(3, java.sql.Types.INTEGER);
            }

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizarVehiculo(): " + e.getMessage());
            return false;
        }
    }

    /** Elimina un vehículo por terminal y placa */
    public boolean eliminarVehiculo(int codTerminal, String placaVehiculo) {
        String sql = "{CALL [VLADIMIRJON].[Terminal_Quito].dbo.EliminarBus(?, ?)}";
        try (Connection cn = ConexionBD.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, codTerminal);
            cs.setString(2, placaVehiculo);

            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminarVehiculo(): " + e.getMessage());
            return false;
        }
    }
}
