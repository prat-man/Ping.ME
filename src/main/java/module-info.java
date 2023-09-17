module in.pratanumandal.pingme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens in.pratanumandal.pingme to javafx.fxml;
    exports in.pratanumandal.pingme;
    exports in.pratanumandal.pingme.controller;
    opens in.pratanumandal.pingme.controller to javafx.fxml;
}