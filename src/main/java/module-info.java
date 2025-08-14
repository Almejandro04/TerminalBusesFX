module com.mycompany.terminalbusesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
     // Abre tu capa de DAL para que PropertyValueFactory pueda acceder por reflexión
    opens com.mycompany.terminalbusesDAL to javafx.base;
    
    opens com.mycompany.terminalbusesfx to javafx.fxml;
    exports com.mycompany.terminalbusesfx;
    exports com.mycompany.terminalbusesDAL;
}
