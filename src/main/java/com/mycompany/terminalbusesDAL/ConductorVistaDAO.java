package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConductorVistaDAO {

    // Consultas filtradas sobre la vista global
    private static final String SQL_CONDUCTORES_QUITO =
        "SELECT cod_conductor, cod_terminal, cedula_conductor, nombre_conductor, apellido_conductor " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Conductor_Vista] " +
        "WHERE cod_terminal = 1";

    private static final String SQL_CONDUCTORES_IBARRA =
        "SELECT cod_conductor, cod_terminal, cedula_conductor, nombre_conductor, apellido_conductor " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Conductor_Vista] " +
        "WHERE cod_terminal = 2";

    /** Caso 1: SOLO elementos de Quito */
    public List<ConductorVista> obtenerConductoresQuito() {
        return ejecutarListado(SQL_CONDUCTORES_QUITO, "quito");
    }

    /** Caso 2: SOLO elementos de Ibarra */
    public List<ConductorVista> obtenerConductoresIbarra() {
        return ejecutarListado(SQL_CONDUCTORES_IBARRA, "ibarra");
    }

    /**
     * Router por compatibilidad con tu firma original.
     */
    public List<ConductorVista> obtenerTodosLosConductores(String ciudad) {
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return new ArrayList<>();
        }
        switch (ciudad.toLowerCase().trim()) {
            case "quito":  return obtenerConductoresQuito();
            case "ibarra": return obtenerConductoresIbarra();
            default:
                System.err.println("Ciudad no reconocida: " + ciudad + " (usa 'quito' o 'ibarra')");
                return new ArrayList<>();
        }
    }

    // =======================
    // Helpers
    // =======================
    private List<ConductorVista> ejecutarListado(String sql, String etiquetaCiudad) {
        List<ConductorVista> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            System.out.println("DAO CONDUCTORES (" + etiquetaCiudad + "): ENCONTRADOS " + lista.size());
        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de conductores (" + etiquetaCiudad + "): " + e.getMessage());
        }
        return lista;
    }

    private ConductorVista mapRow(ResultSet rs) throws SQLException {
        return new ConductorVista(
            rs.getInt("cod_conductor"),
            rs.getInt("cod_terminal"),
            rs.getString("cedula_conductor"),
            rs.getString("nombre_conductor"),
            rs.getString("apellido_conductor")
        );
    }
}
