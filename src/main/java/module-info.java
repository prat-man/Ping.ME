module in.pratanumandal.pingme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.fxmisc.flowless;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires GNAvatarView;

    opens in.pratanumandal.pingme to javafx.fxml;
    opens in.pratanumandal.pingme.controller to javafx.fxml;
    opens in.pratanumandal.pingme.components to javafx.fxml;

    exports in.pratanumandal.pingme;
    exports in.pratanumandal.pingme.controller;
    exports in.pratanumandal.pingme.components;
    exports in.pratanumandal.pingme.common;
    exports in.pratanumandal.pingme.state;
    exports in.pratanumandal.pingme.engine.server;
    exports in.pratanumandal.pingme.engine.client;
    exports in.pratanumandal.pingme.engine.packet;
    exports in.pratanumandal.pingme.engine.entity;
}
