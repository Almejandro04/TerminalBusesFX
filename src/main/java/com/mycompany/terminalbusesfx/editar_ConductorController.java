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

public class editar_ConductorController implements Initializable {

    @FXML private TextField tfCodigo;   // solo lectura
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfLicencia; // cédula
    @FXML private Button    btnGuardar, btnCancelar;

    private ConductorVista conductorActual; // registro original
    private boolean saved = false;

    /** Precarga datos en el formulario */
    public void setConductor(ConductorVista c) {
        this.conductorActual = c;

        if (tfCodigo != null) {
            tfCodigo.setText(String.valueOf(c.getCodConductor()));
            tfCodigo.setEditable(false);
            tfCodigo.setDisable(true);
        }
        tfNombre.setText(c.getNombreConductor());
        tfApellido.setText(c.getApellidoConductor());
        tfLicencia.setText(c.getCedulaConductor());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cédula: solo dígitos, 10 máx (ajusta si tu regla es otra)
        tfLicencia.setTextFormatter(new TextFormatter<>(chg -> {
            String s = chg.getControlNewText();
            return s.matches("\\d{0,10}") ? chg : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        if (conductorActual == null) {
            error("No hay conductor para editar.");
            return;
        }

        String nombre   = safe(tfNombre.getText());
        String apellido = safe(tfApellido.getText());
        String cedula   = safe(tfLicencia.getText());

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty()) {
            warn("Complete Nombre, Apellido y Cédula.");
            return;
        }
        if (!cedula.matches("\\d{10}")) {
            warn("La cédula debe tener 10 dígitos.");
            return;
        }

        // Mantener PK lógica (cod_conductor) y terminal del original
        int codConductor = conductorActual.getCodConductor();
        int codTerminal  = conductorActual.getCodTerminal();

        // Construimos el DTO con los nuevos datos personales.
        // Nota: el SP ActualizarConductor toma nombre/apellido desde el objeto
        // y la cédula NUEVA como parámetro aparte.
        ConductorVista paraActualizar = new ConductorVista(
            codConductor,
            codTerminal,
            conductorActual.getCedulaConductor(), // no lo usa el SP para cambiar cédula
            nombre,
            apellido
        );

        // Si la cédula no cambió, pasamos null para que no se modifique
        String cedulaNueva = cedula.equals(conductorActual.getCedulaConductor())
                           ? null
                           : cedula;

        try {
            boolean ok = new ConductorProcedimientoService()
                    .actualizarConductor(paraActualizar, /*codTerminalNuevo*/ null, /*cedulaNueva*/ cedulaNueva);

            if (!ok) {
                error("No se pudo actualizar el conductor.");
                return;
            }
            saved = true;
            cerrar(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al actualizar conductor:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        cerrar(e);
    }

    public boolean isSaved() { return saved; }

    // Helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
    private static void cerrar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    private String ciudadUsuario;

// Setter que te llama el padre (DetalleViajeFINALController)
public void setCiudadUsuario(String ciudad) {
    this.ciudadUsuario = (ciudad == null ? "" : ciudad.trim());
    // Si necesitas refrescar combos/valores dependientes de la ciudad, hazlo aquí.
}

// (opcional) Getter si lo usas en otro lado
public String getCiudadUsuario() { 
    return ciudadUsuario; 
}
}
