package in.pratanumandal.pingme.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.InetAddress;

public class MainController implements ModeController.ModeListener {

    @FXML private BorderPane container;

    @FXML
    protected void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/mode.fxml"));
        Node root = loader.load();
        container.setCenter(root);

        ModeController controller = loader.getController();
        controller.addListener(this);
    }

    @Override
    public void server(int port) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/server.fxml"));
            Node root = loader.load();
            container.setCenter(root);

            ServerController controller = loader.getController();
            controller.connect(port);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void client(String name, Image avatar, InetAddress address, int port) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/client.fxml"));
            Node root = loader.load();
            container.setCenter(root);

            ClientController controller = loader.getController();
            controller.connect(name, avatar, address, port);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
