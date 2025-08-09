/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.terminalbusesBLL;

/**
 *
 * @author Jorge
 */
import com.mycompany.terminalbusesDAL.TerminalVista;
import com.mycompany.terminalbusesDAL.TerminalVistaDAO;
import java.util.List;

public class TerminalVistaService {
    
    private TerminalVistaDAO dao;

    public TerminalVistaService() {
        dao = new TerminalVistaDAO();
    }

    // Método para obtener todos los terminales desde la vista
//    public List<TerminalVista> obtenerTodosLosTerminales() {
//        return dao.obtenerTodos();
//    }
    
    public List<TerminalVista> obtenerTerminalesPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodosLosTerminales(ciudadUsuario);
    }
    // Aquí puedes agregar más lógica de negocio, validaciones, filtros, etc.
}
