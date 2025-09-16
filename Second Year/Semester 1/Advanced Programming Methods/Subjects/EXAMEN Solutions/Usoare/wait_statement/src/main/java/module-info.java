module toy_interpreter.lab11_project {
    requires javafx.controls;
    requires javafx.fxml;

    opens toy_interpreter.lab11_project to javafx.fxml;
    exports toy_interpreter.lab11_project;
    exports toy_interpreter.lab11_project.Controller;
    opens toy_interpreter.lab11_project.Controller to javafx.fxml;
}