package com.mycompany.terminalbusesDAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViajeVistaDAO {

    public List<ViajeVista> obtenerTodos(String ciudad) {
        List<ViajeVista> lista = new ArrayList<>();
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return lista;
        }

        String sql;
        switch (ciudad.toLowerCase().trim()) {
            case "ibarra":
                sql = "SELECT * FROM dbo.Viaje_Ibarra_Vista";
                break;
            case "quito":
                sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Viaje_Quito_Vista]";
                break;
            default:
                System.err.println("Ciudad no reconocida: " + ciudad);
                return lista;
        }

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ViajeVista viaje = new ViajeVista(
                        rs.getInt("cod_terminal"),
                        rs.getInt("cod_viaje"),
                        rs.getString("placa"),
                        rs.getInt("cod_ruta"),
                        rs.getInt("cod_conductor"),
                        rs.getDate("fecha_viaje").toLocalDate(),
                        rs.getTime("hora_viaje").toLocalTime()
                );
                lista.add(viaje);
            }

            System.out.println("DAO VIAJES (" + ciudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de viajes (" + ciudad + "): " + e.getMessage());
        }

        return lista;
    }
}
