module com.example.repeat_until {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.repeat_until to javafx.fxml;
    exports com.example.repeat_until;
    exports com.example.repeat_until.Controller;
    opens com.example.repeat_until.Controller to javafx.fxml;
}