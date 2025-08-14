package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasajeroVistaDAO {

    public List<PasajeroVista> obtenerTodosLosPasajeros() {
        List<PasajeroVista> lista = new ArrayList<>();
        final String sql = "SELECT * FROM [VLADIMIRJON].[Terminal_Quito].dbo.Pasajero";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PasajeroVista pasajero = new PasajeroVista(
                        rs.getString("cedula_pasajero"),
                        rs.getString("nombre_pasajero"),
                        rs.getString("apellido_pasajero"),
                        rs.getString("telefono_pasajero"),
                        rs.getString("correo_pasajero")
                );
                lista.add(pasajero);
            }

            System.out.println("DAO PASAJEROS: ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar Pasajero_Vista: " + e.getMessage());
        }

        return lista;
    }


}
