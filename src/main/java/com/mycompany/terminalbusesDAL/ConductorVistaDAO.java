package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConductorVistaDAO {

    public List<ConductorVista> obtenerTodosLosConductores(String ciudad) {
        List<ConductorVista> lista = new ArrayList<>();
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return lista;
        }

        String sql;
        switch (ciudad.toLowerCase().trim()) {
            case "ibarra":
                sql = "SELECT * FROM dbo.Conductor_Ibarra_Vista";
                break;
            case "quito":
                sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Conductor_Quito_Vista]";
                break;
            default:
                System.err.println("Ciudad no reconocida: " + ciudad);
                return lista;
        }

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ConductorVista c = new ConductorVista(
                        rs.getInt("cod_conductor"),
                        rs.getInt("cod_terminal"),
                        rs.getString("cedula_conductor"),
                        rs.getString("nombre_conductor"),
                        rs.getString("apellido_conductor")
                );
                lista.add(c);
            }

            System.out.println("DAO CONDUCTORES (" + ciudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de conductores (" + ciudad + "): " + e.getMessage());
        }

        return lista;
    }
}
