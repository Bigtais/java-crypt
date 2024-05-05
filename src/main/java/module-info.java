module cryptography.javacrypt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens cryptography.javacrypt to javafx.fxml;
    opens cryptography.javacrypt.controllers;

    exports cryptography.javacrypt;
    exports cryptography.javacrypt.controllers;
    exports cryptography.javacrypt.services;
}