package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoVistaDAO {

    // Consultas filtradas sobre la vista global
    private static final String SQL_VEHICULOS_QUITO =
        "SELECT cod_terminal, placa, capacidad " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Bus_Vista] " +
        "WHERE cod_terminal = 1";

    private static final String SQL_VEHICULOS_IBARRA =
        "SELECT cod_terminal, placa, capacidad " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Bus_Vista] " +
        "WHERE cod_terminal = 2";

    /** Caso 1: Quito */
    public List<VehiculoVista> obtenerVehiculosQuito() {
        return ejecutarListado(SQL_VEHICULOS_QUITO, "quito");
    }

    /** Caso 2: Ibarra */
    public List<VehiculoVista> obtenerVehiculosIbarra() {
        return ejecutarListado(SQL_VEHICULOS_IBARRA, "ibarra");
    }

    /**
     * Router para compatibilidad con la firma original.
     */
    public List<VehiculoVista> obtenerTodosLosVehiculos(String ciudad) {
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return new ArrayList<>();
        }

        switch (ciudad.toLowerCase().trim()) {
            case "quito":  return obtenerVehiculosQuito();
            case "ibarra": return obtenerVehiculosIbarra();
            default:
                System.err.println("Ciudad no reconocida: " + ciudad + " (usa 'quito' o 'ibarra')");
                return new ArrayList<>();
        }
    }

    // =======================
    // Helper
    // =======================
    private List<VehiculoVista> ejecutarListado(String sql, String etiquetaCiudad) {
        List<VehiculoVista> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new VehiculoVista(
                    rs.getInt("cod_terminal"),
                    rs.getString("placa"),
                    rs.getInt("capacidad")
                ));
            }

            System.out.println("DAO VEHICULOS (" + etiquetaCiudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de vehículos (" + etiquetaCiudad + "): " + e.getMessage());
        }
        return lista;
    }
}
