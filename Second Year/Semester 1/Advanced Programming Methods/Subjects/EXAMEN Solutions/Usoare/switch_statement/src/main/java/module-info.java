module com.example.switch_statement {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.switch_statement to javafx.fxml;
    exports com.example.switch_statement;
    exports com.example.switch_statement.Controller;
    opens com.example.switch_statement.Controller to javafx.fxml;
}