package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.PasajeroProcedimientoService;
import com.mycompany.terminalbusesBLL.ViajeVistaService;
import com.mycompany.terminalbusesDAL.PasajeroVista;
import com.mycompany.terminalbusesDAL.ViajeVista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Ingreso_PasajeroController implements Initializable {
    
    @FXML private ComboBox<ViajeVista> cbViajePasajero;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfTelefono;
    @FXML private TextField tfCedula, tfCorreo;

    @FXML private String ciudadUsuario;
    @FXML private boolean saved = false;
    @FXML private int newCodPasajero;
    @FXML private TextField tfLicencia;
    @FXML private TextField tfCodigo;

    /**
     * Este setter **solo** asigna la ciudad** y luego puebla el ComboBox.
     * Asegúrate de llamarlo **una vez** desde quien abra esta ventana, antes de mostrarla.
     */
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad.toLowerCase();

        // Pobla los viajes **solo** después de asignar la ciudad
        cbViajePasajero.setItems(
            FXCollections.observableArrayList(
                new ViajeVistaService()
                    .obtenerViajesPorUsuario(ciudadUsuario)
            )
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asegúrate de llamar a setCiudadUsuario(...) DESPUÉS de initialize(), no aquí.
        // Configuramos la apariencia del ComboBox:
        cbViajePasajero.setCellFactory(lv -> new ListCell<ViajeVista>() {
            @Override
            protected void updateItem(ViajeVista v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null 
                    ? null 
                    : String.valueOf(v.getCodViaje()));
            }
        });
        cbViajePasajero.setButtonCell(new ListCell<ViajeVista>() {
            @Override
            protected void updateItem(ViajeVista v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null 
                    ? null 
                    : String.valueOf(v.getCodViaje()));
            }
        });
    }

      @FXML private void handleGuardar(ActionEvent e) {
        if (cbViajePasajero.getValue() == null ||
            tfNombre.getText().isBlank() ||
            tfApellido.getText().isBlank() ||
            tfTelefono.getText().isBlank() ||
            tfCedula.getText().isBlank() ||
            tfCorreo.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING,
                "Complete todos los campos obligatorios."
            ).showAndWait();
            return;
        }

        PasajeroVista pasajero = new PasajeroVista(
            0,
            cbViajePasajero.getValue().getCodViaje(),
            tfNombre.getText(),
            tfApellido.getText(),
            tfTelefono.getText(),
            tfCedula.getText(),
            tfCorreo.getText()
        );

        try {
            int id = new PasajeroProcedimientoService()
                        .crearPasajero(pasajero, ciudadUsuario);
            this.newCodPasajero = id;
            this.saved = true;
            ((Stage) btnGuardar.getScene().getWindow()).close();
        } catch (Exception ex) {
                   // 1) Imprime la traza completa en consola
        ex.printStackTrace();

        // 2) Extrae el mensaje real del SQL (causa de la RuntimeException)
        String detalle = ex.getCause() != null
            ? ex.getCause().getMessage()
            : ex.getMessage();

        // 3) Muéstralo en la alerta
        new Alert(Alert.AlertType.ERROR,
            "Error al guardar pasajero:\n" + detalle
        ).showAndWait();




        }
    }


    @FXML
    private void handleCancelar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() { return saved; }
    public int getNewCodPasajero() { return newCodPasajero; }
}
