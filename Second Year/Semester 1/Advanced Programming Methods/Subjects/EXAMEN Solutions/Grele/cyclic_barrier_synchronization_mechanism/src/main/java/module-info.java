module com.example.cyclic_barrier_synchronization_mechanism {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cyclic_barrier_synchronization_mechanism to javafx.fxml;
    exports com.example.cyclic_barrier_synchronization_mechanism;
    exports com.example.cyclic_barrier_synchronization_mechanism.Controller;
    opens com.example.cyclic_barrier_synchronization_mechanism.Controller to javafx.fxml;
}