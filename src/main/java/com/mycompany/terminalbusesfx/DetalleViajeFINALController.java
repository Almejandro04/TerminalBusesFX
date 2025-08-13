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

import com.mycompany.terminalbusesBLL.BoletoVistaService;
import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.PasajeroVistaService;
import com.mycompany.terminalbusesBLL.RutaVistaService;
import com.mycompany.terminalbusesBLL.TerminalVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import com.mycompany.terminalbusesBLL.ViajeVistaService;
import com.mycompany.terminalbusesDAL.BoletoVista;
import com.mycompany.terminalbusesDAL.ConductorVista;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.RutaVista;
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
        
    // ------- VIAJES -------
    // Nota: evito duplicar la columna de terminal. Uso colTerminalViaje y oculto ColTerminal.
    colTerminalViaje.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colcodViaje     .setCellValueFactory(new PropertyValueFactory<>("codViaje"));
    colConductor    .setCellValueFactory(new PropertyValueFactory<>("codConductor"));
    colVehiculo     .setCellValueFactory(new PropertyValueFactory<>("placa"));
    ColRutaViaje    .setCellValueFactory(new PropertyValueFactory<>("codRuta"));
    colFecha        .setCellValueFactory(new PropertyValueFactory<>("fechaViaje"));
    colHora         .setCellValueFactory(new PropertyValueFactory<>("horaViaje"));
   
    // ------- TERMINALES -------
    colcodTerminal      .setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colTerminalCiudad   .setCellValueFactory(new PropertyValueFactory<>("ciudadTerminal"));
    colTerminalNombre   .setCellValueFactory(new PropertyValueFactory<>("nombreTerminal"));
    colTerminalDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionTerminal"));

    // ------- CONDUCTORES -------
    colcodConductor     .setCellValueFactory(new PropertyValueFactory<>("codConductor"));
    colTerminalConductor.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colNombreConductor  .setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
    colApellidoConductor.setCellValueFactory(new PropertyValueFactory<>("apellidoConductor"));
    // Tu modelo ya NO tiene "licenciaConductor"; mapeo esta columna a la cédula:
    colLicenciaConductor.setCellValueFactory(new PropertyValueFactory<>("cedulaConductor"));
    colLicenciaConductor.setText("Cédula"); // opcional: cambia el encabezado visible

    // ------- BUSES -------
    colPlacaBus   .setCellValueFactory(new PropertyValueFactory<>("placaVehiculo"));
    colTerminalBus.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colCapacidadBus.setCellValueFactory(new PropertyValueFactory<>("capacidadVehiculo"));

    // ------- PASAJEROS -------
    colPasNombre  .setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
    colPasApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
    colPasTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
    colPasCedula  .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
    colPasCorreo  .setCellValueFactory(new PropertyValueFactory<>("correoPasajero"));

    // ------- BOLETOS -------
    colTerminalBoleto.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colViajeBoleto   .setCellValueFactory(new PropertyValueFactory<>("codViaje"));
    colCedulaBoleto  .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
    colAsientoBoleto .setCellValueFactory(new PropertyValueFactory<>("numAsiento"));

    // ------- RUTAS -------
    colcodRuta     .setCellValueFactory(new PropertyValueFactory<>("codRuta"));
    colTerminalRuta.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
    colDestinoRuta .setCellValueFactory(new PropertyValueFactory<>("ciudadDestino"));
    colDestinoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

    //


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
    @FXML private TableColumn<ViajeVista, Integer> colTerminalViaje;
    @FXML private TableColumn<ViajeVista, Integer> colcodViaje;
    @FXML private TableColumn<ViajeVista, Integer> colConductor;
    @FXML private TableColumn<ViajeVista, Integer> colVehiculo;
    @FXML private TableColumn<ViajeVista, Integer> ColRutaViaje;       
    @FXML private TableColumn<ViajeVista, LocalDate> colFecha;
    @FXML private TableColumn<ViajeVista, LocalTime> colHora;


    
    // Tabla Rutas
    @FXML private TableView<RutaVista> tblRutas;
    @FXML private TableColumn<RutaVista, Integer> colcodRuta;
    @FXML private TableColumn<RutaVista, Integer> colTerminalRuta;
    @FXML private TableColumn<RutaVista, String> colDestinoRuta;
    @FXML private TableColumn<RutaVista, Double> colDestinoPrecio;

    // Tabla Boletos
    @FXML private TableView<BoletoVista> tblBoletos;
    @FXML private TableColumn<BoletoVista, Integer> colTerminalBoleto;
    @FXML private TableColumn<BoletoVista, Integer> colViajeBoleto;
    @FXML private TableColumn<BoletoVista, String>  colCedulaBoleto;
    @FXML private TableColumn<BoletoVista, Integer> colAsientoBoleto;


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

    boolean isTerminal    = newTab == tabTerminales;
    boolean isViajes      = newTab == tabViajes;
    boolean isRutas       = newTab == tabRutas;
    boolean isBoletos     = newTab == tabBoletos;
    boolean isBuses       = newTab == tabBuses;
    boolean isConductores = newTab == tabConductores;
    boolean isPasajeros   = newTab == tabPasajeros;

    // Botones visibles por pestaña
    btnNuevo.setVisible(isViajes || isRutas || isBoletos || isBuses || isConductores || isPasajeros);
    btnEditar.setVisible(isViajes || isPasajeros || isConductores || isRutas || isBoletos || isBuses);
    btnBorrar.setVisible(isViajes || isPasajeros || isConductores || isRutas || isBoletos || isBuses);
    btnVer.setVisible(isViajes);

    // --- Carga de datos según pestaña ---

    if (isTerminal) {
        TerminalVistaService service = new TerminalVistaService();
        List<TerminalVista> lista = service.obtenerTodosLosTerminales();
        tblTerminales.setItems(FXCollections.observableArrayList(lista));
    }

    if (isViajes) {
        ViajeVistaService service = new ViajeVistaService();
        List<ViajeVista> lista = service.obtenerViajesPorUsuario(currentUser);
        tblViajes.setItems(FXCollections.observableArrayList(lista));
    }

    if (isRutas) {
        // Aquí tu servicio para rutas
        RutaVistaService service = new RutaVistaService();
        List<RutaVista> lista = service.obtenerTodasLasRutas(currentUser);
        tblRutas.setItems(FXCollections.observableArrayList(lista));
    }

    if (isBoletos) {
        // Aquí tu servicio para boletos
        BoletoVistaService service = new BoletoVistaService();
        List<BoletoVista> lista = service.obtenerBoletosPorUsuario(currentUser);
        tblBoletos.setItems(FXCollections.observableArrayList(lista));
    }

    if (isBuses) {
        VehiculoVistaService service = new VehiculoVistaService();
        List<VehiculoVista> lista = service.obtenerVehiculosPorUsuario(currentUser);
        tblBuses.setItems(FXCollections.observableArrayList(lista));
    }

    if (isConductores) {
        ConductorVistaService service = new ConductorVistaService();
        List<ConductorVista> lista = service.obtenerConductoresPorUsuario(currentUser);
        tblConductores.setItems(FXCollections.observableArrayList(lista));
    }

    if (isPasajeros) {
        PasajeroVistaService service = new PasajeroVistaService();
        List<PasajeroVista> lista = service.obtenerPasajerosPorUsuario();
        tblPasajeros.setItems(FXCollections.observableArrayList(lista));
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
private void handleBorrar(ActionEvent event) {
}


@FXML
private void handleVer(ActionEvent event) {
    // 1) Obtén el viaje seleccionado
    // ViajeVista viajeSeleccionado = tblViajes.getSelectionModel().getSelectedItem();
    // System.out.println("DEBUG: handleVer invocado, selectedItem=" + viajeSeleccionado);
    // if (viajeSeleccionado == null) {
    //     System.out.println("DEBUG: sel es null, mostrando alerta");
    //     new Alert(Alert.AlertType.WARNING,
    //         "Por favor, selecciona primero un viaje."
    //     ).showAndWait();
    //     return;
    // }

    // try {
    //     // 2) Prepara el loader apuntando al FXML de detalle
    //     FXMLLoader loader = new FXMLLoader(
    //         getClass().getResource("ViajeInformacion.fxml")
    //     );
    //     Parent root = loader.load();

    //     // 3) Obtén el controlador y pásale los datos
    //     ViajeInformacionController ctrl = loader.getController();
    //     System.out.println("DEBUG: setCiudadUsuario con " + this.currentUser);
    //     ctrl.setCiudadUsuario(this.currentUser);
    //     ctrl.setViaje(viajeSeleccionado);

    //     // 4) Crea y configura el Stage modal
    //     Stage detalleStage = new Stage();
    //     detalleStage.setTitle("Detalle del Viaje #" + viajeSeleccionado.getCodViaje());
    //     detalleStage.setScene(new Scene(root));
    //     detalleStage.initOwner(((Node)event.getSource()).getScene().getWindow());
    //     detalleStage.initModality(Modality.WINDOW_MODAL);

    //     // 5) Muestra la ventana de detalle
    //     detalleStage.showAndWait();

    // } catch (IOException e) {
    //     e.printStackTrace();
    //     new Alert(Alert.AlertType.ERROR,
    //         "No se pudo abrir la pantalla de detalle:\n" + e.getMessage()
    //     ).showAndWait();
    // }
}

@FXML
private void handleNuevo(ActionEvent event) {
    Tab selected = tabPane.getSelectionModel().getSelectedItem();

    // Evitar acción en Terminales
    if (selected == tabTerminales) {
        return;
    }

    // Mapeo pestaña -> archivo FXML
    String fxml = null;
    String titulo = null;

    if (selected == tabViajes) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Viaje.fxml";
        titulo = "Nuevo Viaje";
    }
    else if (selected == tabRutas) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Ruta.fxml";
        titulo = "Nueva Ruta";
    }
    else if (selected == tabBoletos) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Boleto.fxml";
        titulo = "Nuevo Boleto";
    }
    else if (selected == tabBuses) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Bus.fxml";
        titulo = "Nuevo Bus";
    }
    else if (selected == tabConductores) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Conductor.fxml";
        titulo = "Nuevo Conductor";
    }
    else if (selected == tabPasajeros) {
        fxml = "/com/mycompany/terminalbusesfx/ingreso_Pasajero.fxml";
        titulo = "Nuevo Pasajero";
    }

    if (fxml == null) {
        System.err.println("No hay formulario FXML asignado para esta pestaña.");
        return;
    }

    // Verificar que el recurso exista
    URL resource = getClass().getResource(fxml);
    if (resource == null) {
        new Alert(Alert.AlertType.ERROR, "No se encontró el archivo FXML: " + fxml)
            .showAndWait();
        return;
    }

    // try {
    //     FXMLLoader loader = new FXMLLoader(resource);
    //     Parent root = loader.load();

    //     // Inyectar ciudad al controlador si aplica
    //     if (selected == tabViajes) {
    //         Ingreso_ViajeController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }
    //     else if (selected == tabRutas) {
    //         Ingreso_RutaController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }
    //     else if (selected == tabBoletos) {
    //         Ingreso_BoletoController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }
    //     else if (selected == tabBuses) {
    //         Ingreso_BusController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }
    //     else if (selected == tabConductores) {
    //         Ingreso_ConductorController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }
    //     else if (selected == tabPasajeros) {
    //         Ingreso_PasajeroController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //     }

    //     // Mostrar ventana modal
    //     Stage dialog = new Stage();
    //     dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
    //     dialog.initModality(Modality.WINDOW_MODAL);
    //     dialog.setTitle(titulo);
    //     dialog.setScene(new Scene(root));
    //     dialog.showAndWait();

    // } catch (IOException ex) {
    //     ex.printStackTrace();
    //     new Alert(Alert.AlertType.ERROR,
    //         "No se pudo abrir el formulario:\n" + ex.getMessage()
    //     ).showAndWait();
    // }
}


@FXML
private void handleEditar(ActionEvent e) {
    Tab selected = tabPane.getSelectionModel().getSelectedItem();

    // Terminales no es editable aquí
    if (selected == tabTerminales) {
        return;
    }

    String fxml = null;
    String titulo = null;
    Object seleccionado = null;

    // Selección y configuración por pestaña
    if (selected == tabViajes) {
        seleccionado = tblViajes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un viaje primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Viaje.fxml";
        titulo = "Editar Viaje";
    }
    else if (selected == tabRutas) {
        seleccionado = tblRutas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione una ruta primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Ruta.fxml";
        titulo = "Editar Ruta";
    }
    else if (selected == tabBoletos) {
        seleccionado = tblBoletos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un boleto primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Boleto.fxml";
        titulo = "Editar Boleto";
    }
    else if (selected == tabBuses) {
        seleccionado = tblBuses.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un bus primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Bus.fxml";
        titulo = "Editar Bus";
    }
    else if (selected == tabConductores) {
        seleccionado = tblConductores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un conductor primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Conductor.fxml";
        titulo = "Editar Conductor";
    }
    else if (selected == tabPasajeros) {
        seleccionado = tblPasajeros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un pasajero primero.").showAndWait();
            return;
        }
        fxml = "/com/mycompany/terminalbusesfx/editar_Pasajero.fxml";
        titulo = "Editar Pasajero";
    }

    // Verificar FXML encontrado
    if (fxml == null) {
        System.err.println("No se encontró un FXML para esta pestaña");
        return;
    }

    // try {
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    //     Parent root = loader.load();

    //     // Inyectar ciudad y objeto seleccionado en el controlador
    //     if (selected == tabViajes) {
    //         editar_ViajeController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setViaje((ViajeVista) seleccionado);
    //     }
    //     else if (selected == tabRutas) {
    //         editar_RutaController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setRuta((RutaVista) seleccionado);
    //     }
    //     else if (selected == tabBoletos) {
    //         editar_BoletoController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setBoleto((BoletoVista) seleccionado);
    //     }
    //     else if (selected == tabBuses) {
    //         editar_BusController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setBus((VehiculoVista) seleccionado);
    //     }
    //     else if (selected == tabConductores) {
    //         editar_ConductorController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setConductor((ConductorVista) seleccionado);
    //     }
    //     else if (selected == tabPasajeros) {
    //         editar_PasajeroController ctrl = loader.getController();
    //         ctrl.setCiudadUsuario(currentUser);
    //         ctrl.setPasajero((PasajeroVista) seleccionado);
    //     }

    //     // Mostrar modal
    //     Stage st = new Stage();
    //     st.setTitle(titulo);
    //     st.initOwner(((Node) e.getSource()).getScene().getWindow());
    //     st.initModality(Modality.APPLICATION_MODAL);
    //     st.setScene(new Scene(root));
    //     st.showAndWait();

    // } catch (IOException ex) {
    //     ex.printStackTrace();
    //     new Alert(Alert.AlertType.ERROR,
    //         "No se pudo cargar el formulario de edición:\n" + ex.getMessage()
    //     ).showAndWait();
    // }
}



}

 
 
 

    
   
