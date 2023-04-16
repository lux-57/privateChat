module univ_lorraine.iut.java.PrivateChat {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens univ_lorraine.iut.java.privatechat.controller to javafx.fxml;
    opens univ_lorraine.iut.java.privatechat to javafx.fxml;
    exports univ_lorraine.iut.java.privatechat;

}
