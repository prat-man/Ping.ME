package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.client.Client;
import in.pratanumandal.pingme.state.PrimaryStage;
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
            PrimaryStage.getInstance().getStage().setTitle(Constants.APP_NAME + " (Client)");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
