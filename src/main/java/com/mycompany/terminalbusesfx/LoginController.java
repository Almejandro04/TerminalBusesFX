/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.terminalbusesfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import java.io.IOException;


/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class LoginController implements Initializable {
    // 1) Inyecta el botón
    @FXML private Button btnIniciarSesion; 
     @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO// Crear un Alert de información
//        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
//        alerta.setTitle("Bienvenida");
//        alerta.setHeaderText(null);                    // sin cabecera
//        alerta.setContentText("¡Bienvenido al sistema!"); // tu mensaje
//
//        // Mostrar la alerta y esperar a que la cierren
//        alerta.showAndWait();
         // Para añadir el listener
        // btnIniciarSesion.setOnAction(this::handleIniciarSesion);
        
    }   
    // 2) Método que se ejecuta al pulsar el botón
    @FXML
    private void handleIniciarSesion(ActionEvent event) {
        
        
        String user = txtUser.getText().trim().toUpperCase();
        String pass = txtPass.getText().trim();

        // 1) Validar credenciales
        boolean ok = 
            (user.equals("ADMIN")  && pass.equals("ADMIN123"))  ||
            (user.equals("QUITO")  && pass.equals("QUITO123"))  ||
            (user.equals("IBARRA") && pass.equals("IBARRA123"));

        if (!ok) {
            new Alert(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos")
                .showAndWait();
            return;  // sale del método si falla
        }
        
//        String vista;
//         vista = "DetalleViajeFINAL.fxml";
//         // 3) Carga la nueva escena
//          try {
//        Parent root = FXMLLoader.load(getClass().getResource(vista));
//        Stage stage = (Stage) txtUser.getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//        
//        } catch (IOException e) {
//        // Imprime en consola y muestra un error al usuario
//        e.printStackTrace();
//        new Alert(Alert.AlertType.ERROR,
//            "No se pudo cargar la pantalla:\n" + e.getMessage()
//        ).showAndWait();
//       }
String vista = "DetalleViajeFINAL.fxml";
try {
    // 1) Prepara el loader apuntando al FXML
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource(vista)
    );
    // 2) Carga la interfaz
    Parent root = loader.load();
    // 3) Obtén el controlador y pásale el usuario
    DetalleViajeFINALController ctrl =loader.<DetalleViajeFINALController>getController();
    ctrl.setCurrentUser(user);
    // 4) Muestra la nueva escena
    Stage stage = (Stage) txtUser.getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
} catch (IOException e) {
    e.printStackTrace();
    new Alert(Alert.AlertType.ERROR,
        "No se pudo cargar la pantalla:\n" + e.getMessage()
    ).showAndWait();
}
//         
    }
    
}
