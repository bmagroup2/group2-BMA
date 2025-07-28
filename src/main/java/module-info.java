module bmasec2.bmaapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens bmasec2.bmaapplication to javafx.fxml;
    exports bmasec2.bmaapplication;

    exports bmasec2.bmaapplication.shanin;
    exports bmasec2.bmaapplication.afifa;
    exports bmasec2.bmaapplication.zumar;
    exports bmasec2.bmaapplication.fatema;
    exports bmasec2.bmaapplication.system;

    opens bmasec2.bmaapplication.shanin to javafx.fxml;
    opens bmasec2.bmaapplication.afifa to javafx.fxml;
    opens bmasec2.bmaapplication.zumar to javafx.fxml;
    opens bmasec2.bmaapplication.fatema to javafx.fxml;
    opens bmasec2.bmaapplication.system to javafx.fxml;
}