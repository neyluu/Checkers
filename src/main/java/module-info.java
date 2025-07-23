module checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.desktop;
    requires org.slf4j;
    requires java.rmi;
    requires java.logging;

    opens checkers to javafx.fxml;
    exports checkers;
    exports checkers.logging;
    opens checkers.logging to javafx.fxml;
}