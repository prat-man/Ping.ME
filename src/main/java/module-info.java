module in.pratanumandal.pingme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.fxmisc.flowless;

    opens in.pratanumandal.pingme to javafx.fxml;
    opens in.pratanumandal.pingme.components to javafx.fxml;
    opens in.pratanumandal.pingme.controller to javafx.fxml;

    exports in.pratanumandal.pingme;
    exports in.pratanumandal.pingme.components;
    exports in.pratanumandal.pingme.controller;
    exports in.pratanumandal.pingme.engine;
    exports in.pratanumandal.pingme.engine.server;
    exports in.pratanumandal.pingme.common;
    opens in.pratanumandal.pingme.common to javafx.fxml;
}
