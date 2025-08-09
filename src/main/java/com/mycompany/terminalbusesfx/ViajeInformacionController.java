/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.terminalbusesfx;

import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.PasajeroVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.ViajeVista;
import com.mycompany.terminalbusesDAL.VehiculoVista;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class ViajeInformacionController implements Initializable {
 
    // Label para los datos del viaje
    @FXML private Label lblTitulo;
    @FXML private Label lblOrigen;
    @FXML private Label lblDestino;
    @FXML private Label lblFechaHora;
    @FXML private Label lblPrecio;
    @FXML private Label lblConductor;
    @FXML private Label lblVehiculo;
    @FXML private Label lblCompania;

    // Tabla de pasajeros
    @FXML private TableView<PasajeroVista> tblPasajeros;
    @FXML private TableColumn<PasajeroVista,Integer> colPasCodigo;
    @FXML private TableColumn<PasajeroVista,Integer> colViajePasajero;
    @FXML private TableColumn<PasajeroVista,Integer> colPasCedula;
    @FXML private TableColumn<PasajeroVista,String> colPasNombre;
    @FXML private TableColumn<PasajeroVista,String> colPasApellido;
    @FXML private TableColumn<PasajeroVista,Integer> colPasTelefono;
    @FXML private TableColumn<PasajeroVista,String> colPasCorreo;

    // Campos internos
    private String ciudadUsuario;
    private ViajeVista viaje;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura las columnas de la tabla de pasajeros
        colPasCodigo.setCellValueFactory(new PropertyValueFactory<>("codPasajero"));
        colViajePasajero.setCellValueFactory(new PropertyValueFactory<>("codViaje"));
        colPasCedula .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
        colPasNombre .setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
        colPasApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
        colPasTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
        colPasCorreo.setCellValueFactory(new PropertyValueFactory<>("CorreoPasajero"));
        
        colPasCodigo    .setVisible(false);
        colViajePasajero.setVisible(false);


    }
    
    
        // setter para la ciudad (lo pasamos desde el padre)
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;
    }

//     setter para el viaje (lo pasamos desde el padre)
    public void setViaje(ViajeVista viaje) {
            System.out.println("DEBUG setViaje: codViaje=" + viaje.getCodViaje() + ", ciudad=" + ciudadUsuario);
        this.viaje = viaje;
        lblTitulo.setText("Detalle Del Viaje #" + viaje.getCodViaje());
        this.viaje = viaje;
        mostrarDatosViaje();
        cargarTablaPasajeros();
    }
    
    private void mostrarDatosViaje() {
        
    // Origen
    lblOrigen.setText(ciudadUsuario);
     // Destino, fecha/hora y precio directos
    lblDestino.setText(viaje.getCiudadDestino());
    lblFechaHora.setText(viaje.getFechaViaje() + " " + viaje.getHoraViaje());
    lblPrecio.setText(String.valueOf(viaje.getPrecioViaje()));

    // 1) Conductor: buscamos en la vista de conductores
    ConductorVistaService cvs = new ConductorVistaService();
    cvs.obtenerConductoresPorUsuario(ciudadUsuario).stream()
       .filter(c -> c.getCodConductor() == viaje.getCodConductor())
       .findFirst()
       .ifPresent(c -> 
           lblConductor.setText(c.getNombreConductor() + " " + c.getApellidoConductor())
       );

    // 2) Vehículo + Compañía
    VehiculoVistaService vvs = new VehiculoVistaService();
    vvs.obtenerVehiculosPorUsuario(ciudadUsuario).stream()
       .filter(v -> v.getCodVehiculo() == viaje.getCodVehiculo())
       .findFirst()
       .ifPresent(vh -> {
           lblVehiculo.setText(vh.getPlacaVehiculo());
           lblCompania.setText(vh.getCompañiaVehiculo());
       }); 
    // Ajusta según tus getters en ViajeVista
//    tfOrigen   .setText(viaje.getCodTerminal() + "");            // o getCiudadOrigen()
//    tfDestino  .setText(viaje.getCiudadDestino());
//    tfFechaHora.setText(viaje.getFechaViaje() + " " + viaje.getHoraViaje());
//    tfPrecio   .setText(viaje.getPrecioViaje() + "");
//    tfConductor.setText(viaje.getCodConductor() + "");          // o nombre completo si tu modelo lo incluye
//    tfVehiculo .setText(viaje.getCodVehiculo() + "");
//    tfCompania .setText(viaje.getCompania() != null ? viaje.getCompania() : "");
}
    
private void cargarTablaPasajeros() {
    // Llama al service que filtra por ciudad y código de viaje
        System.out.println("DEBUG cargarTablaPasajeros: ciudadUsuario=" 
        + ciudadUsuario + ", codViaje=" + viaje.getCodViaje());
    PasajeroVistaService svc = new PasajeroVistaService();
    List<PasajeroVista> lista =
        svc.obtenerPasajerosPorViaje(ciudadUsuario, viaje.getCodViaje());
    tblPasajeros.setItems(FXCollections.observableArrayList(lista));
    
    System.out.println("DEBUG pasajero count: " + lista.size());
    System.out.println("DEBUG tvPasajeros null? " + (tblPasajeros == null));
    System.out.println("DEBUG colPasCodigo null? " + (colPasCodigo == null));

    
        // 2) Pasa la lista al TableView
    ObservableList<PasajeroVista> obs = 
        FXCollections.observableArrayList(lista);
    tblPasajeros.setItems(obs);
    
}    

    @FXML private Button btnCerrar;
 

    /**
     * Initializes the controller class.
     */
   
    @FXML
    private void handleCerrar(ActionEvent event) {
        // Cierra solo esta ventana
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}
