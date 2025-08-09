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

public class TerminalVistaDAO {

    public List<TerminalVista> obtenerTodosLosTerminales(String ciudad) {
        List<TerminalVista> lista = new ArrayList<>();
        String sql = " ";
    
        switch (ciudad.toLowerCase()) {
        case "ibarra":
            sql = "SELECT * FROM Terminal_Ibarra_vista";
            break;
        case "quito":
            sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Terminal_Quito_vista]";
            break;
        default:
            System.err.println("Ciudad no reconocida: " + ciudad);
            return lista;
    }
    

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TerminalVista terminal = new TerminalVista(
                    rs.getInt("cod_terminal"),
                    rs.getString("ciudad_terminal"),
                    rs.getString("nombre_terminal"),
                    rs.getString("direccion_terminal")
                );
                lista.add(terminal);
            System.out.println("DAO TERMINALES: ENCONTRADAS " + lista.size());
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista: " + e.getMessage());
        }

        return lista;
    }
}
