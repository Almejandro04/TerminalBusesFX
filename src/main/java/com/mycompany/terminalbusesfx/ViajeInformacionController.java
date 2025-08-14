// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
//  */
// package com.mycompany.terminalbusesfx;

// import java.net.URL;
// import java.util.List;
// import java.util.ResourceBundle;

// import com.mycompany.terminalbusesBLL.ConductorVistaService;
// import com.mycompany.terminalbusesBLL.PasajeroVistaService;
// import com.mycompany.terminalbusesBLL.VehiculoVistaService;
// import com.mycompany.terminalbusesDAL.PasajeroVista;
// import com.mycompany.terminalbusesDAL.ViajeVista;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.stage.Stage;

// /**
//  * FXML Controller class
//  *
//  * @author Jorge
//  */
// public class ViajeInformacionController implements Initializable {
 
//     // Label para los datos del viaje
//     @FXML private Label lblTitulo;
//     @FXML private Label lblTerminal;
//     @FXML private Label lblDestino;
//     @FXML private Label lblFechaHora;
//     @FXML private Label lblPrecio;
//     @FXML private Label lblConductor;
//     @FXML private Label lblBus;
    

//     // Tabla de pasajeros
//     @FXML private TableView<PasajeroVista> tblPasajeros;
//     @FXML private TableColumn<PasajeroVista,Integer> colPasCedula;
//     @FXML private TableColumn<PasajeroVista,String> colPasNombre;
//     @FXML private TableColumn<PasajeroVista,String> colPasApellido;
//     @FXML private TableColumn<PasajeroVista,Integer> colPasTelefono;
//     @FXML private TableColumn<PasajeroVista,String> colPasCorreo;

//     // Campos internos
//     private String ciudadUsuario;
//     private ViajeVista viaje;
    
    
//     @Override
//     public void initialize(URL url, ResourceBundle rb) {
//         // Configura las columnas de la tabla de pasajeros
//         colPasCodigo.setCellValueFactory(new PropertyValueFactory<>("codPasajero"));
//         colViajePasajero.setCellValueFactory(new PropertyValueFactory<>("codViaje"));
//         colPasCedula .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
//         colPasNombre .setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
//         colPasApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
//         colPasTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
//         colPasCorreo.setCellValueFactory(new PropertyValueFactory<>("CorreoPasajero"));
        
//         colPasCodigo    .setVisible(false);
//         colViajePasajero.setVisible(false);


//     }
    
    
//         // setter para la ciudad (lo pasamos desde el padre)
//     public void setCiudadUsuario(String ciudad) {
//         this.ciudadUsuario = ciudad;
//     }

// //     setter para el viaje (lo pasamos desde el padre)
//     public void setViaje(ViajeVista viaje) {
//             System.out.println("DEBUG setViaje: codViaje=" + viaje.getCodViaje() + ", ciudad=" + ciudadUsuario);
//         this.viaje = viaje;
//         lblTitulo.setText("Detalle Del Viaje #" + viaje.getCodViaje());
//         this.viaje = viaje;
//         mostrarDatosViaje();
//         cargarTablaPasajeros();
//     }
    
//     private void mostrarDatosViaje() {
        
//     // Origen
//     lblOrigen.setText(ciudadUsuario);
//      // Destino, fecha/hora y precio directos
//     lblDestino.setText(viaje.getCiudadDestino());
//     lblFechaHora.setText(viaje.getFechaViaje() + " " + viaje.getHoraViaje());
//     lblPrecio.setText(String.valueOf(viaje.getPrecioViaje()));

//     // 1) Conductor: buscamos en la vista de conductores
//     ConductorVistaService cvs = new ConductorVistaService();
//     cvs.obtenerConductoresPorUsuario(ciudadUsuario).stream()
//        .filter(c -> c.getCodConductor() == viaje.getCodConductor())
//        .findFirst()
//        .ifPresent(c -> 
//            lblConductor.setText(c.getNombreConductor() + " " + c.getApellidoConductor())
//        );

//     // 2) Vehículo + Compañía
//     VehiculoVistaService vvs = new VehiculoVistaService();
//     vvs.obtenerVehiculosPorUsuario(ciudadUsuario).stream()
//        .filter(v -> v.getCodVehiculo() == viaje.getCodVehiculo())
//        .findFirst()
//        .ifPresent(vh -> {
//            lblVehiculo.setText(vh.getPlacaVehiculo());
//            lblCompania.setText(vh.getCompañiaVehiculo());
//        }); 
//     // Ajusta según tus getters en ViajeVista
// //    tfOrigen   .setText(viaje.getCodTerminal() + "");            // o getCiudadOrigen()
// //    tfDestino  .setText(viaje.getCiudadDestino());
// //    tfFechaHora.setText(viaje.getFechaViaje() + " " + viaje.getHoraViaje());
// //    tfPrecio   .setText(viaje.getPrecioViaje() + "");
// //    tfConductor.setText(viaje.getCodConductor() + "");          // o nombre completo si tu modelo lo incluye
// //    tfVehiculo .setText(viaje.getCodVehiculo() + "");
// //    tfCompania .setText(viaje.getCompania() != null ? viaje.getCompania() : "");
// }
    
// private void cargarTablaPasajeros() {
//     // Llama al service que filtra por ciudad y código de viaje
//         System.out.println("DEBUG cargarTablaPasajeros: ciudadUsuario=" 
//         + ciudadUsuario + ", codViaje=" + viaje.getCodViaje());
//     PasajeroVistaService svc = new PasajeroVistaService();
//     List<PasajeroVista> lista =
//         svc.obtenerPasajerosPorViaje(ciudadUsuario, viaje.getCodViaje());
//     tblPasajeros.setItems(FXCollections.observableArrayList(lista));
    
//     System.out.println("DEBUG pasajero count: " + lista.size());
//     System.out.println("DEBUG tvPasajeros null? " + (tblPasajeros == null));
//     System.out.println("DEBUG colPasCodigo null? " + (colPasCodigo == null));

    
//         // 2) Pasa la lista al TableView
//     ObservableList<PasajeroVista> obs = 
//         FXCollections.observableArrayList(lista);
//     tblPasajeros.setItems(obs);
    
// }    

//     @FXML private Button btnCerrar;
 

//     /**
//      * Initializes the controller class.
//      */
   
//     @FXML
//     private void handleCerrar(ActionEvent event) {
//         // Cierra solo esta ventana
//         Stage stage = (Stage) btnCerrar.getScene().getWindow();
//         stage.close();
//     }
// }

package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.mycompany.terminalbusesBLL.BoletoVistaService;
import com.mycompany.terminalbusesBLL.ConductorVistaService;
import com.mycompany.terminalbusesBLL.PasajeroVistaService;
import com.mycompany.terminalbusesBLL.TerminalVistaService;
import com.mycompany.terminalbusesBLL.VehiculoVistaService;
import com.mycompany.terminalbusesDAL.BoletoVista;
import com.mycompany.terminalbusesDAL.ConductorVista;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.TerminalVista;
import com.mycompany.terminalbusesDAL.VehiculoVista;
import com.mycompany.terminalbusesDAL.ViajeVista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViajeInformacionController implements Initializable {

    // ------- Labels del encabezado -------
    @FXML private Label lblTitulo;
    @FXML private Label lblTerminal;    // Nombre (Ciudad)
    @FXML private Label lblConductor;   // Nombre y Apellido
    @FXML private Label lblBus;         // Placa
    @FXML private Label lblRuta;        // Ciudad destino + precio
    @FXML private Label lblFechaHora;   // Fecha y hora

    // ------- Tabla de pasajeros -------
    @FXML private TableView<PasajeroVista> tblPasajeros;
    @FXML private TableColumn<PasajeroVista, String>  colPasCedula;
    @FXML private TableColumn<PasajeroVista, String>  colPasNombre;
    @FXML private TableColumn<PasajeroVista, String>  colPasApellido;
    @FXML private TableColumn<PasajeroVista, Integer> colPasTelefono;
    @FXML private TableColumn<PasajeroVista, String>  colPasCorreo;

    @FXML private Button btnCerrar;

    // ------- Estado -------
    private String ciudadUsuario; // "QUITO" o "IBARRA"
    private ViajeVista viaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mapeo de columnas (usa los nombres exactos de tu modelo PasajeroVista)
        colPasCedula  .setCellValueFactory(new PropertyValueFactory<>("cedulaPasajero"));
        colPasNombre  .setCellValueFactory(new PropertyValueFactory<>("nombrePasajero"));
        colPasApellido.setCellValueFactory(new PropertyValueFactory<>("apellidoPasajero"));
        colPasTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoPasajero"));
        colPasCorreo  .setCellValueFactory(new PropertyValueFactory<>("correoPasajero"));
    }

    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad;
    }

    public void setViaje(ViajeVista v) {
        this.viaje = v;
        lblTitulo.setText("Detalle Del Viaje #" + v.getCodViaje());

        // 1) Terminal
        TerminalVistaService tvs = new TerminalVistaService();
        TerminalVista terminal = tvs.obtenerTodosLosTerminales()
                .stream()
                .filter(t -> t.getCodTerminal() == v.getCodTerminal())
                .findFirst().orElse(null);
        if (terminal != null) {
            lblTerminal.setText(terminal.getNombreTerminal() + " (" + terminal.getCiudadTerminal() + ")");
        } else {
            lblTerminal.setText("Terminal #" + v.getCodTerminal());
        }

        // 2) Conductor (desde vista filtrada por ciudad)
        ConductorVistaService cvs = new ConductorVistaService();
        ConductorVista cond = cvs.obtenerConductoresPorUsuario(ciudadUsuario)
                .stream()
                .filter(c -> c.getCodConductor() == v.getCodConductor())
                .findFirst().orElse(null);
        lblConductor.setText(
            cond != null
                ? cond.getNombreConductor() + " " + cond.getApellidoConductor()
                : "Conductor #" + v.getCodConductor()
        );

        // 3) Bus (placa). ViajeVista suele traer 'placa'; si no, intenta localizar por codVehiculo
        String placa = null;
        try {
            // si tu ViajeVista tiene getPlaca()
            placa = (String) ViajeVista.class.getMethod("getPlaca").invoke(v);
        } catch (Exception ignore) { /* no tiene getPlaca, intentamos por servicio */ }

        if (placa == null) {
            VehiculoVistaService vvs = new VehiculoVistaService();
            VehiculoVista bus = vvs.obtenerVehiculosPorUsuario(ciudadUsuario)
                    .stream()
                    .filter(b -> {
                        try {
                            // si tu ViajeVista tiene getCodVehiculo()
                            Integer codVeh = (Integer) ViajeVista.class.getMethod("getCodVehiculo").invoke(v);
                            // OJO: si tu modelo VehiculoVista no tiene codVehiculo, ignora este bloque
                            return false; // ajusta si trabajas con ID de bus distinto a placa
                        } catch (Exception e) { return false; }
                    })
                    .findFirst().orElse(null);
            if (bus != null) placa = bus.getPlacaVehiculo();
        }
        lblBus.setText(placa != null ? placa : "-");

        // Ruta: solo número (cod_ruta)
        String rutaTxt = "-";
        try {
            rutaTxt = String.valueOf(viaje.getCodRuta()); // ViajeVista debe tener getCodRuta()
        } catch (Exception ignore) {
            // (opcional) fallback si hiciera falta buscarla por servicio
            // RutaVistaService rvs = new RutaVistaService();
            // RutaVista r = rvs.obtenerRutasPorUsuario(ciudadUsuario)
            //                  .stream().filter(x -> x.getCodRuta() == viaje.getCodRuta())
            //                  .findFirst().orElse(null);
            // if (r != null) rutaTxt = String.valueOf(r.getCodRuta());
        }
        lblRuta.setText(rutaTxt);

        // 5) Fecha y hora
        String fechaHora = "";
        try { fechaHora += v.getFechaViaje(); } catch (Exception ignore) {}
        try { fechaHora += (fechaHora.isEmpty() ? "" : " ") + v.getHoraViaje(); } catch (Exception ignore) {}
        lblFechaHora.setText(fechaHora.isEmpty() ? "-" : fechaHora);

        // 6) Pasajeros del viaje (vía BOLETO)
        cargarPasajerosDelViaje();
    }

    private void cargarPasajerosDelViaje() {
        if (viaje == null || ciudadUsuario == null) return;

        // a) Obtener boletos de la ciudad y filtrar por codViaje
        BoletoVistaService bsvc = new BoletoVistaService();
        List<BoletoVista> boletosCiudad = bsvc.obtenerBoletosPorUsuario(ciudadUsuario);
        Set<String> cedulasDeEsteViaje = boletosCiudad.stream()
                .filter(b -> b.getCodViaje() == viaje.getCodViaje())
                .map(BoletoVista::getCedulaPasajero)
                .collect(Collectors.toSet());

        // b) Obtener pasajeros de la ciudad y filtrar por cédulas de esos boletos
        PasajeroVistaService psvc = new PasajeroVistaService();
        List<PasajeroVista> pasajerosCiudad = psvc.obtenerPasajerosPorUsuario();
        List<PasajeroVista> pasajerosDelViaje = pasajerosCiudad.stream()
                .filter(p -> cedulasDeEsteViaje.contains(p.getCedulaPasajero()))
                .collect(Collectors.toList());

        // c) Cargar en la tabla
        tblPasajeros.setItems(FXCollections.observableArrayList(pasajerosDelViaje));
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
}
