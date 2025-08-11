/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class Ingreso_ViajeController implements Initializable {
    
    @FXML private ComboBox<ConductorVista> cbConductor;
    @FXML private ComboBox<VehiculoVista>  cbVehiculo;
    @FXML private ComboBox<String>         cbDestino;
    @FXML private DatePicker               dpFechaViaje;
    @FXML private Spinner<Integer> spHora;
    @FXML private Spinner<Integer> spMin;
    @FXML private TextField                tfPrecio;
    @FXML private Button                   btnGuardar, btnCancelar;
    
    
    private String ciudadUsuario;
    private boolean saved = false;
    private int newCodViaje;
    
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;
    System.out.println("DEBUG(ingreso): poblando Conductor y Vehículo para " + ciudadUsuario);
    cbConductor.setItems(FXCollections.observableArrayList(
        new ConductorVistaService().obtenerConductoresPorUsuario(ciudadUsuario)
    ));
    cbVehiculo.setItems(FXCollections.observableArrayList(
        new VehiculoVistaService().obtenerVehiculosPorUsuario(ciudadUsuario)
    ));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carga conductores y vehículos para la ciudad

        cbDestino  .setItems(FXCollections.observableArrayList("Quito","Ibarra","Cuenca","Guayaquil"));

        // Inicializa el spinner de hora (ejemplo simple)
        spHora.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, LocalTime.now().getHour())
);      spMin .setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, LocalTime.now().getMinute())
);
    }
    
    @FXML private void handleGuardar(ActionEvent e) {
    // 1) Validaciones…
    if (cbConductor.getValue()==null || cbVehiculo.getValue()==null 
        || dpFechaViaje.getValue()==null || tfPrecio.getText().isBlank()) {
        new Alert(Alert.AlertType.WARNING,
          "Complete todos los campos obligatorios."
        ).showAndWait();
        return;
    }

    // 2) Aquí: leo la hora y obtengo el código de terminal
    LocalTime horaSeleccionada = LocalTime.of(spHora.getValue(), spMin.getValue());
    LocalDate fechaSeleccionada = dpFechaViaje.getValue();
    
        String ciudad = ciudadUsuario == null
       ? ""
       : ciudadUsuario.toLowerCase();
    
    int codTerminal;
    switch (ciudad) {
      case "ibarra":
        codTerminal = 2;
        break;
      case "quito":
        codTerminal = 1;
        break;
      default:
        throw new IllegalStateException("Ciudad de usuario desconocida: " + ciudad);
    }

    ViajeVista v = new ViajeVista(
        0,                          // codViaje (no importa, lo genera la BD)
        cbConductor.getValue().getCodConductor(),
        cbVehiculo .getValue().getCodVehiculo(),
        codTerminal,                // codTerminal calculado
        fechaSeleccionada,          // LocalDate
        horaSeleccionada,           // LocalTime
        cbDestino   .getValue(),    // ciudadDestino
        Double.parseDouble(tfPrecio.getText())  // precioViaje
    );
    // 4) Llamo al SP
    int idGenerado = new ViajeProcedimientoService().crearViaje(v, ciudad);

    // 5) Marco guardado y cierro
    this.newCodViaje = idGenerado;
    this.saved      = true;
    ((Stage)btnGuardar.getScene().getWindow()).close();
}
    
    
    
 @FXML
    private void handleCancelar(ActionEvent event) {
        // Cierra sólo esta ventana de ingreso
        Stage stage = (Stage) ((Node)event.getSource())
                              .getScene().getWindow();
        stage.close();
    }
    
        public boolean isSaved() {
        return saved;
    }
    public int getNewCodViaje() {
        return newCodViaje;
    }
    
}
