module checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.desktop;

    opens checkers to javafx.fxml;
    exports checkers;
}