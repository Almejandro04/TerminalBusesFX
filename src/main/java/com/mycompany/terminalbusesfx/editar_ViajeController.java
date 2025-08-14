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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class editar_ViajeController implements Initializable {

    @FXML private TextField                tfCodigo;        // cod_viaje (solo lectura)
    @FXML private ComboBox<ConductorVista> cbConductor;
    @FXML private ComboBox<VehiculoVista>  cbVehiculo;      // placa
    @FXML private ComboBox<RutaVista>      cbRuta;
    @FXML private DatePicker               dpFechaViaje;
    @FXML private Spinner<Integer>         spHora, spMin;
    @FXML private Button                   btnGuardar, btnCancelar;

    private final ViajeProcedimientoService service = new ViajeProcedimientoService();

    private String    ciudadUsuario;   // "quito" | "ibarra"
    private ViajeVista viajeActual;    // registro a editar
    private boolean   saved = false;

    /* ======================= Ciclo de vida ======================= */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Spinners de hora/minuto (compatibles con Java 8/11)
        spHora.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour())
        );
        spMin.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute())
        );

        // Render de combos
        cbConductor.setConverter(new StringConverter<ConductorVista>() {
            @Override public String toString(ConductorVista c) {
                return c == null ? "" : c.getNombreConductor() + " " + c.getApellidoConductor();
            }
            @Override public ConductorVista fromString(String s) { return null; }
        });
        cbVehiculo.setConverter(new StringConverter<VehiculoVista>() {
            @Override public String toString(VehiculoVista v) {
                return v == null ? "" : v.getPlacaVehiculo();
            }
            @Override public VehiculoVista fromString(String s) { return null; }
        });
        cbRuta.setConverter(new StringConverter<RutaVista>() {
            @Override public String toString(RutaVista r) {
                if (r == null) return "";
                String precio = r.getPrecio() == null ? "-" : r.getPrecio().toString();
                return "#" + r.getCodRuta() + " • " + r.getCiudadDestino() + " • $" + precio;
            }
            @Override public RutaVista fromString(String s) { return null; }
        });

        // Código solo lectura
        tfCodigo.setEditable(false);
        tfCodigo.setDisable(true);
    }

    /** Inyecta ciudad y carga listas para esa sede */
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;

        cbConductor.setItems(FXCollections.observableArrayList(
            new ConductorVistaService().obtenerConductoresPorUsuario(ciudadUsuario)
        ));
        cbVehiculo.setItems(FXCollections.observableArrayList(
            new VehiculoVistaService().obtenerVehiculosPorUsuario(ciudadUsuario)
        ));
        cbRuta.setItems(FXCollections.observableArrayList(
            new RutaVistaService().obtenerRutasPorUsuario(ciudadUsuario)
        ));
    }

    /** Precarga datos del viaje seleccionado */
    public void setViaje(ViajeVista v) {
        this.viajeActual = v;

        tfCodigo.setText(String.valueOf(v.getCodViaje()));
        dpFechaViaje.setValue(v.getFechaViaje());
        if (v.getHoraViaje() != null) {
            spHora.getValueFactory().setValue(v.getHoraViaje().getHour());
            spMin .getValueFactory().setValue(v.getHoraViaje().getMinute());
        }

        // Seleccionar conductor por cod_conductor
        cbConductor.getSelectionModel().select(
            cbConductor.getItems().stream()
                .filter(c -> c.getCodConductor() == v.getCodConductor())
                .findFirst().orElse(null)
        );

        // Seleccionar vehículo por PLACA
        cbVehiculo.getSelectionModel().select(
            cbVehiculo.getItems().stream()
                .filter(b -> b.getPlacaVehiculo().equalsIgnoreCase(v.getPlaca()))
                .findFirst().orElse(null)
        );

        // Seleccionar ruta por cod_ruta
        cbRuta.getSelectionModel().select(
            cbRuta.getItems().stream()
                .filter(r -> r.getCodRuta() == v.getCodRuta())
                .findFirst().orElse(null)
        );
    }

    /* ======================= Acciones ======================= */

    @FXML
    private void handleGuardar(ActionEvent e) {
        // Validación mínima
        if (cbConductor.getValue()==null || cbVehiculo.getValue()==null ||
            cbRuta.getValue()==null || dpFechaViaje.getValue()==null) {
            alert(Alert.AlertType.WARNING, "Complete Conductor, Bus, Ruta y Fecha.");
            return;
        }

        LocalDate fecha = dpFechaViaje.getValue();
        Integer h = spHora.getValue();
        Integer m = spMin.getValue();
        if (h == null || m == null) {
            alert(Alert.AlertType.WARNING, "Seleccione hora y minuto.");
            return;
        }
        LocalTime hora = LocalTime.of(h, m);

        // Java 8/11: switch clásico
        int codTerminal;
        switch (ciudadUsuario == null ? "" : ciudadUsuario.toLowerCase()) {
            case "quito":
                codTerminal = 1;
                break;
            case "ibarra":
                codTerminal = 2;
                break;
            default:
                alert(Alert.AlertType.ERROR, "Ciudad desconocida: " + ciudadUsuario);
                return;
        }

        // Actualiza el DTO existente (mantiene cod_viaje)
        viajeActual.setCodConductor(cbConductor.getValue().getCodConductor());
        viajeActual.setPlaca(cbVehiculo.getValue().getPlacaVehiculo()); // SP usa placa
        viajeActual.setCodRuta(cbRuta.getValue().getCodRuta());
        viajeActual.setCodTerminal(codTerminal);
        viajeActual.setFechaViaje(fecha);
        viajeActual.setHoraViaje(hora);

        try {
            boolean ok = service.actualizarViaje(viajeActual);
            if (!ok) {
                alert(Alert.AlertType.ERROR, "No se pudo actualizar el viaje.");
                return;
            }
            saved = true;
            ((Stage) btnGuardar.getScene().getWindow()).close();
        } catch (Exception ex) {
            ex.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error al actualizar viaje:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }

    /* ======================= Helpers ======================= */
    private static void alert(Alert.AlertType t, String m) {
        new Alert(t, m).showAndWait();
    }
}
