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

    // --- UI ---
    @FXML private TextField                tfCodigo;        // puede NO existir en el FXML; por eso null-guard
    @FXML private ComboBox<ConductorVista> cbConductor;
    @FXML private ComboBox<VehiculoVista>  cbVehiculo;      // placa
    @FXML private ComboBox<RutaVista>      cbRuta;
    @FXML private DatePicker               dpFechaViaje;
    @FXML private Spinner<Integer>         spHora, spMin;
    @FXML private Button                   btnGuardar, btnCancelar;

    // --- Servicios / estado ---
    private final ViajeProcedimientoService service = new ViajeProcedimientoService();

    private String    ciudadUsuario;         // "quito" | "ibarra"
    private ViajeVista viajeActual;          // registro a editar
    private boolean   saved = false;

    // Buffer temporal por si setViaje llega antes de setCiudadUsuario
    private Integer   selCodConductor;
    private String    selPlaca;
    private Integer   selCodRuta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Null-guards por si el FXML no define alguno (evita NPE)
        if (spHora != null) {
            spHora.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour())
            );
        }
        if (spMin != null) {
            spMin.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute())
            );
        }
        if (dpFechaViaje != null && dpFechaViaje.getValue() == null) {
            dpFechaViaje.setValue(LocalDate.now());
        }

        // Render de combos (si existen)
        if (cbConductor != null) {
            cbConductor.setConverter(new StringConverter<ConductorVista>() {
                @Override public String toString(ConductorVista c) {
                    return c == null ? "" : c.getNombreConductor() + " " + c.getApellidoConductor();
                }
                @Override public ConductorVista fromString(String s) { return null; }
            });
        }
        if (cbVehiculo != null) {
            cbVehiculo.setConverter(new StringConverter<VehiculoVista>() {
                @Override public String toString(VehiculoVista v) {
                    return v == null ? "" : v.getPlacaVehiculo();
                }
                @Override public VehiculoVista fromString(String s) { return null; }
            });
        }
        if (cbRuta != null) {
            cbRuta.setConverter(new StringConverter<RutaVista>() {
                @Override public String toString(RutaVista r) {
                    if (r == null) return "";
                    String precio = (r.getPrecio() == null) ? "-" : r.getPrecio().toString();
                    return "#" + r.getCodRuta() + " • " + r.getCiudadDestino() + " • $" + precio;
                }
                @Override public RutaVista fromString(String s) { return null; }
            });
        }

        // Código solo lectura si existe en FXML
        if (tfCodigo != null) {
            tfCodigo.setEditable(false);
            tfCodigo.setDisable(true);
        }
    }

    /** Inyecta la ciudad y carga listas para esa sede */
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = (ciudad == null ? "" : ciudad.trim());

        // Cargar listas filtradas
        if (cbConductor != null) {
            cbConductor.setItems(FXCollections.observableArrayList(
                new ConductorVistaService().obtenerConductoresPorUsuario(ciudadUsuario)
            ));
        }
        if (cbVehiculo != null) {
            cbVehiculo.setItems(FXCollections.observableArrayList(
                new VehiculoVistaService().obtenerVehiculosPorUsuario(ciudadUsuario)
            ));
        }
        if (cbRuta != null) {
            cbRuta.setItems(FXCollections.observableArrayList(
                new RutaVistaService().obtenerRutasPorUsuario(ciudadUsuario)
            ));
        }

        // Si ya teníamos seleccionado (vía setViaje), intenta seleccionar ahora
        trySelectBufferedChoices();
    }

    /** Precarga datos del viaje seleccionado */
    public void setViaje(ViajeVista v) {
        this.viajeActual = v;

        if (tfCodigo != null) tfCodigo.setText(String.valueOf(v.getCodViaje()));
        if (dpFechaViaje != null) dpFechaViaje.setValue(v.getFechaViaje());
        if (v.getHoraViaje() != null) {
            if (spHora != null && spHora.getValueFactory() != null)
                spHora.getValueFactory().setValue(v.getHoraViaje().getHour());
            if (spMin  != null && spMin .getValueFactory() != null)
                spMin .getValueFactory().setValue(v.getHoraViaje().getMinute());
        }

        // Si aún no están cargadas las listas (setCiudadUsuario no se llamó), bufferiza
        selCodConductor = v.getCodConductor();
        selPlaca        = v.getPlaca();
        selCodRuta      = v.getCodRuta();

        // Si ya hay datos en los combos, selecciona de inmediato
        trySelectBufferedChoices();
    }

    private void trySelectBufferedChoices() {
        if (cbConductor != null && cbConductor.getItems() != null && !cbConductor.getItems().isEmpty() && selCodConductor != null) {
            cbConductor.getSelectionModel().select(
                cbConductor.getItems().stream()
                    .filter(c -> c.getCodConductor() == selCodConductor)
                    .findFirst().orElse(null)
            );
        }
        if (cbVehiculo != null && cbVehiculo.getItems() != null && !cbVehiculo.getItems().isEmpty() && selPlaca != null) {
            cbVehiculo.getSelectionModel().select(
                cbVehiculo.getItems().stream()
                    .filter(b -> b.getPlacaVehiculo().equalsIgnoreCase(selPlaca))
                    .findFirst().orElse(null)
            );
        }
        if (cbRuta != null && cbRuta.getItems() != null && !cbRuta.getItems().isEmpty() && selCodRuta != null) {
            cbRuta.getSelectionModel().select(
                cbRuta.getItems().stream()
                    .filter(r -> r.getCodRuta() == selCodRuta)
                    .findFirst().orElse(null)
            );
        }
    }

    /* ======================= Acciones ======================= */

    @FXML
    private void handleGuardar(ActionEvent e) {
        // Validación mínima
        if (cbConductor == null || cbVehiculo == null || cbRuta == null || dpFechaViaje == null ||
            cbConductor.getValue()==null || cbVehiculo.getValue()==null ||
            cbRuta.getValue()==null || dpFechaViaje.getValue()==null) {
            alert(Alert.AlertType.WARNING, "Complete Conductor, Bus, Ruta y Fecha.");
            return;
        }

        Integer h = (spHora != null) ? spHora.getValue() : null;
        Integer m = (spMin  != null) ? spMin .getValue() : null;
        if (h == null || m == null) {
            alert(Alert.AlertType.WARNING, "Seleccione hora y minuto.");
            return;
        }

        LocalDate fecha = dpFechaViaje.getValue();
        LocalTime hora  = LocalTime.of(h, m);

        // Mapea ciudad → terminal
        int codTerminal;
        String c = (ciudadUsuario == null) ? "" : ciudadUsuario.toLowerCase();
        switch (c) {
            case "quito":  codTerminal = 1; break;
            case "ibarra": codTerminal = 2; break;
            default:
                alert(Alert.AlertType.ERROR, "Ciudad desconocida: " + ciudadUsuario);
                return;
        }

        // Actualiza DTO (tu DAO usa placa, no codVehiculo)
        viajeActual.setCodConductor(cbConductor.getValue().getCodConductor());
        viajeActual.setPlaca(cbVehiculo.getValue().getPlacaVehiculo());
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
            closeWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error al actualizar viaje:\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        closeWindow();
    }

    private void closeWindow() {
        if (btnCancelar != null && btnCancelar.getScene() != null) {
            Stage st = (Stage) btnCancelar.getScene().getWindow();
            if (st != null) st.close();
        } else if (btnGuardar != null && btnGuardar.getScene() != null) {
            Stage st = (Stage) btnGuardar.getScene().getWindow();
            if (st != null) st.close();
        }
    }

    public boolean isSaved() { return saved; }

    /* ======================= Helpers ======================= */
    private static void alert(Alert.AlertType t, String m) {
        new Alert(t, m).showAndWait();
    }
}
