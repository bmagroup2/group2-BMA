module bmasec2.bmaapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens bmasec2.bmaapplication to javafx.fxml;
    exports bmasec2.bmaapplication;
}