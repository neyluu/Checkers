module checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;

    opens checkers to javafx.fxml;
    exports checkers;
}