module com.example.conditional_assignment {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.conditional_assignment to javafx.fxml;
    exports com.example.conditional_assignment;
    exports com.example.conditional_assignment.Controller;
    opens com.example.conditional_assignment.Controller to javafx.fxml;
}