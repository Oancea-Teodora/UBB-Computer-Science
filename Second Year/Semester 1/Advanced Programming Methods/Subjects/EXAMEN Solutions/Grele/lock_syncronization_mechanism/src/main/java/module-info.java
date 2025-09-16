module com.example.lock_syncronization_mechanism {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lock_syncronization_mechanism to javafx.fxml;
    exports com.example.lock_syncronization_mechanism;
    exports com.example.lock_syncronization_mechanism.Controller;
    opens com.example.lock_syncronization_mechanism.Controller to javafx.fxml;
}