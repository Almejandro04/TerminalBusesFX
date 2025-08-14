package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.VehiculoProcedimientoService;
import com.mycompany.terminalbusesDAL.VehiculoVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class Ingreso_BusController implements Initializable {

    @FXML private TextField tfPlaca;
    @FXML private TextField tfCapacidad;
    @FXML private Button    btnGuardar, btnCancelar;

    private String ciudadUsuario;   // "QUITO" o "IBARRA"
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = (ciudad == null) ? "" : ciudad.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo números en capacidad (hasta 3 dígitos; ajusta si quieres)
        tfCapacidad.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,3}") ? c : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        String placa  = safe(tfPlaca.getText()).toUpperCase();
        String capStr = safe(tfCapacidad.getText());

        // Validaciones mínimas
        if (placa.isEmpty() || capStr.isEmpty()) {
            warn("Complete los campos Placa y Capacidad.");
            return;
        }
        // (opcional) patrón de placa; ajusta a tu estándar
        if (!placa.matches("[A-Z0-9-]{3,10}")) {
            warn("Formato de placa inválido.");
            return;
        }
        int capacidad;
        try {
            capacidad = Integer.parseInt(capStr);
            if (capacidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Capacidad inválida. Debe ser un entero positivo.");
            return;
        }

        // cod_terminal por ciudad (null-safe)
        int codTerminal;
        String c = (ciudadUsuario == null) ? "" : ciudadUsuario.toLowerCase();
        if ("quito".equals(c)) {
            codTerminal = 1;
        } else if ("ibarra".equals(c)) {
            codTerminal = 2;
        } else {
            error("Ciudad de usuario desconocida: " + ciudadUsuario);
            return;
        }

        // DTO (codTerminal, placaVehiculo, capacidadVehiculo)
        VehiculoVista bus = new VehiculoVista(codTerminal, placa, capacidad);

        // Llamada al Service (un solo parámetro)
        try {
            boolean ok = new VehiculoProcedimientoService().crearVehiculo(bus);
            if (!ok) {
                error("No se pudo crear el bus.");
                return;
            }
            saved = true;
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al crear bus:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    // Helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
}
