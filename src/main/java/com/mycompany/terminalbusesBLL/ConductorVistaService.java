/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.terminalbusesBLL;

import com.mycompany.terminalbusesDAL.ConductorVista;
import com.mycompany.terminalbusesDAL.ConductorVistaDAO;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class ConductorVistaService {
    private ConductorVistaDAO dao;

    public ConductorVistaService() {
        dao = new ConductorVistaDAO();
    }

 
    // Nuevo: devuelve la lista según la vista remota o local
    public List<ConductorVista> obtenerConductoresPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodosLosConductores(ciudadUsuario);
    }
}
