package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoVistaDAO {

    public List<VehiculoVista> obtenerTodosLosVehiculos(String ciudad) {
        List<VehiculoVista> lista = new ArrayList<>();
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return lista;
        }

        String sql;
        switch (ciudad.toLowerCase().trim()) {
            case "ibarra":
                // Vista local en Terminal_Ibarra
                sql = "SELECT * FROM dbo.Bus_Ibarra_Vista";
                break;
            case "quito":
                // Vista en la otra base (ajusta servidor/esquema si difiere)
                sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Bus_Quito_Vista]";
                break;
            default:
                System.err.println("Ciudad no reconocida: " + ciudad);
                return lista;
        }

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VehiculoVista vehiculo = new VehiculoVista(
                        rs.getInt("cod_terminal"),
                        rs.getString("placa_vehiculo"),
                        rs.getInt("capacidad_vehiculo")
                );
                lista.add(vehiculo);
            }

            System.out.println("DAO VEHICULOS (" + ciudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de vehículos (" + ciudad + "): " + e.getMessage());
        }

        return lista;
    }
}
