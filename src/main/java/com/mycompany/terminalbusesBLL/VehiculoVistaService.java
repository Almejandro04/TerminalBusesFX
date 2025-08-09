/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;

/**
 *
 * @author Jorge
 */
import com.mycompany.terminalbusesDAL.VehiculoVista;
import com.mycompany.terminalbusesDAL.VehiculoVistaDAO;
import java.util.List;

public class VehiculoVistaService {
    
    private VehiculoVistaDAO dao;

    public VehiculoVistaService() {
        dao = new VehiculoVistaDAO();
    }

    // Método para obtener todos los vehículos desde la vista
//    public List<VehiculoVista> obtenerTodosLosVehiculos() {
//        return dao.obtenerTodosLosVehiculos();
//    }
    
    public List<VehiculoVista> obtenerVehiculosPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodosLosVehiculos(ciudadUsuario);
    }
    // Aquí puedes agregar más lógica de negocio, validaciones, filtros, etc.
}
