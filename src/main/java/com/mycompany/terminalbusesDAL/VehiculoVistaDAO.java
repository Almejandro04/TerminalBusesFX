/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

/**
 *
 * @author Jorge
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoVistaDAO {
  public List<VehiculoVista> obtenerTodosLosVehiculos(String ciudad) {
        List<VehiculoVista> lista = new ArrayList<>();
        String sql = " ";
    
        switch (ciudad.toLowerCase()) {
        case "ibarra":
            sql = "SELECT * FROM Vehiculo_Ibarra_vista";
            break;
        case "quito":
            sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Vehiculo_Quito_vista]";
            break;
        default:
            System.err.println("Ciudad no reconocida: " + ciudad);
            return lista;
    }
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VehiculoVista terminal = new VehiculoVista(
                    rs.getInt("cod_vehiculo"),
                    rs.getInt("cod_terminal"),
                    rs.getString("placa_vehiculo"),
                    rs.getInt("capacidad_vehiculo"),
                    rs.getString("compañia_vehiculo")  
                );
                lista.add(terminal);
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista: " + e.getMessage());
        }

        return lista;
    }
  
}
