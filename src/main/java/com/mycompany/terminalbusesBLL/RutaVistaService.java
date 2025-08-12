package com.mycompany.terminalbusesBLL;

import java.util.List;

import com.mycompany.terminalbusesDAL.RutaVista;
import com.mycompany.terminalbusesDAL.RutaVistaDAO;

public class RutaVistaService {

    private final RutaVistaDAO dao;

    // Constructor por defecto
    public RutaVistaService() {
        this(new RutaVistaDAO());
    }

    // Inyección para tests
    public RutaVistaService(RutaVistaDAO dao) {
        this.dao = dao;
    }

    /** Obtiene todas las rutas desde la vista correspondiente según la ciudad (ibarra/quito). */
    public List<RutaVista> obtenerTodasLasRutas(String ciudad) {
        return dao.obtenerTodasLasRutas(ciudad);
    }
}
