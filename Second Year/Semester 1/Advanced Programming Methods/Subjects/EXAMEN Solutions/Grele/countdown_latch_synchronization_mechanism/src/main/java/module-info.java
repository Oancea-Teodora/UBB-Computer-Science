module com.example.countdown_latch_synchronization_mechanism {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.countdown_latch_synchronization_mechanism to javafx.fxml;
    exports com.example.countdown_latch_synchronization_mechanism;
    exports com.example.countdown_latch_synchronization_mechanism.Controller;
    opens com.example.countdown_latch_synchronization_mechanism.Controller to javafx.fxml;
}