module com.example.sleep {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.sleep to javafx.fxml;
    exports com.example.sleep;
    exports com.example.sleep.Controller;
    opens com.example.sleep.Controller to javafx.fxml;
}