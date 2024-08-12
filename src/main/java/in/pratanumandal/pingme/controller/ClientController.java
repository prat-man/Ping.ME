package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.state.PrimaryStage;
import in.pratanumandal.pingme.engine.client.Client;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.InetAddress;

public class ClientController {

    @FXML private ChatController chatController;

    private Client client;

    public void connect(String name, InetAddress address, int port) {
        try {
            client = new Client(name, address, port);
            client.start();

            client.connect();

            chatController.setClient(client);

            PrimaryStage.getInstance().getStage().setOnCloseRequest(event -> client.disconnect());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
