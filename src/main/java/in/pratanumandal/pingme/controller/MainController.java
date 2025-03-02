package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.components.AppAlert;
import in.pratanumandal.pingme.state.ChatState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.InetAddress;

public class MainController implements ModeController.ModeListener {

    @FXML private BorderPane container;
    private Node modeRoot;

    @FXML
    protected void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/mode.fxml"));
        modeRoot = loader.load();
        container.setCenter(modeRoot);

        ModeController controller = loader.getController();
        controller.addListener(this);
    }

    @Override
    public void server(int port) {
        try {
            ChatState.initialize();

            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/server.fxml"));
            Node root = loader.load();
            container.setCenter(root);

            ServerController controller = loader.getController();
            controller.connect(port);
        }
        catch (Exception e) {
            AppAlert appAlert = new AppAlert(Alert.AlertType.ERROR,
                    "Server",
                    "Failed to create server");
            appAlert.show();

            container.setCenter(modeRoot);
        }
    }

    @Override
    public void client(String name, Image avatar, InetAddress address, int port) {
        try {
            ChatState.initialize();

            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/client.fxml"));
            Node root = loader.load();
            container.setCenter(root);

            ClientController controller = loader.getController();
            controller.connect(name, avatar, address, port);
        }
        catch (Exception e) {
            e.printStackTrace();

            AppAlert appAlert = new AppAlert(Alert.AlertType.ERROR,
                    "Client",
                    "Failed to join server");
            appAlert.show();

            container.setCenter(modeRoot);
        }
    }

}
