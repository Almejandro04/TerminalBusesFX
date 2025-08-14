package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.RutaProcedimientoService;
import com.mycompany.terminalbusesDAL.RutaVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class Ingreso_RutaController implements Initializable {

    // --- Controles ---
    @FXML private TextField tfCodigo;   // cod_ruta (REQUERIDO por el SP)
    @FXML private TextField tfDestino;  // ciudad_destino
    @FXML private TextField tfPrecio;   // precio (decimal)
    @FXML private Button    btnGuardar, btnCancelar;

    // --- Estado ---
    private String ciudadUsuario;   // "QUITO" o "IBARRA"
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad == null ? "" : ciudad.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo decimales con hasta 2 decimales. Permitimos coma o punto para escribir;
        // luego convertimos coma -> punto antes de parsear.
        tfPrecio.setTextFormatter(new TextFormatter<>(change -> {
            String n = change.getControlNewText();
            return n.matches("\\d{0,6}([\\.,]\\d{0,2})?") ? change : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        String codStr   = safe(tfCodigo.getText());
        String destino  = safe(tfDestino.getText());
        String precioStr= safe(tfPrecio.getText());

        // Validaciones
        if (codStr.isEmpty() || destino.isEmpty() || precioStr.isEmpty()) {
            warn("Complete Código, Destino y Precio.");
            return;
        }

        int codRuta;
        try {
            codRuta = Integer.parseInt(codStr);
            if (codRuta <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Código de ruta inválido. Debe ser un entero positivo.");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr.replace(',', '.'));
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Precio inválido. Use un número positivo (ej. 5.50).");
            return;
        }

        // cod_terminal por ciudad
        int codTerminal;
        String c = ciudadUsuario.toLowerCase();
        if ("quito".equals(c))       codTerminal = 1;
        else if ("ibarra".equals(c)) codTerminal = 2;
        else {
            error("Ciudad de usuario desconocida: " + ciudadUsuario);
            return;
        }

        // DTO y llamada al service
        RutaVista ruta = new RutaVista(codTerminal, codRuta, destino, precio);

        try {
            boolean ok = new RutaProcedimientoService().crearRuta(ruta);
            if (!ok) {
                error("No se pudo crear la ruta.");
                return;
            }
            saved = true;
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al crear ruta:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    // helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String msg) { new Alert(Alert.AlertType.WARNING, msg).showAndWait(); }
    private static void error(String msg){ new Alert(Alert.AlertType.ERROR,   msg).showAndWait(); }
}
