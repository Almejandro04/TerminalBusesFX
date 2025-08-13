package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RutaVistaDAO {

    // Vista global con filtro por cod_terminal
    private static final String SQL_RUTAS_QUITO =
        "SELECT cod_terminal, cod_ruta, ciudad_destino, precio " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Ruta_Vista] " +
        "WHERE cod_terminal = 1";

    private static final String SQL_RUTAS_IBARRA =
        "SELECT cod_terminal, cod_ruta, ciudad_destino, precio " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Ruta_Vista] " +
        "WHERE cod_terminal = 2";

    public List<RutaVista> obtenerRutasQuito() {
        return ejecutarListado(SQL_RUTAS_QUITO, "quito");
    }

    public List<RutaVista> obtenerRutasIbarra() {
        return ejecutarListado(SQL_RUTAS_IBARRA, "ibarra");
    }

    /**
     * Método router para compatibilidad con tu firma actual.
     */
    public List<RutaVista> obtenerTodasLasRutas(String ciudad) {
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return new ArrayList<>();
        }

        switch (ciudad.toLowerCase().trim()) {
            case "quito":  return obtenerRutasQuito();
            case "ibarra": return obtenerRutasIbarra();
            default:
                System.err.println("Ciudad no reconocida: " + ciudad + " (usa 'quito' o 'ibarra')");
                return new ArrayList<>();
        }
    }

    // =======================
    // Helper
    // =======================
    private List<RutaVista> ejecutarListado(String sql, String etiquetaCiudad) {
        List<RutaVista> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new RutaVista(
                    rs.getInt("cod_terminal"),
                    rs.getInt("cod_ruta"),
                    rs.getString("ciudad_destino"),
                    rs.getDouble("precio")
                ));
            }

            System.out.println("DAO RUTAS (" + etiquetaCiudad + "): ENCONTRADAS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de rutas (" + etiquetaCiudad + "): " + e.getMessage());
        }
        return lista;
    }
}
