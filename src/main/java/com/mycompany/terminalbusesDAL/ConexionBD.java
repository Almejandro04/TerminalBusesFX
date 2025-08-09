/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesDAL;

/**
 *
 * @author Jorge
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
  //  private static final String URL = "jdbc:sqlserver://ASUS-8KR2UI2;databaseName=Terminal_Ibarra";
    private static final String URL = "jdbc:sqlserver://ASUS-8KR2UI2;"
      + "databaseName=Terminal_Ibarra;"
      + "encrypt=true;"
      + "trustServerCertificate=true";  // confía en el certificado sin validarlo

    private static final String USER = "sa";
    private static final String PASSWORD = "admin";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
