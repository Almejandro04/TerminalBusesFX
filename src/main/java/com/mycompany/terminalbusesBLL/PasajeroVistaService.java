/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;

/**
 *
 * @author Jorge
 */

import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.PasajeroVistaDAO;
import java.util.List;


public class PasajeroVistaService {
    private PasajeroVistaDAO dao;

    public  PasajeroVistaService() {
        dao = new PasajeroVistaDAO();
    }

    // Método para obtener todos los terminales desde la vista
//    public List<PasajeroVista> obtenerTodosLosPasajeros() {
//        return dao.obtenerTodosLosPasajeros();
//    }   
    public List<PasajeroVista> obtenerPasajerosPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodosLosPasajeros(ciudadUsuario);
    }
    
        public List<PasajeroVista> obtenerPasajerosPorViaje(String ciudadUsuario, int codViaje) {
        return dao.obtenerPasajerosPorViaje(ciudadUsuario, codViaje);
    }
    
}
