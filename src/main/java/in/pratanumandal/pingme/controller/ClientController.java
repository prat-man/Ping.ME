package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.client.Client;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.InetAddress;

public class ClientController {

    @FXML private ChatController chatController;

    public void connect(String name, Image image, InetAddress address, int port) throws IOException {
        Client client = new Client(name, image, address, port);
        client.start();

        client.connect();

        chatController.setClient(client);

        PrimaryStage.getInstance().getStage().setOnCloseRequest(event -> client.disconnect());
        PrimaryStage.getInstance().getStage().setTitle(Constants.APP_NAME + " (Client)");
    }

}
