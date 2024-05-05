module cryptography.javacrypt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens cryptography.javacrypt to javafx.fxml;
    exports cryptography.javacrypt;
}