// // package com.mycompany.terminalbusesfx;

// // import java.net.URL;
// // import java.util.ResourceBundle;

// // import com.mycompany.terminalbusesBLL.ConductorProcedimientoService;
// // import com.mycompany.terminalbusesDAL.ConductorVista;

// // import javafx.event.ActionEvent;
// // import javafx.fxml.FXML;
// // import javafx.fxml.Initializable;
// // import javafx.scene.Node;
// // import javafx.scene.control.Alert;
// // import javafx.scene.control.Button;
// // import javafx.scene.control.TextField;
// // import javafx.stage.Stage;

// // public class Ingreso_ConductorController implements Initializable {

// //     @FXML private TextField tfCodigo;
// //     @FXML private TextField tfNombre;
// //     @FXML private TextField tfApellido;
// //     @FXML private TextField tfLicencia;
// //     @FXML private Button btnGuardar;
// //     @FXML private Button btnCancelar;

// //     private String ciudadUsuario;
// //     private boolean saved = false;
// //     private int newCodConductor;

// //     /** 
// //      * Setter para inyectar la ciudad (Ibarra / Quito) 
// //      * antes de mostrar esta ventana. 
// //      */
// //     public void setCiudadUsuario(String ciudad) {
// //         this.ciudadUsuario = ciudad.toLowerCase();
// //     }

// //     @Override
// //     public void initialize(URL url, ResourceBundle rb) {
// //         // Nada especial por ahora
// //     }

// //     @FXML
// //     private void handleGuardar(ActionEvent e) {
// //         // 1) Validaciones
// //         if (tfNombre.getText().isBlank() ||
// //             tfApellido.getText().isBlank() ||
// //             tfLicencia.getText().isBlank()) {
// //             new Alert(Alert.AlertType.WARNING,
// //                 "Complete todos los campos obligatorios."
// //             ).showAndWait();
// //             return;
// //         }

// //         // 2) Construye el objeto ConductorVista
// //         ConductorVista conductor = new ConductorVista(
// //             tfCodigo.getText(),
// //             tfNombre.getText(),
// //             tfApellido.getText(),
// //             tfLicencia.getText()
// //         );

// //         // 3) Llama al Service
// //         try {
// //             int id = new ConductorProcedimientoService()
// //                           .crearConductor(conductor, ciudadUsuario);
// //             this.newCodConductor = id;
// //             this.saved = true;
// //             // 4) Cierra la ventana
// //             ((Stage)btnGuardar.getScene().getWindow()).close();

// //         } catch (Exception ex) {
// //             ex.printStackTrace();
// //             String detalle = ex.getCause() != null
// //                 ? ex.getCause().getMessage()
// //                 : ex.getMessage();
// //             new Alert(Alert.AlertType.ERROR,
// //                 "Error al guardar conductor:\n" + detalle
// //             ).showAndWait();
// //         }
// //     }

// //     @FXML
// //     private void handleCancelar(ActionEvent e) {
// //         ((Stage)((Node)e.getSource())
// //             .getScene()
// //             .getWindow()
// //         ).close();
// //     }

// //     // Para que el controlador padre sepa si guardó y cuál fue el nuevo código
// //     public boolean isSaved() {
// //         return saved;
// //     }
// //     public int getNewCodConductor() {
// //         return newCodConductor;
// //     }
// // }

// package com.mycompany.terminalbusesfx;

// import java.net.URL;
// import java.util.ResourceBundle;

// import com.mycompany.terminalbusesBLL.ConductorProcedimientoService;
// import com.mycompany.terminalbusesDAL.ConductorVista;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.scene.Node;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.stage.Stage;

// public class Ingreso_ConductorController implements Initializable {
// // mostramos pero no lo usamos al guardar
//     @FXML private TextField tfNombre;
//     @FXML private TextField tfApellido;
//     @FXML private TextField tfLicencia;
//     @FXML private Button    btnGuardar;
//     @FXML private Button    btnCancelar;

//     private String ciudadUsuario;
//     private boolean saved = false;
//     private int newCodConductor;

//     /**
//      * Inyectar la ciudad (ibarra/quito) antes de mostrar.
//      */
//     public void setCiudadUsuario(String ciudad) {
//         this.ciudadUsuario = ciudad.toLowerCase();
//     }

//     @Override
//     public void initialize(URL url, ResourceBundle rb) {
//         // Deshabilitamos tfCodigo en modo inserción
//         tfCodigo.setDisable(true);
//     }

//     @FXML
//     private void handleGuardar(ActionEvent e) {
//         // 1) Validaciones
//         if (tfNombre.getText().isBlank() ||
//             tfApellido.getText().isBlank() ||
//             tfLicencia.getText().isBlank()) {
//             new Alert(Alert.AlertType.WARNING,
//                 "Complete todos los campos obligatorios."
//             ).showAndWait();
//             return;
//         }

//         // 2) Calcula codTerminal según la ciudad
//         int codTerminal;
//         switch (ciudadUsuario) {
//             case "ibarra": codTerminal = 2; break;
//             case "quito":  codTerminal = 1; break;
//             default:
//                 new Alert(Alert.AlertType.ERROR,
//                     "Ciudad desconocida: " + ciudadUsuario
//                 ).showAndWait();
//                 return;
//         }

//         // 3) Construye ConductorVista (0 para que el SP genere el ID)
//         ConductorVista conductor = new ConductorVista(
//             0,                  // codConductor generado por BD
//             codTerminal,        // codTerminal según la sede
//             tfNombre.getText(),
//             tfApellido.getText(),
//             tfLicencia.getText()
//         );

//         // 4) Llama al Service/DAO
//         try {
//             int id = new ConductorProcedimientoService()
//                           .crearConductor(conductor, ciudadUsuario);
//             this.newCodConductor = id;
//             this.saved = true;
//             ((Stage)btnGuardar.getScene().getWindow()).close();

//         } catch (Exception ex) {
//             ex.printStackTrace();
//             String detalle = ex.getCause() != null
//                 ? ex.getCause().getMessage()
//                 : ex.getMessage();
//             new Alert(Alert.AlertType.ERROR,
//                 "Error al guardar conductor:\n" + detalle
//             ).showAndWait();
//         }
//     }

//     @FXML
//     private void handleCancelar(ActionEvent e) {
//         ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
//     }

//     public boolean isSaved() {
//         return saved;
//     }
//     public int getNewCodConductor() {
//         return newCodConductor;
//     }
// }

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

