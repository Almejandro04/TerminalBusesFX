// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//  */
// package com.mycompany.terminalbusesDAL;

// /**
//  *
//  * @author Jorge
//  */

//     /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//  */

// import java.sql.CallableStatement;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// /**
//  *
//  * @author Jorge
//  */
// public class ConductorProcedimientoDAO {
//     public boolean insertarConductorCompleto(ConductorVista c, String ciudad) throws SQLException {
//         String sql;
//         switch (ciudad.toLowerCase()) {
//             case "ibarra":
//                 sql = "{ CALL dbo.InsertarConductorCompleto(?, ?, ?, ?) }";
//                 break;
//             case "quito":
//                 sql = "{ CALL [VLADIMIRJON].[Terminal_Quito].[dbo].[InsertarConductorCompleto](?, ?, ?, ?) }";
//                 break;
//             default:
//                 throw new IllegalArgumentException("Conductor no reconocido: " + ciudad);
//         }
//         try (Connection cn = ConexionBD.conectar();
//              CallableStatement cs = cn.prepareCall(sql)) {

//                 cs.setInt(1, c.getCodTerminal());
//                 cs.setString(2, c.getNombreConductor());
//                 cs.setString(3, c.getApellidoConductor());
//                 cs.setString(4, c.getLicenciaConductor());
                

//                 cs.execute();
//                 int filasAfectadas = cs.getUpdateCount();
//                 return filasAfectadas > 0;
           
//         }
//     }



//  public boolean eliminarConductor(int codConductor, String ciudad) throws SQLException {
//         String sql;
//         switch (ciudad.toLowerCase()) {
//             case "ibarra":
//                 sql = "DELETE FROM Conductor_Ibarra WHERE cod_conductor = ?";
//                 break;
//             case "quito":
//                 sql = "DELETE FROM [VLADIMIRJON].[Terminal_Quito].[dbo].[Conductor_Quito] WHERE cod_conductor = ?";
//                 break;
//             default:
//                 throw new IllegalArgumentException("Ciudad no reconocida: " + ciudad);
//         }

//         try (Connection cn = ConexionBD.conectar();
//              PreparedStatement ps = cn.prepareStatement(sql)) {

//             ps.setInt(1, codConductor);
//             int filasAfectadas = ps.executeUpdate();
//             return filasAfectadas > 0;
//         }
//     }
// }


