package com.mycompany.terminalbusesDAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoletoVistaDAO {

    // Consultas filtradas sobre la vista global
    private static final String SQL_BOLETOS_QUITO =
        "SELECT cod_terminal, cod_viaje, cedula_pasajero, num_asiento " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Boleto_Vista] " +
        "WHERE cod_terminal = 1";

    private static final String SQL_BOLETOS_IBARRA =
        "SELECT cod_terminal, cod_viaje, cedula_pasajero, num_asiento " +
        "FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Boleto_Vista] " +
        "WHERE cod_terminal = 2";

    /** Caso Quito */
    public List<BoletoVista> obtenerBoletosQuito() {
        return ejecutarListado(SQL_BOLETOS_QUITO, "quito");
    }

    /** Caso Ibarra */
    public List<BoletoVista> obtenerBoletosIbarra() {
        return ejecutarListado(SQL_BOLETOS_IBARRA, "ibarra");
    }

    /** Router para compatibilidad */
    public List<BoletoVista> obtenerTodosLosBoletos(String ciudad) {
        if (ciudad == null) {
            System.err.println("Ciudad no puede ser null.");
            return new ArrayList<>();
        }
        switch (ciudad.toLowerCase().trim()) {
            case "quito":  return obtenerBoletosQuito();
            case "ibarra": return obtenerBoletosIbarra();
            default:
                System.err.println("Ciudad no reconocida: " + ciudad + " (usa 'quito' o 'ibarra')");
                return new ArrayList<>();
        }
    }

    // =======================
    // Helper
    // =======================
    private List<BoletoVista> ejecutarListado(String sql, String etiquetaCiudad) {
        List<BoletoVista> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            System.out.println("DAO BOLETOS (" + etiquetaCiudad + "): ENCONTRADOS " + lista.size());

        } catch (SQLException e) {
            System.err.println("Error al consultar la vista de boletos (" + etiquetaCiudad + "): " + e.getMessage());
        }
        return lista;
    }

    private BoletoVista mapRow(ResultSet rs) throws SQLException {
        return new BoletoVista(
            rs.getInt("cod_terminal"),
            rs.getInt("cod_viaje"),
            rs.getString("cedula_pasajero"),
            rs.getInt("num_asiento")
        );
    }

    // =======================
// Métodos de escritura (CRUD)
// =======================

public boolean insertarBoleto(BoletoVista b) {
    String sql = "{CALL dbo.InsertarBoleto(?, ?, ?, ?)}";
    try (Connection conn = ConexionBD.conectar();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, b.getCodTerminal());
        cs.setInt(2, b.getCodViaje());
        cs.setString(3, b.getCedulaPasajero());
        cs.setInt(4, b.getNumAsiento());

        cs.execute();
        return true;
    } catch (SQLException e) {
        System.err.println("Error insertarBoleto(): " + e.getMessage());
        return false;
    }
}

public boolean actualizarBoleto(BoletoVista b) {
    String sql = "{CALL dbo.ActualizarBoleto(?, ?, ?, ?)}";
    try (Connection conn = ConexionBD.conectar();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, b.getCodTerminal());
        cs.setInt(2, b.getCodViaje());
        cs.setString(3, b.getCedulaPasajero());
        cs.setInt(4, b.getNumAsiento()); // si quieres permitir null, usa setObject

        cs.execute();
        return true;
    } catch (SQLException e) {
        System.err.println("Error actualizarBoleto(): " + e.getMessage());
        return false;
    }
}

public boolean eliminarBoleto(int codTerminal, int codViaje, String cedulaPasajero) {
    String sql = "{CALL dbo.EliminarBoleto(?, ?, ?)}";
    try (Connection conn = ConexionBD.conectar();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, codTerminal);
        cs.setInt(2, codViaje);
        cs.setString(3, cedulaPasajero);

        cs.execute();
        return true;
    } catch (SQLException e) {
        System.err.println("Error eliminarBoleto(): " + e.getMessage());
        return false;
    }
}

}
