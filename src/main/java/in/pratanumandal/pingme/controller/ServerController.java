package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.state.PrimaryStage;
import in.pratanumandal.pingme.engine.server.Server;
import javafx.fxml.FXML;

import java.io.IOException;

public class ServerController {

    @FXML private LobbyController lobbyController;

    public void connect(int port) {
        try {
            Server server = new Server(port);
            server.start();

            lobbyController.setServer(server);

            PrimaryStage.getInstance().getStage().setOnCloseRequest(event -> server.disconnect());
            PrimaryStage.getInstance().getStage().setTitle(Constants.APP_NAME + " (Server)");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
