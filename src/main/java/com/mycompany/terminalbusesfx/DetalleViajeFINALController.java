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

public class DetalleViajeFINALController implements Initializable {

    // Botones generales
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
    @FXML private TableColumn<TerminalVista, String>  colTerminalCiudad;
    @FXML private TableColumn<TerminalVista, String>  colTerminalNombre;
    @FXML private TableColumn<TerminalVista, String>  colTerminalDireccion;

    // Tabla Viajes
    @FXML private TableView<ViajeVista> tblViajes;
    @FXML private TableColumn<ViajeVista, Integer> colTerminalViaje;
    @FXML private TableColumn<ViajeVista, Integer> colcodViaje;
    @FXML private TableColumn<ViajeVista, Integer> colConductor;
    @FXML private TableColumn<ViajeVista, String>  colVehiculo;     // <- String (placa)
    @FXML private TableColumn<ViajeVista, Integer> ColRutaViaje;
    @FXML private TableColumn<ViajeVista, LocalDate> colFecha;
    @FXML private TableColumn<ViajeVista, LocalTime> colHora;

    // Tabla Rutas
    @FXML private TableView<RutaVista> tblRutas;
    @FXML private TableColumn<RutaVista, Integer> colcodRuta;
    @FXML private TableColumn<RutaVista, Integer> colTerminalRuta;
    @FXML private TableColumn<RutaVista, String>  colDestinoRuta;
    @FXML private TableColumn<RutaVista, Double>  colDestinoPrecio;

    // Tabla Boletos
    @FXML private TableView<BoletoVista> tblBoletos;
    @FXML private TableColumn<BoletoVista, Integer> colTerminalBoleto;
    @FXML private TableColumn<BoletoVista, Integer> colViajeBoleto;
    @FXML private TableColumn<BoletoVista, String>  colCedulaBoleto;
    @FXML private TableColumn<BoletoVista, Integer> colAsientoBoleto;

    // Tabla Vehiculo
    @FXML private TableView<VehiculoVista> tblBuses;
    @FXML private TableColumn<VehiculoVista, String>  colPlacaBus;
    @FXML private TableColumn<VehiculoVista, Integer> colTerminalBus;
    @FXML private TableColumn<VehiculoVista, Integer> colCapacidadBus;

    // Tabla Conductor
    @FXML private TableView<ConductorVista> tblConductores;
    @FXML private TableColumn<ConductorVista, Integer> colcodConductor;
    @FXML private TableColumn<ConductorVista, Integer> colTerminalConductor;
    @FXML private TableColumn<ConductorVista, String>  colNombreConductor;
    @FXML private TableColumn<ConductorVista, String>  colApellidoConductor;
    @FXML private TableColumn<ConductorVista, String>  colLicenciaConductor; // cédula

    // Tabla Pasajeros
    @FXML private TableView<PasajeroVista> tblPasajeros;
    @FXML private TableColumn<PasajeroVista, String>  colPasCedula;    // <- String
    @FXML private TableColumn<PasajeroVista, String>  colPasNombre;
    @FXML private TableColumn<PasajeroVista, String>  colPasApellido;
    @FXML private TableColumn<PasajeroVista, String>  colPasTelefono;  // <- String
    @FXML private TableColumn<PasajeroVista, String>  colPasCorreo;

    private String currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Listener cambio de pestaña
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::onTabChanged);

        // Ocultar campos de tiempo inicialmente
        colFecha.setVisible(false);
        colHora.setVisible(false);

        // Destinos demo
        List<String> todosLosDestinos = List.of("Quito","Ibarra","Cuenca","Guayaquil");
        cbDestino.setItems(FXCollections.observableArrayList(todosLosDestinos));

        // Rango de fechas por defecto
        dpFechaDesde.setValue(LocalDate.now().minusDays(7));
        dpFechaHasta.setValue(LocalDate.now());

        // ------- VIAJES -------
        colTerminalViaje.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colcodViaje     .setCellValueFactory(new PropertyValueFactory<>("codViaje"));
        colConductor    .setCellValueFactory(new PropertyValueFactory<>("codConductor"));
        colVehiculo     .setCellValueFactory(new PropertyValueFactory<>("placa"));        // placa (String)
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
        colLicenciaConductor.setCellValueFactory(new PropertyValueFactory<>("cedulaConductor"));
        colLicenciaConductor.setText("Cédula");

        // ------- BUSES -------
        colPlacaBus     .setCellValueFactory(new PropertyValueFactory<>("placaVehiculo"));
        colTerminalBus  .setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colCapacidadBus .setCellValueFactory(new PropertyValueFactory<>("capacidadVehiculo"));

        // ------- PASAJEROS -------
        colPasNombre   .setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
        colPasApellido .setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
        colPasTelefono .setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
        colPasCedula   .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
        colPasCorreo   .setCellValueFactory(new PropertyValueFactory<>("correoPasajero"));

        // ------- RUTAS -------
        colcodRuta      .setCellValueFactory(new PropertyValueFactory<>("codRuta"));
        colTerminalRuta .setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colDestinoRuta  .setCellValueFactory(new PropertyValueFactory<>("ciudadDestino"));
        colDestinoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Boleto
        colTerminalBoleto.setCellValueFactory(new PropertyValueFactory<>("codTerminal"));
        colViajeBoleto   .setCellValueFactory(new PropertyValueFactory<>("codViaje"));
        colCedulaBoleto  .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
        colAsientoBoleto .setCellValueFactory(new PropertyValueFactory<>("numAsiento"));



    }

    public void setCurrentUser(String user) {
        this.currentUser = user;

        // Combo Origen (no editable)
        ObservableList<String> datos = FXCollections.observableArrayList(this.currentUser);
        cbOrigen.setItems(datos);
        cbOrigen.getSelectionModel().selectFirst();
        cbOrigen.setDisable(true);

        configureColumns();
        onTabChanged(null, null, tabPane.getSelectionModel().getSelectedItem());
    }

    private void configureColumns() {
        if ("IBARRA".equalsIgnoreCase(currentUser)) {
            colFecha.setVisible(false);
            colHora.setVisible(false);
        } else if ("QUITO".equalsIgnoreCase(currentUser)) {
            colFecha.setVisible(true);
            colHora.setVisible(true);
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

        btnNuevo.setVisible(isViajes || isRutas || isBoletos || isBuses || isConductores || isPasajeros);
        btnEditar.setVisible(isViajes || isConductores || isRutas || isBoletos || isBuses || isPasajeros);
        btnBorrar.setVisible(isViajes || isConductores || isRutas || isBoletos || isBuses || isPasajeros);
        btnVer.setVisible(isViajes);

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
            RutaVistaService service = new RutaVistaService();
            List<RutaVista> lista = service.obtenerTodasLasRutas(currentUser);
            tblRutas.setItems(FXCollections.observableArrayList(lista));
        }
        if (isBoletos) {
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

    @FXML
    private void handleSalir(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo volver al login:\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleBuscar(ActionEvent event) {
        LocalDate desde = dpFechaDesde.getValue();
        LocalDate hasta = dpFechaHasta.getValue();
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un rango de fechas válido.").showAndWait();
            return;
        }
        // Aquí puedes filtrar por fecha si tu servicio lo soporta.
    }

    @FXML
    private void handleVer(ActionEvent event) {
        ViajeVista viajeSeleccionado = tblViajes.getSelectionModel().getSelectedItem();
        if (viajeSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, selecciona primero un viaje.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViajeInformacion.fxml"));
            Parent root = loader.load();

            ViajeInformacionController ctrl = loader.getController();
            ctrl.setCiudadUsuario(this.currentUser);
            ctrl.setViaje(viajeSeleccionado);

            Stage detalleStage = new Stage();
            detalleStage.setTitle("Detalle del Viaje #" + viajeSeleccionado.getCodViaje());
            detalleStage.setScene(new Scene(root));
            detalleStage.initOwner(((Node)event.getSource()).getScene().getWindow());
            detalleStage.initModality(Modality.WINDOW_MODAL);
            detalleStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la pantalla de detalle:\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleNuevo(ActionEvent event) {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected == tabTerminales) return;

        String fxml = null, titulo = null;

        if (selected == tabViajes)      { fxml = "/com/mycompany/terminalbusesfx/ingreso_Viaje.fxml";     titulo = "Nuevo Viaje"; }
        else if (selected == tabRutas)  { fxml = "/com/mycompany/terminalbusesfx/ingreso_Ruta.fxml";      titulo = "Nueva Ruta"; }
        else if (selected == tabBoletos){ fxml = "/com/mycompany/terminalbusesfx/ingreso_Boleto.fxml";    titulo = "Nuevo Boleto"; }
        else if (selected == tabBuses)  { fxml = "/com/mycompany/terminalbusesfx/ingreso_Bus.fxml";       titulo = "Nuevo Bus"; }
        else if (selected == tabConductores){ fxml = "/com/mycompany/terminalbusesfx/ingreso_Conductor.fxml"; titulo = "Nuevo Conductor"; }
        else if (selected == tabPasajeros){ fxml = "/com/mycompany/terminalbusesfx/ingreso_Pasajero.fxml";   titulo = "Nuevo Pasajero"; }

        if (fxml == null) {
            System.err.println("No hay formulario FXML asignado para esta pestaña.");
            return;
        }

        URL resource = getClass().getResource(fxml);
        if (resource == null) {
            new Alert(Alert.AlertType.ERROR, "No se encontró el archivo FXML: " + fxml).showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            if (selected == tabViajes) {
                Ingreso_ViajeController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            } else if (selected == tabRutas) {
                Ingreso_RutaController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            } else if (selected == tabBoletos) {
                Ingreso_BoletoController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            } else if (selected == tabBuses) {
                Ingreso_BusController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            } else if (selected == tabConductores) {
                Ingreso_ConductorController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            } else if (selected == tabPasajeros) {
                Ingreso_PasajeroController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
            }

            Stage dialog = new Stage();
            dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle(titulo);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el formulario:\n" + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleEditar(ActionEvent e) {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected == tabTerminales) return;

        String fxml = null, titulo = null;
        Object seleccionado = null;

        if (selected == tabViajes) {
            seleccionado = tblViajes.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione un viaje primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Viaje.fxml";
            titulo = "Editar Viaje";
        } else if (selected == tabRutas) {
            seleccionado = tblRutas.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione una ruta primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Ruta.fxml";
            titulo = "Editar Ruta";
        } else if (selected == tabBoletos) {
            seleccionado = tblBoletos.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione un boleto primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Boleto.fxml";
            titulo = "Editar Boleto";
        } else if (selected == tabBuses) {
            seleccionado = tblBuses.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione un bus primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Bus.fxml";
            titulo = "Editar Bus";
        } else if (selected == tabConductores) {
            seleccionado = tblConductores.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione un conductor primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Conductor.fxml";
            titulo = "Editar Conductor";
        } else if (selected == tabPasajeros) {
            seleccionado = tblPasajeros.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { warn("Seleccione un pasajero primero."); return; }
            fxml = "/com/mycompany/terminalbusesfx/editar_Pasajero.fxml";
            titulo = "Editar Pasajero";
        }

        if (fxml == null) {
            System.err.println("No se encontró un FXML para esta pestaña");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            if (selected == tabViajes) {
                editar_ViajeController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setViaje((ViajeVista) seleccionado);
            } else if (selected == tabRutas) {
                editar_RutaController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setRuta((RutaVista) seleccionado);
            } else if (selected == tabBoletos) {
                editar_BoletoController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setBoleto((BoletoVista) seleccionado);
            } else if (selected == tabBuses) {
                editar_BusController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setBus((VehiculoVista) seleccionado);
            } else if (selected == tabConductores) {
                editar_ConductorController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setConductor((ConductorVista) seleccionado);
            } else if (selected == tabPasajeros) {
                editar_PasajeroController ctrl = loader.getController();
                ctrl.setCiudadUsuario(currentUser);
                ctrl.setPasajero((PasajeroVista) seleccionado);
            }

            Stage st = new Stage();
            st.setTitle(titulo);
            st.initOwner(((Node) e.getSource()).getScene().getWindow());
            st.initModality(Modality.APPLICATION_MODAL);
            st.setScene(new Scene(root));
            st.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar el formulario de edición:\n" + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleBorrar(ActionEvent event) {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        // cod_terminal por usuario
        int codTerminal = -1;
        String u = currentUser == null ? "" : currentUser.trim().toLowerCase();
        if ("quito".equals(u)) codTerminal = 1;
        else if ("ibarra".equals(u)) codTerminal = 2;

        java.util.function.Function<String, Boolean> confirmar = (msg) -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg,
                    javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
            a.setHeaderText(null);
            a.setTitle("Confirmar eliminación");
            a.showAndWait();
            return a.getResult() == javafx.scene.control.ButtonType.OK;
        };

        try {
            if (selected == tabViajes) {
                ViajeVista v = tblViajes.getSelectionModel().getSelectedItem();
                if (v == null) { warn("Seleccione un viaje primero."); return; }
                if (!confirmar.apply("¿Eliminar el viaje #" + v.getCodViaje() + "? También se eliminarán sus boletos.")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.ViajeProcedimientoService()
                        .borrarViaje(v.getCodTerminal(), v.getCodViaje());
                if (!ok) { error("No se pudo eliminar el viaje."); return; }

                List<ViajeVista> lista = new com.mycompany.terminalbusesBLL.ViajeVistaService()
                        .obtenerViajesPorUsuario(currentUser);
                tblViajes.setItems(FXCollections.observableArrayList(lista));
            }
            else if (selected == tabRutas) {
                RutaVista r = tblRutas.getSelectionModel().getSelectedItem();
                if (r == null) { warn("Seleccione una ruta primero."); return; }
                if (!confirmar.apply("¿Eliminar la ruta #" + r.getCodRuta() + "? Esto eliminará sus viajes y boletos asociados.")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.RutaProcedimientoService()
                        .borrarRuta(r.getCodTerminal(), r.getCodRuta());
                if (!ok) { error("No se pudo eliminar la ruta."); return; }

                List<RutaVista> lista = new com.mycompany.terminalbusesBLL.RutaVistaService()
                        .obtenerTodasLasRutas(currentUser);
                tblRutas.setItems(FXCollections.observableArrayList(lista));
            }
            else if (selected == tabBoletos) {
                BoletoVista b = tblBoletos.getSelectionModel().getSelectedItem();
                if (b == null) { warn("Seleccione un boleto primero."); return; }
                if (!confirmar.apply("¿Eliminar el boleto del pasajero " + b.getCedulaPasajero()
                        + " en el viaje #" + b.getCodViaje() + "?")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.BoletoProcedimientoService()
                        .borrarBoleto(b.getCodTerminal(), b.getCodViaje(), b.getCedulaPasajero());
                if (!ok) { error("No se pudo eliminar el boleto."); return; }

                List<BoletoVista> lista = new com.mycompany.terminalbusesBLL.BoletoVistaService()
                        .obtenerBoletosPorUsuario(currentUser);
                tblBoletos.setItems(FXCollections.observableArrayList(lista));
            }
            else if (selected == tabBuses) {
                VehiculoVista v = tblBuses.getSelectionModel().getSelectedItem();
                if (v == null) { warn("Seleccione un bus primero."); return; }
                if (!confirmar.apply("¿Eliminar el bus con placa " + v.getPlacaVehiculo()
                        + "? Esto eliminará sus viajes y boletos asociados.")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.VehiculoProcedimientoService()
                        .borrarVehiculo(v.getCodTerminal(), v.getPlacaVehiculo());
                if (!ok) { error("No se pudo eliminar el bus."); return; }

                List<VehiculoVista> lista = new com.mycompany.terminalbusesBLL.VehiculoVistaService()
                        .obtenerVehiculosPorUsuario(currentUser);
                tblBuses.setItems(FXCollections.observableArrayList(lista));
            }
            else if (selected == tabConductores) {
                ConductorVista c = tblConductores.getSelectionModel().getSelectedItem();
                if (c == null) { warn("Seleccione un conductor primero."); return; }
                if (!confirmar.apply("¿Eliminar al conductor #" + c.getCodConductor()
                        + " (" + c.getNombreConductor() + " " + c.getApellidoConductor() + ")? Esto eliminará sus viajes y boletos asociados.")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.ConductorProcedimientoService()
                        .borrarConductor(c.getCodConductor());
                if (!ok) { error("No se pudo eliminar el conductor."); return; }

                List<ConductorVista> lista = new com.mycompany.terminalbusesBLL.ConductorVistaService()
                        .obtenerConductoresPorUsuario(currentUser);
                tblConductores.setItems(FXCollections.observableArrayList(lista));
            }
            else if (selected == tabPasajeros) {
                PasajeroVista p = tblPasajeros.getSelectionModel().getSelectedItem();
                if (p == null) { warn("Seleccione un pasajero primero."); return; }
                if (!confirmar.apply("¿Eliminar al pasajero " + p.getCedulaPasajero()
                        + " (" + p.getNombrePasajero() + " " + p.getApellidoPasajero() + ")? También se eliminarán sus boletos.")) return;

                boolean ok = new com.mycompany.terminalbusesBLL.PasajeroProcedimientoService()
                        .borrarPasajero(p.getCedulaPasajero());
                if (!ok) { error("No se pudo eliminar el pasajero."); return; }

                List<PasajeroVista> lista = new com.mycompany.terminalbusesBLL.PasajeroVistaService()
                        .obtenerPasajerosPorUsuario();
                tblPasajeros.setItems(FXCollections.observableArrayList(lista));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            error("Error al borrar:\n" + ex.getMessage());
        }
    }

    // Helpers locales
    private static void warn(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR,   m).showAndWait(); }
}


    
   
