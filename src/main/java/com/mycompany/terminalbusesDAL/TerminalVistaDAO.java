package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TerminalVistaDAO {

    public List<TerminalVista> obtenerTodosLosTerminales() {
        List<TerminalVista> lista = new ArrayList<>();

        String sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].dbo.Terminal_Vista";

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
            }

            System.out.println("DAO TERMINALES: ENCONTRADAS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista: " + e.getMessage());
        }

        return lista;
    }
}
