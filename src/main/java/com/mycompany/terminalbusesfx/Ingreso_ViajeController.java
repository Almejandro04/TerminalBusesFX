package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.RutaVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import com.mycompany.terminalbusesBLL.ViajeProcedimientoService;
import com.mycompany.terminalbusesDAL.ConductorVista;
import com.mycompany.terminalbusesDAL.RutaVista;
import com.mycompany.terminalbusesDAL.VehiculoVista;
import com.mycompany.terminalbusesDAL.ViajeVista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Ingreso_ViajeController implements Initializable {

    // Controles
    @FXML private ComboBox<ConductorVista> cbConductor;
    @FXML private ComboBox<VehiculoVista>  cbVehiculo;
    @FXML private ComboBox<RutaVista>      cbRuta;
    @FXML private DatePicker               dpFechaViaje;
    @FXML private Spinner<Integer>         spHora;
    @FXML private Spinner<Integer>         spMin;
    @FXML private TextField                tfCodigo;   // cod_viaje (REQUERIDO por el SP)
    @FXML private Button                   btnGuardar, btnCancelar;

    // Estado
    private String ciudadUsuario;        // "QUITO" o "IBARRA"
    private boolean saved = false;

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad == null ? "" : ciudad.trim();

        // Poblar combos filtrados por ciudad
        cbConductor.setItems(FXCollections.observableArrayList(
            new ConductorVistaService().obtenerConductoresPorUsuario(ciudadUsuario)
        ));
        cbVehiculo.setItems(FXCollections.observableArrayList(
            new VehiculoVistaService().obtenerVehiculosPorUsuario(ciudadUsuario)
        ));
        cbRuta.setItems(FXCollections.observableArrayList(
            new RutaVistaService().obtenerRutasPorUsuario(ciudadUsuario)
        ));

        // Render amigable
        configurarRenderCombos();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Spinners 24h
        spHora.setValueFactory(new IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        spMin .setValueFactory(new IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        // Fecha hoy por defecto
        dpFechaViaje.setValue(LocalDate.now());

        // tfCodigo: solo números (el SP InsertarViaje exige @cod_viaje)
        tfCodigo.setTextFormatter(new TextFormatter<>(c -> {
            String s = c.getControlNewText();
            return s.matches("\\d{0,9}") ? c : null;
        }));
    }

    private void configurarRenderCombos() {
        // Conductor -> "Nombre Apellido (Cédula)"
        cbConductor.setConverter(new StringConverter<ConductorVista>() {
            @Override public String toString(ConductorVista c) {
                return (c == null) ? "" : c.getNombreConductor() + " " + c.getApellidoConductor()
                        + " (" + c.getCedulaConductor() + ")";
            }
            @Override public ConductorVista fromString(String s) { return null; }
        });

        // Vehículo -> solo placa
        cbVehiculo.setConverter(new StringConverter<VehiculoVista>() {
            @Override public String toString(VehiculoVista v) {
                return (v == null) ? "" : v.getPlacaVehiculo();
            }
            @Override public VehiculoVista fromString(String s) { return null; }
        });

        // Ruta -> "#<codRuta> • <destino> • $<precio>"
        cbRuta.setConverter(new StringConverter<RutaVista>() {
            @Override public String toString(RutaVista r) {
                if (r == null) return "";
                String precio = r.getPrecio() == null ? "-" : r.getPrecio().toString();
                return "#" + r.getCodRuta() + " • " + r.getCiudadDestino() + " • $" + precio;
            }
            @Override public RutaVista fromString(String s) { return null; }
        });
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        // Validaciones mínimas
        if (cbConductor.getValue() == null || cbVehiculo.getValue() == null ||
            cbRuta.getValue() == null || dpFechaViaje.getValue() == null) {
            alert(Alert.AlertType.WARNING, "Complete Conductor, Bus, Ruta y Fecha.");
            return;
        }

        // cod_viaje requerido por el SP
        int codViaje;
        String codStr = safe(tfCodigo.getText());
        if (codStr.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Ingrese el código de viaje.");
            return;
        }
        try {
            codViaje = Integer.parseInt(codStr);
            if (codViaje <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            alert(Alert.AlertType.WARNING, "El código de viaje debe ser un entero positivo.");
            return;
        }

        // Hora/Fecha
        LocalTime horaSeleccionada = LocalTime.of(spHora.getValue(), spMin.getValue());
        LocalDate fechaSeleccionada = dpFechaViaje.getValue();

        // cod_terminal según ciudad
        int codTerminal;
        String ciudad = (ciudadUsuario == null) ? "" : ciudadUsuario.toLowerCase();
        switch (ciudad) {
            case "quito":  codTerminal = 1; break;
            case "ibarra": codTerminal = 2; break;
            default:
                alert(Alert.AlertType.ERROR, "Ciudad de usuario desconocida: " + ciudadUsuario);
                return;
        }

        // Armar ViajeVista con el esquema que usa el DAO/SP:
        // cod_terminal, cod_viaje, placa, cod_ruta, cod_conductor, fecha_viaje, hora_viaje
        ViajeVista v = new ViajeVista(
            codTerminal,
            codViaje,
            cbVehiculo.getValue().getPlacaVehiculo(),
            cbRuta.getValue().getCodRuta(),
            cbConductor.getValue().getCodConductor(),
            fechaSeleccionada,
            horaSeleccionada
        );

        // Llamar al servicio (retorna boolean)
        try {
            boolean ok = new ViajeProcedimientoService().crearViaje(v);
            if (!ok) {
                alert(Alert.AlertType.ERROR, "No se pudo crear el viaje.");
                return;
            }
            saved = true;
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        } catch (Exception ex) {
            ex.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error al crear viaje:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    /* Helpers */
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static void alert(Alert.AlertType t, String m) { new Alert(t, m).showAndWait(); }
}
