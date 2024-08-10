package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.server.Server;
import javafx.fxml.FXML;

import java.io.IOException;

public class ServerController extends AbstractController {

    @FXML private LobbyController lobbyController;

    private Server server;

    @FXML
    protected void initialize() {
        lobbyController.setServer(true);
    }

    public void connect(int port) {
        try {
            server = new Server(port);
            server.start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
