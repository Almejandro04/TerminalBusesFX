/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;

/**
 *
 * @author Jorge
 */
import com.mycompany.terminalbusesDAL.ViajeVista;
import com.mycompany.terminalbusesDAL.ViajeVistaDAO;
import java.util.List;

public class ViajeVistaService {
    
    private ViajeVistaDAO dao;

    public ViajeVistaService() {
        dao = new ViajeVistaDAO();
    }

    //Método para obtener todos los viajes desde la vista
//    public List<ViajeVista> obtenerTodosLosViajes() {
//        return dao.obtenerTodos();
//    }

    public List<ViajeVista> obtenerViajesPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodos(ciudadUsuario);
    }
    // Aquí puedes agregar más lógica de negocio, validaciones, filtros, etc.
    
    
    
}
