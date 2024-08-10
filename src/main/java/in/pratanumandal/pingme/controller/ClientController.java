package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.client.Client;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.InetAddress;

public class ClientController extends AbstractController {

    @FXML private ChatController chatController;

    private Client client;

    public void connect(String name, InetAddress address, int port) {
        try {
            client = new Client(name, address, port);
            client.start();

            client.connect();

            chatController.setClient(client);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
