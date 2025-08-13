// package com.mycompany.terminalbusesfx;

// import java.net.URL;
// import java.util.ResourceBundle;

// import com.mycompany.terminalbusesBLL.PasajeroProcedimientoService;
// import com.mycompany.terminalbusesBLL.ViajeVistaService;
// import com.mycompany.terminalbusesDAL.PasajeroVista;
// import com.mycompany.terminalbusesDAL.ViajeVista;

// import javafx.collections.FXCollections;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.TextField;
// import javafx.stage.Stage;

// public class editar_PasajeroController implements Initializable {

//     @FXML private ComboBox<ViajeVista> cbViajePasajero;
//     @FXML private Button btnGuardar;
//     @FXML private Button btnCancelar;
//     @FXML private TextField tfNombre;
//     @FXML private TextField tfApellido;
//     @FXML private TextField tfTelefono;
//     @FXML private TextField tfCedula, tfCorreo;


//     private String ciudadUsuario;
//     private PasajeroVista pasajeroActual;

//     /** Inyecta la ciudad para poblar el combo de viajes */
//     public void setCiudadUsuario(String ciudad) {
//         this.ciudadUsuario = ciudad.toLowerCase();
//         cbViajePasajero.setItems(
//           FXCollections.observableArrayList(
//             new ViajeVistaService().obtenerViajesPorUsuario(ciudadUsuario)
//           )
//         );
//     }

//     /** Llama desde el padre para cargar los datos en el formulario */
//     public void setPasajero(PasajeroVista p) {
//         this.pasajeroActual = p;
//         // Selecciona el viaje
//         cbViajePasajero.getSelectionModel().select(
//           cbViajePasajero.getItems()
//             .stream()
//             .filter(v -> v.getCodViaje() == p.getCodViaje())
//             .findFirst().orElse(null)
//         );
//         tfNombre .setText(p.getNombrePasajero());
//         tfApellido .setText(p.getApellidoPasajero());
//         tfTelefono .setText(p.getTelefonoPasajero());
//         tfCedula   .setText(p.getCedulaPasajero());
//         tfCorreo   .setText(p.getCorreoPasajero());
//     }

//     @Override
//     public void initialize(URL url, ResourceBundle rb) {
//         // nada más aquí
//     }

//     @FXML
//     private void handleGuardar(ActionEvent e) {
//         // 1) Validaciones
//         if (cbViajePasajero.getValue()==null
//          || tfNombre.getText().isBlank()
//          || tfApellido.getText().isBlank()
//          || tfTelefono.getText().isBlank()
//          || tfCedula.getText().isBlank()
//          || tfCorreo.getText().isBlank()) {
//             new Alert(Alert.AlertType.WARNING,
//               "Complete todos los campos.").showAndWait();
//             return;
//         }

//         // 2) Construye objeto con mismos ID
//         PasajeroVista p = new PasajeroVista(
//             pasajeroActual.getCodPasajero(),
//             cbViajePasajero.getValue().getCodViaje(),
//             tfNombre.getText(),
//             tfApellido.getText(),
//             tfTelefono.getText(),
//             tfCedula.getText(),
//             tfCorreo.getText()
//         );

//         // 3) Llama al SP de UPDATE
//         boolean ok = new PasajeroProcedimientoService()
//                        .actualizarPasajero(p, ciudadUsuario);
//         if (ok) {
//             // cierra o notifica éxito
//             ((Stage)tfNombre.getScene().getWindow()).close();
//         } else {
//             new Alert(Alert.AlertType.ERROR,
//               "No se pudo actualizar.").showAndWait();
//         }
//     }

//     @FXML
//     private void handleCancelar(ActionEvent e) {
//         ((Stage)tfNombre.getScene().getWindow()).close();
//     }
// }
