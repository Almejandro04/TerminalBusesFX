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

public class editar_RutaController implements Initializable {

    @FXML private TextField tfCodigo;   // cod_ruta (solo lectura)
    @FXML private TextField tfDestino;
    @FXML private TextField tfPrecio;
    @FXML private Button    btnGuardar, btnCancelar;

    private RutaVista rutaActual;   // ruta seleccionada
    private boolean saved = false;

    /** Precarga datos del registro seleccionado */
    public void setRuta(RutaVista r) {
        this.rutaActual = r;

        tfCodigo.setText(String.valueOf(r.getCodRuta()));
        tfCodigo.setEditable(false);
        tfCodigo.setDisable(true);

        tfDestino.setText(r.getCiudadDestino());
        tfPrecio.setText(r.getPrecio() == null ? "" : String.valueOf(r.getPrecio()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Precio: solo números y un punto decimal (hasta 2 decimales)
        tfPrecio.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,6}(\\.\\d{0,2})?") ? c : null;
        }));
        

        
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        if (rutaActual == null) {
            error("No hay ruta para editar.");
            return;
        }

        String destino = safe(tfDestino.getText());
        String pStr    = safe(tfPrecio.getText());

        if (destino.isEmpty() || pStr.isEmpty()) {
            warn("Complete Destino y Precio.");
            return;
        }

        Double precio;
        try {
            precio = Double.valueOf(pStr);
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Precio inválido. Use un número positivo (ej. 5.50).");
            return;
        }

        // Mantener PK (cod_terminal, cod_ruta) y actualizar destino/precio
        RutaVista actualizado = new RutaVista(
            rutaActual.getCodTerminal(),
            rutaActual.getCodRuta(),
            destino,
            precio
        );

        try {
            boolean ok = new RutaProcedimientoService().actualizarRuta(actualizado);
            if (!ok) {
                error("No se pudo actualizar la ruta.");
                return;
            }
            saved = true;
            cerrar(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al actualizar ruta:\n" + ex.getMessage());
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
