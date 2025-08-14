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

public class editar_BusController implements Initializable {

    @FXML private TextField tfPlaca;
    @FXML private TextField tfCapacidad;
    @FXML private Button    btnGuardar, btnCancelar;

    private VehiculoVista busActual;   // bus a editar (PK: cod_terminal, placa)
    private boolean saved = false;

    /** Precarga datos en el formulario */
    public void setBus(VehiculoVista bus) {
        this.busActual = bus;
        tfPlaca.setText(bus.getPlacaVehiculo());
        tfCapacidad.setText(String.valueOf(bus.getCapacidadVehiculo()));
        // La PK (placa) no se puede cambiar con el SP ActualizarBus
        tfPlaca.setEditable(false);
        tfPlaca.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo números (hasta 4 dígitos por si acaso)
        tfCapacidad.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,4}") ? c : null;
        }));
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        if (busActual == null) {
            error("No hay bus para editar.");
            return;
        }

        String capStr = safe(tfCapacidad.getText());
        if (capStr.isEmpty()) {
            warn("Ingrese la capacidad.");
            return;
        }

        int nuevaCap;
        try {
            nuevaCap = Integer.parseInt(capStr);
            if (nuevaCap <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Capacidad inválida. Debe ser un entero positivo.");
            return;
        }

        // Mantener PK (cod_terminal, placa) y actualizar solo capacidad
        VehiculoVista actualizado = new VehiculoVista(
            busActual.getCodTerminal(),
            busActual.getPlacaVehiculo(),
            nuevaCap
        );

        try {
            boolean ok = new VehiculoProcedimientoService().actualizarVehiculo(actualizado);
            if (!ok) {
                error("No se pudo actualizar el bus.");
                return;
            }
            saved = true;
            cerrar(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al actualizar bus:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        cerrar(e);
    }

    public boolean isSaved() { return saved; }

    // Helpers
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void warn(String msg) { new Alert(Alert.AlertType.WARNING, msg).showAndWait(); }
    private static void error(String msg){ new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
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
