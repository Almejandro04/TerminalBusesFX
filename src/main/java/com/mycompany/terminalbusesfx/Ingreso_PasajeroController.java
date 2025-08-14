package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.PasajeroProcedimientoService;
import com.mycompany.terminalbusesDAL.PasajeroVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class Ingreso_PasajeroController implements Initializable {

    @FXML private TextField tfCedula;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfTelefono;
    @FXML private TextField tfCorreo;
    @FXML private Button    btnGuardar, btnCancelar;

    private String  ciudadUsuario;   // "QUITO" o "IBARRA" (no se usa para el SP)
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad == null ? "" : ciudad.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cédula: solo dígitos, máx 10
        tfCedula.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
        // Teléfono: solo dígitos, máx 10 (se mantiene como String)
        tfTelefono.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        String cedula   = safe(tfCedula.getText());
        String nombre   = safe(tfNombre.getText());
        String apellido = safe(tfApellido.getText());
        String telefono = safe(tfTelefono.getText()); // ← String (no int)
        String correo   = safe(tfCorreo.getText());

        // Validaciones mínimas
        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
            telefono.isEmpty() || correo.isEmpty()) {
            warn("Complete todos los campos.");
            return;
        }
        if (!cedula.matches("\\d{10}")) {
            warn("La cédula debe tener 10 dígitos.");
            return;
        }
        if (!telefono.matches("\\d{7,10}")) { // ajusta el rango si lo necesitas
            warn("Teléfono inválido (solo números, 7 a 10 dígitos).");
            return;
        }
        // validación simple de correo
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            warn("Correo inválido.");
            return;
        }

        // Construir DTO (cedula, nombre, apellido, telefono, correo)
        PasajeroVista p = new PasajeroVista(cedula, nombre, apellido, telefono, correo);

        try {
            // Service actual: un solo parámetro
            boolean ok = new PasajeroProcedimientoService().crearPasajero(p);
            if (!ok) {
                error("No se pudo crear el pasajero.");
                return;
            }
            saved = true;
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al crear pasajero:\n" + ex.getMessage());
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
