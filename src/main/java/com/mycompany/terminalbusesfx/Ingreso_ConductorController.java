package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.ConductorProcedimientoService;
import com.mycompany.terminalbusesDAL.ConductorVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class Ingreso_ConductorController implements Initializable {

    @FXML private TextField tfCodigo;    // requerido por el SP (no lo genera BD)
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfLicencia;  // cédula del conductor
    @FXML private Button    btnGuardar;
    @FXML private Button    btnCancelar;

    private String ciudadUsuario;   // "QUITO" o "IBARRA"
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad == null ? "" : ciudad.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo dígitos en código y cédula
        tfCodigo.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
        tfLicencia.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        String codStr   = safe(tfCodigo.getText());
        String nombre   = safe(tfNombre.getText());
        String apellido = safe(tfApellido.getText());
        String cedula   = safe(tfLicencia.getText());

        // Validaciones
        if (codStr.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty()) {
            warn("Complete Código, Nombre, Apellido y Cédula.");
            return;
        }
        int codConductor;
        try {
            codConductor = Integer.parseInt(codStr);
            if (codConductor <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("El código debe ser un entero positivo.");
            return;
        }
        if (!cedula.matches("\\d{10}")) {
            warn("La cédula debe tener 10 dígitos.");
            return;
        }

        // cod_terminal por ciudad
        int codTerminal;
        String c = ciudadUsuario == null ? "" : ciudadUsuario.toLowerCase();
        if ("quito".equals(c))       codTerminal = 1;
        else if ("ibarra".equals(c)) codTerminal = 2;
        else {
            error("Ciudad de usuario desconocida: " + ciudadUsuario);
            return;
        }

        // DTO: (codConductor, codTerminal, cedula, nombre, apellido)
        ConductorVista conductor = new ConductorVista(
            codConductor,
            codTerminal,
            cedula,
            nombre,
            apellido
        );

        try {
            boolean ok = new ConductorProcedimientoService().crearConductor(conductor);
            if (!ok) {
                error("No se pudo ingresar el conductor.");
                return;
            }
            saved = true;
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al crear conductor:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    // helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
}
