package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import com.mycompany.terminalbusesBLL.ViajeProcedimientoService;
import com.mycompany.terminalbusesDAL.ConductorVista;
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

public class editar_ViajeController implements Initializable {

    @FXML private TextField                tfCodigo;
    @FXML private ComboBox<ConductorVista> cbConductor;
    @FXML private ComboBox<VehiculoVista>  cbVehiculo;
    @FXML private ComboBox<String>         cbDestino;
    @FXML private DatePicker               dpFechaViaje;
    @FXML private Spinner<Integer>       spHora, spMin;
    @FXML private TextField                tfPrecio;
    @FXML private Button                   btnGuardar, btnCancelar;
    

    private String ciudadUsuario;
    private ViajeVista viajeActual;
    private boolean saved = false;
    private ViajeProcedimientoService service = new ViajeProcedimientoService();
    /** Inyecta la ciudad y carga conductores/vehículos */
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad.toLowerCase();
        cbConductor.setItems(FXCollections.observableArrayList(
            new ConductorVistaService().obtenerConductoresPorUsuario(ciudadUsuario)
        ));
        cbVehiculo.setItems(FXCollections.observableArrayList(
            new VehiculoVistaService().obtenerVehiculosPorUsuario(ciudadUsuario)
        ));
        // destinos fijos (igual que en ingreso)
        cbDestino.setItems(FXCollections.observableArrayList(
            "Quito","Ibarra","Cuenca","Guayaquil"
        ));
    }

        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Spinner de hora
        spHora.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour())
        );
        // Spinner de minutos
        spMin.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute())
        );
    }

        /** Llama el padre para cargar datos en el formulario */
    public void setViaje(ViajeVista v) {
        this.viajeActual = v;

        // Selecciono conductor y vehículo en los combos
        cbConductor.getSelectionModel().select(
            cbConductor.getItems().stream()
                .filter(c -> c.getCodConductor() == v.getCodConductor())
                .findFirst().orElse(null)
        );
        cbVehiculo.getSelectionModel().select(
            cbVehiculo.getItems().stream()
                .filter(vh -> vh.getCodVehiculo() == v.getCodVehiculo())
                .findFirst().orElse(null)
        );

        // Seleccionar destino, fecha, hora y precio
        cbDestino.getSelectionModel().select(v.getCiudadDestino());
        dpFechaViaje.setValue(v.getFechaViaje());
        spHora.getValueFactory().setValue(v.getHoraViaje().getHour());
        spMin.getValueFactory().setValue(v.getHoraViaje().getMinute());
        tfPrecio.setText(Double.toString(v.getPrecioViaje()));
    }

        @FXML
    private void handleGuardar(ActionEvent e) {
        // validación
        if (cbConductor.getValue() == null ||
            cbVehiculo.getValue()  == null ||
            dpFechaViaje.getValue() == null ||
            tfPrecio.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING,
                "Complete todos los campos obligatorios."
            ).showAndWait();
            return;
        }

        // leo hora y minuto
        LocalTime hora = LocalTime.of(spHora.getValue(), spMin.getValue());
        LocalDate fecha = dpFechaViaje.getValue();

        // obtengo codTerminal igual que en Ingreso
        int codTerminal;
        switch (ciudadUsuario) {
            case "ibarra": codTerminal = 2; break;
            case "quito":  codTerminal = 1; break;
            default:
                throw new IllegalStateException(
                    "Ciudad de usuario desconocida: " + ciudadUsuario);
        }

        // actualizo el objeto
        viajeActual.setCodConductor(cbConductor.getValue().getCodConductor());
        viajeActual.setCodVehiculo (cbVehiculo.getValue().getCodVehiculo());
        viajeActual.setCodTerminal (codTerminal);
        viajeActual.setCiudadDestino(cbDestino.getValue());
        viajeActual.setFechaViaje  (fecha);
        viajeActual.setHoraViaje   (hora);
        viajeActual.setPrecioViaje (Double.parseDouble(tfPrecio.getText()));

        // llamo al service
        service.actualizarViaje(viajeActual, ciudadUsuario);
        new Alert(Alert.AlertType.INFORMATION,
            "Viaje actualizado correctamente."
        ).showAndWait();

        this.saved = true;
        // cierro sólo este Stage
        ((Stage)btnGuardar.getScene().getWindow()).close();
    }


        @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)btnCancelar.getScene().getWindow()).close();
    }

    /** Opcional si quieres saber desde el padre si se guardó */
    public boolean isSaved() {
        return saved;
    }
}



