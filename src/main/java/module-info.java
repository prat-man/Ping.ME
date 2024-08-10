module in.pratanumandal.pingme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens in.pratanumandal.pingme to javafx.fxml;
    opens in.pratanumandal.pingme.components to javafx.fxml;
    opens in.pratanumandal.pingme.controller to javafx.fxml;

    exports in.pratanumandal.pingme;
    exports in.pratanumandal.pingme.components;
    exports in.pratanumandal.pingme.controller;
    exports in.pratanumandal.pingme.engine;
    exports in.pratanumandal.pingme.engine.server;
}
