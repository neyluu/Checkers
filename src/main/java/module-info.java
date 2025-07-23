module checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.desktop;
    requires java.rmi;
    requires java.logging;

    opens checkers to javafx.fxml;
    exports checkers;
    exports checkers.logging;
    opens checkers.logging to javafx.fxml;
}