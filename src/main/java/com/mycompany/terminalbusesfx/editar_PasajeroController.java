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

public class editar_PasajeroController implements Initializable {

    @FXML private TextField tfCedula;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfTelefono;
    @FXML private TextField tfCorreo;
    @FXML private Button    btnGuardar;
    @FXML private Button    btnCancelar;

    private PasajeroVista pasajeroActual;  // registro original
    private boolean saved = false;

    /** Precarga los datos del pasajero seleccionado */
    public void setPasajero(PasajeroVista p) {
        this.pasajeroActual = p;
        tfCedula.setText(p.getCedulaPasajero());
        tfNombre.setText(p.getNombrePasajero());
        tfApellido.setText(p.getApellidoPasajero());
        tfTelefono.setText(p.getTelefonoPasajero());
        tfCorreo.setText(p.getCorreoPasajero());

        // Bloquea cédula (PK)
        tfCedula.setEditable(false);
        tfCedula.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cédula y teléfono: solo números, 10 máx (ajusta si tu regla es otra)
        tfCedula.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
        tfTelefono.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,10}") ? c : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        if (pasajeroActual == null) {
            error("No hay pasajero para editar.");
            return;
        }

        String cedula   = safe(tfCedula.getText());
        String nombre   = safe(tfNombre.getText());
        String apellido = safe(tfApellido.getText());
        String telefono = safe(tfTelefono.getText());
        String correo   = safe(tfCorreo.getText());

        // Validaciones básicas
        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()) {
            warn("Complete todos los campos.");
            return;
        }
        if (!cedula.matches("\\d{10}")) {
            warn("La cédula debe tener 10 dígitos.");
            return;
        }
        if (!telefono.matches("\\d{7,10}")) { // ajusta el rango si hace falta
            warn("Teléfono inválido (solo números, 7 a 10 dígitos).");
            return;
        }
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            warn("Correo inválido.");
            return;
        }

        // Construye objeto con las ediciones (la PK cédula NO cambia)
        PasajeroVista actualizado = new PasajeroVista(
            cedula, nombre, apellido, telefono, correo
        );

        try {
            boolean ok = new PasajeroProcedimientoService().actualizarPasajero(actualizado);
            if (!ok) {
                error("No se pudo actualizar el pasajero.");
                return;
            }
            saved = true;
            cerrar(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al actualizar pasajero:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        cerrar(e);
    }

    public boolean isSaved() { return saved; }

    // helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
    private static void cerrar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    public void setCiudadUsuario(String currentUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCiudadUsuario'");
    }
}
