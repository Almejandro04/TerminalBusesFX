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

public class ConductorVistaDAO {
    
public List<ConductorVista> obtenerTodosLosConductores(String ciudad) {
    List<ConductorVista> lista = new ArrayList<>();
    
    String sql = " ";
    
    switch (ciudad.toLowerCase()) {
        case "ibarra":
            sql = "SELECT * FROM Conductor_Ibarra_vista";
            break;
        case "quito":
            sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Conductor_Quito_vista]";
            break;
        default:
            System.err.println("Ciudad no reconocida: " + ciudad);
            return lista;
    }
    
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ConductorVista conductor = new ConductorVista(
                rs.getInt("cod_conductor"),
                rs.getInt("cod_terminal"),
                rs.getString("nombre_conductor"),
                rs.getString("apellido_conductor"),
                rs.getString("licencia_conductor")
            );
            lista.add(conductor);
        }

    } catch (SQLException e) {
        System.err.println("Error al consultar la vista: " + e.getMessage());
    }

    return lista;
}

}

