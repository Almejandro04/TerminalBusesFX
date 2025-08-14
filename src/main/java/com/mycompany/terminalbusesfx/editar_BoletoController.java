package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.BoletoProcedimientoService;
import com.mycompany.terminalbusesBLL.PasajeroVistaService;
import com.mycompany.terminalbusesBLL.ViajeVistaService;
import com.mycompany.terminalbusesDAL.BoletoVista;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.ViajeVista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class editar_BoletoController implements Initializable {

    // UI
    @FXML private ComboBox<ViajeVista> cbViaje;
    @FXML private ComboBox<PasajeroVista> cbPasajero;
    @FXML private TextField tfAsiento;
    @FXML private Button btnGuardar, btnCancelar;

    // Estado
    private String ciudadUsuario;   // "QUITO" o "IBARRA"
    private BoletoVista boletoActual;
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;

        // Poblar combos filtrados por la ciudad
        cbViaje.setItems(FXCollections.observableArrayList(
            new ViajeVistaService().obtenerViajesPorUsuario(ciudadUsuario)
        ));
        cbPasajero.setItems(FXCollections.observableArrayList(
            new PasajeroVistaService().obtenerPasajerosPorUsuario()
        ));

        configurarRenderCombos();
    }

    /** Carga los valores actuales en el formulario */
    public void setBoleto(BoletoVista boleto) {
        this.boletoActual = boleto;

        // Seleccionar viaje por cod_viaje
        if (cbViaje.getItems() != null) {
            cbViaje.getSelectionModel().select(
                cbViaje.getItems().stream()
                       .filter(v -> v.getCodViaje() == boleto.getCodViaje())
                       .findFirst().orElse(null)
            );
        }

        // Seleccionar pasajero por cédula
        if (cbPasajero.getItems() != null) {
            cbPasajero.getSelectionModel().select(
                cbPasajero.getItems().stream()
                          .filter(p -> p.getCedulaPasajero().equals(boleto.getCedulaPasajero()))
                          .findFirst().orElse(null)
            );
        }

        tfAsiento.setText(String.valueOf(boleto.getNumAsiento()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asiento: solo enteros (hasta 3 dígitos; ajusta si quieres)
        tfAsiento.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,3}") ? c : null;
        }));
        if (cbViaje != null) {
    cbViaje.setMouseTransparent(true);
    cbViaje.setFocusTraversable(false);
}
if (cbPasajero != null) {
    cbPasajero.setMouseTransparent(true);
    cbPasajero.setFocusTraversable(false);
}
    }

    private void configurarRenderCombos() {
        DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm");

        // Viaje: "#100 • 2025-08-15 08:00 • PQT-1001"
        cbViaje.setConverter(new StringConverter<>() {
            @Override public String toString(ViajeVista v) {
                if (v == null) return "";
                String fecha = v.getFechaViaje() != null ? v.getFechaViaje().toString() : "-";
                String hora  = v.getHoraViaje()  != null ? v.getHoraViaje().format(hf)  : "--:--";
                String placa = v.getPlaca() != null ? v.getPlaca() : "-";
                return "#" + v.getCodViaje() + " • " + fecha + " " + hora + " • " + placa;
            }
            @Override public ViajeVista fromString(String s) { return null; }
        });

        // Pasajero: "0102030405 • Ana Torres"
        cbPasajero.setConverter(new StringConverter<>() {
            @Override public String toString(PasajeroVista p) {
                return (p == null) ? "" : p.getCedulaPasajero() + " • " +
                        p.getNombrePasajero() + " " + p.getApellidoPasajero();
            }
            @Override public PasajeroVista fromString(String s) { return null; }
        });
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        // Validaciones
        ViajeVista viajeSel = cbViaje.getValue();
        PasajeroVista pasSel = cbPasajero.getValue();
        String asientoStr = tfAsiento.getText() == null ? "" : tfAsiento.getText().trim();

        if (viajeSel == null || pasSel == null || asientoStr.isEmpty()) {
            warn("Seleccione el viaje, el pasajero y escriba el número de asiento.");
            return;
        }

        int asiento;
        try {
            asiento = Integer.parseInt(asientoStr);
            if (asiento <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("El número de asiento debe ser un entero positivo.");
            return;
        }

        // Construir boleto editado
        BoletoVista actualizado = new BoletoVista(
            boletoActual.getCodTerminal(),        // Mantiene terminal original
            viajeSel.getCodViaje(),
            pasSel.getCedulaPasajero(),
            asiento
        );

        try {
            boolean ok = new BoletoProcedimientoService().actualizarBoleto(actualizado);
            if (!ok) {
                error("No se pudo actualizar el boleto.");
                return;
            }
            saved = true;
            ((Stage)((Node) e.getSource()).getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al actualizar boleto:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)((Node) e.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    // helpers UI
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
}
