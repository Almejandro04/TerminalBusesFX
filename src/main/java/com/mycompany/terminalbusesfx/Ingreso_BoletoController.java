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

public class Ingreso_BoletoController implements Initializable {

    // UI
    @FXML private ComboBox<ViajeVista>     cbViaje;
    @FXML private ComboBox<PasajeroVista>  cbPasajero;
    @FXML private TextField                tfAsiento;
    @FXML private Button                   btnGuardar;
    @FXML private Button                   btnCancelar;

    // Estado
    private String ciudadUsuario;   // "QUITO" o "IBARRA"
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;

        // Poblamos combos filtrando por la ciudad/terminal del usuario
        cbViaje.setItems(FXCollections.observableArrayList(
            new ViajeVistaService().obtenerViajesPorUsuario(ciudadUsuario)
        ));
        cbPasajero.setItems(FXCollections.observableArrayList(
            new PasajeroVistaService().obtenerPasajerosPorUsuario()
        ));

        configurarRenderCombos();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo números en asiento
        tfAsiento.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,3}") ? c : null; // hasta 3 dígitos; ajusta si quieres
        }));
    }

    private void configurarRenderCombos() {
        // Viaje: "#100 • 2025-08-15 08:00 • PQT-1001"
        DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm");
        cbViaje.setConverter(new StringConverter<ViajeVista>() {
            @Override public String toString(ViajeVista v) {
                if (v == null) return "";
                String fecha = (v.getFechaViaje() != null) ? v.getFechaViaje().toString() : "-";
                String hora  = (v.getHoraViaje()  != null) ? v.getHoraViaje().format(hf)       : "--:--";
                String placa = (v.getPlaca() != null) ? v.getPlaca() : "-";
                return "#" + v.getCodViaje() + " • " + fecha + " " + hora + " • " + placa;
            }
            @Override public ViajeVista fromString(String s) { return null; }
        });

        // Pasajero: "0102030405 • Ana Torres"
        cbPasajero.setConverter(new StringConverter<PasajeroVista>() {
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

        // cod_terminal por ciudad (switch clásico compatible con Java 8/11)
        int codTerminal;
        String c = ciudadUsuario == null ? "" : ciudadUsuario.trim().toLowerCase();
        switch (c) {
            case "quito":
                codTerminal = 1;
                break;
            case "ibarra":
                codTerminal = 2;
                break;
            default:
                error("Ciudad desconocida: " + ciudadUsuario);
                return;
        }

        // Construir DTO y llamar al service
        BoletoVista nuevo = new BoletoVista(
            codTerminal,
            viajeSel.getCodViaje(),
            pasSel.getCedulaPasajero(),
            asiento
        );

        try {
            boolean ok = new BoletoProcedimientoService().insertarBoleto(nuevo);
            if (!ok) {
                error("No se pudo registrar el boleto.");
                return;
            }
            saved = true;
            cerrar(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al registrar boleto:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        cerrar(e);
    }

    public boolean isSaved() { return saved; }

    // Helpers
    private static void cerrar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
}
