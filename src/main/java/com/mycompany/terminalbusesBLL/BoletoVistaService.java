package com.mycompany.terminalbusesBLL;

import java.util.List;

import com.mycompany.terminalbusesDAL.BoletoVista;
import com.mycompany.terminalbusesDAL.BoletoVistaDAO;

public class BoletoVistaService {

    private BoletoVistaDAO dao;

    public BoletoVistaService() {
        dao = new BoletoVistaDAO();
    }

    /**
     * Devuelve la lista de boletos filtrada según la ciudad del usuario.
     * @param ciudadUsuario "quito" o "ibarra"
     * @return lista de BoletoVista
     */
    public List<BoletoVista> obtenerBoletosPorUsuario(String ciudadUsuario) {
        return dao.obtenerTodosLosBoletos(ciudadUsuario);
    }
}
