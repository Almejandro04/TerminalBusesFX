package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.terminalbusesBLL.ConductorProcedimientoService;
import com.mycompany.terminalbusesDAL.ConductorVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Ingreso_ConductorController implements Initializable {

    @FXML private TextField tfCodigo;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellido;
    @FXML private TextField tfLicencia;
    @FXML private Button    btnGuardar;
    @FXML private Button    btnCancelar;

    private String ciudadUsuario;
    private boolean saved = false;
    private int newCodConductor;

    /** 
     * Inyecta la ciudad (ibarra/quito) antes de mostrar el formulario. 
     */
    public void setCiudadUsuario(String ciudad) {
        this.ciudadUsuario = ciudad.toLowerCase();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nada más
    }

    @FXML
    private void handleGuardar(ActionEvent e) {
        // 1) Validaciones
        if (tfNombre.getText().isBlank() ||
            tfApellido.getText().isBlank() ||
            tfLicencia.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING,
                "Complete todos los campos obligatorios."
            ).showAndWait();
            return;
        }

        // 2) Determina codTerminal según ciudadUsuario
        int codTerminal;
        switch (ciudadUsuario) {
            case "ibarra": codTerminal = 2; break;
            case "quito":  codTerminal = 1; break;
            default:
                new Alert(Alert.AlertType.ERROR,
                    "Ciudad desconocida: " + ciudadUsuario
                ).showAndWait();
                return;
        }

        // 3) Construye ConductorVista con codConductor = 0 (IDENTITY)
        ConductorVista conductor = new ConductorVista(
            0,              // codConductor (lo genera la BD)
            codTerminal,    
            tfNombre.getText(),
            tfApellido.getText(),
            tfLicencia.getText()
        );

        // 4) Inserta vía Service/DAO
        try {
            int id = new ConductorProcedimientoService()
                          .crearConductor(conductor, ciudadUsuario);
            this.newCodConductor = id;
            this.saved = true;
            ((Stage)btnGuardar.getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
            String detalle = ex.getCause() != null
                ? ex.getCause().getMessage()
                : ex.getMessage();
            new Alert(Alert.AlertType.ERROR,
                "Error al guardar conductor:\n" + detalle
            ).showAndWait();
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    public boolean isSaved() {
        return saved;
    }
    public int getNewCodConductor() {
        return newCodConductor;
    }
}

