/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.terminalbusesfx;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.ConductorProcedimientoService;
import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.PasajeroProcedimientoService;
import com.mycompany.terminalbusesBLL.PasajeroVistaService;
import com.mycompany.terminalbusesBLL.TerminalVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import com.mycompany.terminalbusesBLL.ViajeProcedimientoService;
import com.mycompany.terminalbusesBLL.ViajeVistaService;
import com.mycompany.terminalbusesDAL.ConductorVista;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.TerminalVista;
import com.mycompany.terminalbusesDAL.VehiculoVista;
import com.mycompany.terminalbusesDAL.ViajeVista;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class DetalleViajeFINALController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                // Listener: cada vez que cambie la pestaña seleccionada…
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::onTabChanged);

        // Llamada inicial para que la visibilidad se ajuste al tab por defecto
        
        
        colFecha.setVisible(false);
        colHora.setVisible(false);
        
        
        
        
//        cbOrigen.setItems(FXCollections.observableArrayList(currentUser));  // “IBARRA” o “QUITO”
//        cbOrigen.getSelectionModel().selectFirst();
//        cbOrigen.setDisable(true);  // bloqueamos la edición

        // 2) Destino: se cargan desde la base de datos o un servicio
        // (simulamos aquí)
        List<String> todosLosDestinos = List.of("Quito","Ibarra","Cuenca","Guayaquil");
        cbDestino.setItems(FXCollections.observableArrayList(todosLosDestinos));

        // 3) DatePickers con valores por defecto (opcional)
        dpFechaDesde.setValue(LocalDate.now().minusDays(7));
        dpFechaHasta.setValue(LocalDate.now());
        
        colcodViaje      .setCellValueFactory(new PropertyValueFactory<>("codViaje"));
        colConductor     .setCellValueFactory(new PropertyValueFactory<>("codConductor"));
        colVehiculo      .setCellValueFactory(new PropertyValueFactory<>("codVehiculo"));
        ColTerminal      .setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colFecha         .setCellValueFactory(new PropertyValueFactory<>("fechaViaje"));
        colHora          .setCellValueFactory(new PropertyValueFactory<>("horaViaje"));
        colCiudadDestino .setCellValueFactory(new PropertyValueFactory<>("ciudadDestino"));
        colPrecio        .setCellValueFactory(new PropertyValueFactory<>("precioViaje"));
        
        // Terminal
        colcodTerminal.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colTerminalCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudadTerminal"));
        colTerminalNombre.setCellValueFactory(new PropertyValueFactory<>("nombreTerminal"));
        colTerminalDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionTerminal"));
        
        // Conductor
        colcodConductor.setCellValueFactory(new PropertyValueFactory<>("codConductor"));
        colTerminalConductor.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colNombreConductor.setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
        colApellidoConductor.setCellValueFactory(new PropertyValueFactory<>("apellidoConductor"));
        colLicenciaConductor.setCellValueFactory(new PropertyValueFactory<>("licenciaConductor"));
        
        
        // Vehiculo
        colcodVehiculo.setCellValueFactory(new PropertyValueFactory<>("codVehiculo"));
        colTerminalVehiculo.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colPlacaVehiculo.setCellValueFactory(new PropertyValueFactory<>("placaVehiculo"));
        colCapacidadVehiculo.setCellValueFactory(new PropertyValueFactory<>("capacidadVehiculo"));
        colCompañiaVehiculo.setCellValueFactory(new PropertyValueFactory<>("compañiaVehiculo"));
        
        // Pasajero
        colPasCodigo.setCellValueFactory(new PropertyValueFactory<>("codPasajero"));
        colViajePasajero.setCellValueFactory(new PropertyValueFactory<>("codViaje"));
        colPasNombre.setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
        colPasApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
        colPasTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
        colPasCedula.setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
        colPasCorreo.setCellValueFactory(new PropertyValueFactory<>("correoPasajero"));
        
    }

    // Botones Generales
    @FXML private Button btnSalir;
    @FXML private Button btnBuscar;
    @FXML private TabPane tabPane;
    @FXML private Tab tabTerminales, tabViajes, tabRutas, tabBoletos, tabBuses, tabConductores, tabPasajeros;
    @FXML private Button btnNuevo, btnEditar, btnVer, btnBorrar;
    @FXML private DatePicker   dpFechaDesde;
    @FXML private DatePicker   dpFechaHasta;
    @FXML private ComboBox<String> cbOrigen;
    @FXML private ComboBox<String> cbDestino;

    // Tabla Terminal
    @FXML private TableView<TerminalVista> tblTerminales;
    @FXML private TableColumn<TerminalVista, Integer> colcodTerminal;
    @FXML private TableColumn<TerminalVista, String> colTerminalCiudad;
    @FXML private TableColumn<TerminalVista, String> colTerminalNombre;
    @FXML private TableColumn<TerminalVista, String> colTerminalDireccion;
    
    // Tabla Viajes
    @FXML private TableView<ViajeVista> tblViajes;
    @FXML private TableColumn<ViajeVista, Integer> colcodViaje;
    @FXML private TableColumn<ViajeVista, Integer> colConductor;
    @FXML private TableColumn<ViajeVista, Integer> colVehiculo;
    @FXML private TableColumn<ViajeVista, Integer> ColTerminal;       
    @FXML private TableColumn<ViajeVista, LocalDate> colFecha;
    @FXML private TableColumn<ViajeVista, LocalTime> colHora;
    @FXML private TableColumn<ViajeVista, String> colCiudadDestino;
    @FXML private TableColumn<ViajeVista, Double> colPrecio;
    
    // Tabla Rutas
    @FXML private TableView<?> tblRutas;
    @FXML private TableColumn<?, ?> colcodRuta;
    @FXML private TableColumn<?, ?> colTerminalRuta;
    @FXML private TableColumn<?, ?> colDestinoRuta;
    @FXML private TableColumn<?, ?> colDestinoPrecio;

    // Tabla Boletos
    @FXML private TableView<?> tblBoletos;
    @FXML private TableColumn<?, ?> colViajeBoleto;
    @FXML private TableColumn<?, ?> colCedulaBoleto;
    @FXML private TableColumn<?, ?> colAsientoBoleto;

    // Tabla Vehiculo
    @FXML private TableView<VehiculoVista> tblBuses;
    @FXML private TableColumn<VehiculoVista, String> colPlacaBus;
    @FXML private TableColumn<VehiculoVista, Integer> colTerminalBus;
    @FXML private TableColumn<VehiculoVista, Integer> colCapacidadBus;
    
    // Tabla Conductor
    @FXML private TableView<ConductorVista> tblConductores;
    @FXML private TableColumn<ConductorVista, Integer> colcodConductor;
    @FXML private TableColumn<ConductorVista, Integer> colTerminalConductor;
    @FXML private TableColumn<ConductorVista, String> colNombreConductor;
    @FXML private TableColumn<ConductorVista, String> colApellidoConductor;
    @FXML private TableColumn<ConductorVista, String> colLicenciaConductor;
    
    // Tabla Pasajeros
    @FXML private TableView<PasajeroVista> tblPasajeros;
    @FXML private TableColumn<PasajeroVista,Integer>   colPasCedula;
    @FXML private TableColumn<PasajeroVista,String>   colPasNombre;
    @FXML private TableColumn<PasajeroVista,String>   colPasApellido;
    @FXML private TableColumn<PasajeroVista,Integer>   colPasTelefono;
    @FXML private TableColumn<PasajeroVista,String>   colPasCorreo;
    
    
    // Configuracion Botones
    
    

    
    private String currentUser;
    
    

    public void setCurrentUser(String user) {
    this.currentUser = user;
            // 1) Poblamos el combo con ese único valor:
        ObservableList<String> datos = 
            FXCollections.observableArrayList(this.currentUser);
        cbOrigen.setItems(datos);

        // 2) Lo seleccionamos:
        cbOrigen.getSelectionModel().selectFirst();

        // 3) Lo deshabilitamos para que no se cambie
        cbOrigen.setDisable(true);
    configureColumns(); 
    onTabChanged(null, null, tabPane.getSelectionModel().getSelectedItem());
    }
    
    @FXML private void handleSalir(ActionEvent event) {
    try {
        // 1) Carga el FXML de la pantalla de usuario / login
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Login.fxml")
        );
        Parent root = loader.load();

        // 2) Obtén el Stage actual (ventana) desde el botón
        Stage stage = (Stage)((Node)event.getSource())
                         .getScene().getWindow();

        // 3) Pon la nueva escena en el mismo Stage
        stage.setScene(new Scene(root));
        stage.setTitle("Iniciar Sesión");  // opcional, ajusta el título

        // 4) (Opcional) si quieres restablecer tamaño, solo p. ej.:
        // stage.setWidth(600);
        // stage.setHeight(400);

        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        new Alert(AlertType.ERROR,
            "No se pudo volver al login:\n" + e.getMessage()
        ).showAndWait();
    }
}

    private void onTabChanged(ObservableValue<? extends Tab> obs, Tab oldTab, Tab newTab) {

    boolean isViajes    = newTab == tabViajes;
    boolean isPasajeros = newTab == tabPasajeros;
    boolean isTerminal = newTab == tabTerminales;
    boolean isConductor = newTab == tabConductores;
    boolean isBus = newTab == tabBuses;

    btnNuevo.setVisible(isViajes || isPasajeros || isConductor);
    btnEditar.setVisible(isViajes || isPasajeros);
    btnBorrar.setVisible(isViajes || isPasajeros || isConductor);

    btnVer   .setVisible(isViajes);

    if (isViajes) {
    
     // 1) Llama al servicio
    ViajeVistaService service = new ViajeVistaService();
    List<ViajeVista> lista = service.obtenerViajesPorUsuario(currentUser);

    // 2) Convierte a ObservableList
    ObservableList<ViajeVista> data = FXCollections.observableArrayList(lista);

    // 3) Asigna al TableView
    tblViajes.setItems(data);
    
    }
    
    if (isBus) {
    
     // 1) Llama al servicio
    VehiculoVistaService service = new VehiculoVistaService();
    List<VehiculoVista> lista = service.obtenerVehiculosPorUsuario(currentUser);

    // 2) Convierte a ObservableList
    ObservableList<VehiculoVista> data = FXCollections.observableArrayList(lista);
//
//    // 3) Asigna al TableView
//    tblVehiculo.setItems(data);
//    
    tblBuses.setItems(data);
    
    };
    
    
    if (isTerminal) {

        TerminalVistaService service = new TerminalVistaService();
        List<TerminalVista> lista = service.obtenerTodosLosTerminales();
        tblTerminales.setItems(
        FXCollections.observableArrayList(lista)
    );


}

    if (isConductor) {
    
     // 1) Llama al servicio
    ConductorVistaService service = new ConductorVistaService();
    List<ConductorVista> lista = service.obtenerConductoresPorUsuario(currentUser);
  
    ObservableList<ConductorVista> data =
        FXCollections.observableArrayList(lista);
    tblConductores.setItems(data);

    
    }
 
    if (isPasajeros) {
        if ("IBARRA".equals(currentUser)) {
            // Sólo Código, Nombre, Apellido, Cédula
            colPasCodigo.setVisible(true);
            colPasNombre.setVisible(true);
            colPasApellido.setVisible(true);
            colPasCedula.setVisible(true);
            // Oculta Teléfono y Correo
            colPasTelefono.setVisible(false);
            colPasCorreo  .setVisible(false);
        } else {  // Quito (u otro rol) ve TODO
            colPasCodigo.setVisible(true);
            colPasNombre.setVisible(true);
            colPasApellido.setVisible(true);
            colPasCedula.setVisible(true);
            colPasTelefono.setVisible(true);
            colPasCorreo  .setVisible(true);
        }
            // 1) Llama al servicio
    PasajeroVistaService service = new PasajeroVistaService();
    List<PasajeroVista> lista = service.obtenerPasajerosPorUsuario(currentUser);

    // 2) Convierte a ObservableList
    ObservableList<PasajeroVista> data = FXCollections.observableArrayList(lista);

    // 3) Asigna al TableView
    tblPasajeros.setItems(data);
    }
    else {
        // Si no es Pasajeros, opcionalmente oculta todas las columnas
        colPasCodigo.setVisible(false);
        colPasNombre.setVisible(false);
        colPasApellido.setVisible(false);
        colPasTelefono.setVisible(false);
        colPasCedula.setVisible(false);
        colPasCorreo.setVisible(false);
    }
}

  private void configureColumns() {
        if ("IBARRA".equals(currentUser)) {
            // muestra Vehículo, oculta Fecha/Hora
          
            colFecha  .setVisible(false);
            colHora   .setVisible(false);
        } else if ("QUITO".equals(currentUser)) {
            // muestra Fecha/Hora, oculta Vehículo
           
            colFecha  .setVisible(true);
            colHora   .setVisible(true);
        }
     
    }
  
@FXML private void handleBuscar(ActionEvent event) {
    LocalDate desde = dpFechaDesde.getValue();
    LocalDate hasta = dpFechaHasta.getValue();
    String origen  = cbOrigen.getValue();
    String destino = cbDestino.getValue();

    // 1) Validación sencilla
    if (desde == null || hasta == null || desde.isAfter(hasta)) {
        new Alert(Alert.AlertType.WARNING,
            "Seleccione un rango de fechas válido."
        ).showAndWait();
        return;
    }

}

@FXML
private void handleVer(ActionEvent event) {
    // 1) Obtén el viaje seleccionado
    ViajeVista viajeSeleccionado = tblViajes.getSelectionModel().getSelectedItem();
    System.out.println("DEBUG: handleVer invocado, selectedItem=" + viajeSeleccionado);
    if (viajeSeleccionado == null) {
        System.out.println("DEBUG: sel es null, mostrando alerta");
        new Alert(Alert.AlertType.WARNING,
            "Por favor, selecciona primero un viaje."
        ).showAndWait();
        return;
    }

    try {
        // 2) Prepara el loader apuntando al FXML de detalle
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("ViajeInformacion.fxml")
        );
        Parent root = loader.load();

        // 3) Obtén el controlador y pásale los datos
        ViajeInformacionController ctrl = loader.getController();
        System.out.println("DEBUG: setCiudadUsuario con " + this.currentUser);
        ctrl.setCiudadUsuario(this.currentUser);
        ctrl.setViaje(viajeSeleccionado);

        // 4) Crea y configura el Stage modal
        Stage detalleStage = new Stage();
        detalleStage.setTitle("Detalle del Viaje #" + viajeSeleccionado.getCodViaje());
        detalleStage.setScene(new Scene(root));
        detalleStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        detalleStage.initModality(Modality.WINDOW_MODAL);

        // 5) Muestra la ventana de detalle
        detalleStage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR,
            "No se pudo abrir la pantalla de detalle:\n" + e.getMessage()
        ).showAndWait();
    }
}


//  // añade tabConductores
@FXML
private void handleNuevo(ActionEvent event) {
    Tab selected = tabPane.getSelectionModel().getSelectedItem();
    boolean esViaje    = selected == tabViajes;
    boolean esPasajero = selected == tabPasajeros;
    boolean esConductor = selected == tabConductores;
    if (!esViaje && !esPasajero && !esConductor) {
        return;  // nada que hacer en otras pestañas
    }

    // 1) Elige el FXML según la pestaña
    String fxml = esViaje
        ? "/com/mycompany/terminalbusesfx/ingreso_Viaje.fxml"
        : esPasajero
            ? "/com/mycompany/terminalbusesfx/ingreso_Pasajero.fxml"
            : "/com/mycompany/terminalbusesfx/ingreso_Conductor.fxml";

    System.out.println("DEBUG: fxml=" + fxml + ", currentUser=" + currentUser);

    // 2) Verifica que exista el recurso
    URL resource = getClass().getResource(fxml);
    System.out.println("DEBUG: resource=" + resource);
    if (resource == null) {
        new Alert(Alert.AlertType.ERROR, "No se encontró el archivo FXML: " + fxml)
            .showAndWait();
        return;
    }

    try {
        // 3) Carga el FXML y obtiene el controlador
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        // 4) Inyecta la ciudad en el controlador adecuado
        if (esViaje) {
            Ingreso_ViajeController ctrl = loader.getController();
            ctrl.setCiudadUsuario(currentUser);
        }
        else if (esPasajero) {
            Ingreso_PasajeroController ctrl = loader.getController();
            ctrl.setCiudadUsuario(currentUser);
        }
        else { // esConductor
            Ingreso_ConductorController ctrl = loader.getController();
            ctrl.setCiudadUsuario(currentUser);
        }

        // 5) Prepara y muestra la ventana modal
        Stage dialog = new Stage();
        dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(esViaje
                        ? "Nuevo Viaje"
                        : esPasajero
                          ? "Nuevo Pasajero"
                          : "Nuevo Conductor");
        dialog.setScene(new Scene(root));
        dialog.showAndWait();

    } catch (IOException ex) {
        ex.printStackTrace();
        new Alert(Alert.AlertType.ERROR,
            "No se pudo abrir el formulario:\n" + ex.getMessage()
        ).showAndWait();
    }
}

@FXML
private void handleBorrar(ActionEvent e) {
    Tab selected = tabPane.getSelectionModel().getSelectedItem();
// 1) BORRAR VIAJE
    if (selected == tabViajes) {
        ViajeVista v = tblViajes.getSelectionModel().getSelectedItem();
        if (v == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un viaje primero.").showAndWait();
            return;
        }
        boolean ok = new ViajeProcedimientoService()
                         .borrarViaje(v.getCodViaje(), currentUser);
        if (ok) {
            tblViajes.getItems().remove(v);
        } else {
            new Alert(Alert.AlertType.ERROR, "No se pudo borrar el viaje.").showAndWait();
        }
        return;
    }

    // 2) BORRAR PASAJERO
    if (selected == tabPasajeros) {
        PasajeroVista p = tblPasajeros.getSelectionModel().getSelectedItem();
        if (p == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un pasajero primero.").showAndWait();
            return;
        }
        boolean ok = new PasajeroProcedimientoService()
                         .borrarPasajero(p.getCodPasajero(), currentUser);
        if (ok) {
            tblPasajeros.getItems().remove(p);
        } else {
            //new Alert(Alert.AlertType.ERROR, "No se pudo borrar el pasajero.").showAndWait();
        }
        return;
    }

    // 3) BORRAR CONDUCTOR
    if (selected == tabConductores) {
        ConductorVista c = tblConductores.getSelectionModel().getSelectedItem();
        if (c == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un conductor primero.").showAndWait();
            return;
        }
        boolean ok = new ConductorProcedimientoService()
                         .borrarConductor(c.getCodConductor(), currentUser);
        if (ok) {
            tblConductores.getItems().remove(c);
        } else {
            new Alert(Alert.AlertType.ERROR, "No se pudo borrar el conductor.").showAndWait();
        }
    }
}


@FXML
private void handleEditar(ActionEvent e) {
    // 0) Averigua qué pestaña está activa
    Tab selected = tabPane.getSelectionModel().getSelectedItem();

    // —— BLOQUE ESPECIAL PARA VIAJES ——
    if (selected == tabViajes) {
        // 1) Obtén el viaje elegido
        ViajeVista sel = tblViajes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING,
                      "Por favor seleccione un viaje primero.")
                .showAndWait();
            return;
        }

        try {
            // 2) Carga FXML de edición de viajes
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/terminalbusesfx/editar_Viaje.fxml")
            );
            Parent root = loader.load();

            // 3) Inyecta datos en el controller
            editar_ViajeController ctrl = loader.getController();
            ctrl.setCiudadUsuario(currentUser);
            ctrl.setViaje(sel);

            // 4) Muestra modal
            Stage st = new Stage();
            st.setTitle("Editar Viaje");
            st.initOwner(tblViajes.getScene().getWindow());
            st.initModality(Modality.APPLICATION_MODAL);
            st.setScene(new Scene(root));
            st.showAndWait();

            // 5) Refresca la tabla de viajes


        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "No se pudo cargar el formulario de edición de Viaje:\n"
                + ex.getMessage()
            ).showAndWait();
        }

        return;  // ¡importante! salimos del método aquí
    }

    // —— BLOQUE POR DEFECTO PARA PASAJEROS ——  
    // (todo lo que ya tenías)
    PasajeroVista sel = tblPasajeros.getSelectionModel().getSelectedItem();
    if (sel == null) {
        new Alert(Alert.AlertType.WARNING,
                  "Por favor seleccione un pasajero primero.")
            .showAndWait();
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/mycompany/terminalbusesfx/editar_pasajero.fxml")
        );
        Parent root = loader.load();

        editar_PasajeroController ctrl = loader.getController();
        ctrl.setCiudadUsuario(currentUser);
        ctrl.setPasajero(sel);

        Stage editStage = new Stage();
        editStage.setTitle("Editar Pasajero");
        editStage.initOwner(tblPasajeros.getScene().getWindow());
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setScene(new Scene(root));
        editStage.showAndWait();

    } catch (IOException ex) {
        ex.printStackTrace();
        new Alert(Alert.AlertType.ERROR,
            "No se pudo cargar el formulario de edición de Pasajero:\n"
            + ex.getMessage()
        ).showAndWait();
    }
}



}

 
 
 

    
   
