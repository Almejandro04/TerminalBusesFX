package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RutaVistaDAO {

    public List<RutaVista> obtenerTodasLasRutas(String ciudad) {
        List<RutaVista> lista = new ArrayList<>();
        String sql;

        switch (ciudad.toLowerCase().trim()) {
            case "ibarra":
                sql = "SELECT * FROM dbo.Ruta_Ibarra_Vista";
                break;
            case "quito":
                sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Ruta_Quito_Vista]";
                break;
            default:
                System.err.println("Ciudad no reconocida: " + ciudad);
                return lista;
        }

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RutaVista ruta = new RutaVista(
                        rs.getInt("cod_terminal"),
                        rs.getInt("cod_ruta"),
                        rs.getString("ciudad_destino"),
                        rs.getDouble("precio")
                );
                lista.add(ruta);
            }

            System.out.println("DAO RUTAS (" + ciudad + "): ENCONTRADAS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de rutas (" + ciudad + "): " + e.getMessage());
        }

        return lista;
    }
}
