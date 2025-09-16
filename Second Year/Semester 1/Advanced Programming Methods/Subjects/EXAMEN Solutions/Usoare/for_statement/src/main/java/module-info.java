module com.example.for_statement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.for_statement to javafx.fxml;
    exports com.example.for_statement;
    exports com.example.for_statement.Controller;
    opens com.example.for_statement.Controller to javafx.fxml;
}