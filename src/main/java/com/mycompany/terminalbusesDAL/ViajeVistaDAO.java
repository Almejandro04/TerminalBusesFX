package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViajeVistaDAO {

    // Consultas filtradas sobre la vista global
    private static final String SQL_VIAJES_QUITO =
        "SELECT cod_terminal, cod_viaje, placa, cod_ruta, cod_conductor, fecha_viaje, hora_viaje " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Viaje_Vista] " +
        "WHERE cod_terminal = 1";

    private static final String SQL_VIAJES_IBARRA =
        "SELECT cod_terminal, cod_viaje, placa, cod_ruta, cod_conductor, fecha_viaje, hora_viaje " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Viaje_Vista] " +
        "WHERE cod_terminal = 2";

    /** Caso 1: Quito */
    public List<ViajeVista> obtenerViajesQuito() {
        return ejecutarListado(SQL_VIAJES_QUITO, "quito");
    }

    /** Caso 2: Ibarra */
    public List<ViajeVista> obtenerViajesIbarra() {
        return ejecutarListado(SQL_VIAJES_IBARRA, "ibarra");
    }

    /**
     * Router para compatibilidad con la firma original.
     */
    public List<ViajeVista> obtenerTodos(String ciudad) {
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return new ArrayList<>();
        }

        switch (ciudad.toLowerCase().trim()) {
            case "quito":  return obtenerViajesQuito();
            case "ibarra": return obtenerViajesIbarra();
            default:
                System.err.println("Ciudad no reconocida: " + ciudad + " (usa 'quito' o 'ibarra')");
                return new ArrayList<>();
        }
    }

    // =======================
    // Helper
    // =======================
    private List<ViajeVista> ejecutarListado(String sql, String etiquetaCiudad) {
        List<ViajeVista> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ViajeVista(
                    rs.getInt("cod_terminal"),
                    rs.getInt("cod_viaje"),
                    rs.getString("placa"),
                    rs.getInt("cod_ruta"),
                    rs.getInt("cod_conductor"),
                    rs.getDate("fecha_viaje").toLocalDate(),
                    rs.getTime("hora_viaje").toLocalTime()
                ));
            }

            System.out.println("DAO VIAJES (" + etiquetaCiudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de viajes (" + etiquetaCiudad + "): " + e.getMessage());
        }
        return lista;
    }
}
