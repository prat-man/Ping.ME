package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.components.AppAlert;
import in.pratanumandal.pingme.components.Avatar;
import in.pratanumandal.pingme.components.IntegerTextFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SegmentedButton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ModeController {

    @FXML private HBox container;
    @FXML private Separator separator;

    @FXML private SegmentedButton modeButton;

    @FXML private Label appName;
    @FXML private Label author;

    @FXML private VBox server;
    @FXML private TextField serverPort;

    @FXML private VBox client;
    @FXML private HBox avatarBox;
    @FXML private Avatar avatar;
    @FXML private TextField clientName;
    @FXML private TextField clientAddress;
    @FXML private TextField clientPort;
    @FXML private Button clientButton;

    private List<ModeListener> listeners;

    @FXML
    protected void initialize() {
        listeners = new ArrayList<>();

        separator.prefHeightProperty().bind(container.heightProperty());

        appName.setText(Constants.APP_NAME);
        author.setText(Constants.AUTHOR);

        modeButton.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
            else {
                boolean isServer = ((ToggleButton) newVal).getText().equals("Server");
                server.setVisible(isServer);
                client.setVisible(!isServer);
            }
        });

        server.managedProperty().bind(server.visibleProperty());
        client.managedProperty().bind(client.visibleProperty());

        avatar = new Avatar();
        avatar.randomImage();
        avatar.setPrefSize(100, 100);
        avatarBox.getChildren().add(avatar);

        avatar.addListener(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/avatar.fxml"));
                Node root = loader.load();

                PopOver popOver = new PopOver();
                popOver.setDetachable(false);
                popOver.setContentNode(root);
                popOver.show(avatar);

                AvatarController avatarController = loader.getController();
                avatarController.selectedAvatarProperty().addListener((obs, oldVal, newVal) -> {
                    popOver.hide();
                    avatar.setImage(newVal.intValue());
                });
            }
            catch (IOException ignored) { }
        });

        serverPort.setTextFormatter(new IntegerTextFormatter());
        clientPort.setTextFormatter(new IntegerTextFormatter());
    }

    public void addListener(ModeListener listener) {
        listeners.add(listener);
        clientName.textProperty().addListener((obs, oldVal, newVal) -> clientButton.setDisable(newVal.isBlank()));
    }

    @FXML
    private void server() {
        int port = 80;
        if (!serverPort.getText().isBlank()) {
            try {
                port = Integer.parseInt(serverPort.getText());
            }
            catch (NumberFormatException e) {
                AppAlert appAlert = new AppAlert(Alert.AlertType.ERROR,
                        "Server",
                        "Invalid port");
                appAlert.show();

                return;
            }
        }

        for (ModeListener listener : listeners) {
            listener.server(port);
        }
    }

    @FXML
    private void client() throws UnknownHostException {
        if (clientName.getText().isBlank()) {
            throw new RuntimeException();
        }
        String name = clientName.getText();

        Image image = avatar.getImage();

        InetAddress address = InetAddress.getLocalHost();
        if (!clientAddress.getText().isBlank()) {
            address = InetAddress.getByName(clientAddress.getText());
        }

        int port = 80;
        if (!clientPort.getText().isBlank()) {
            try {
                port = Integer.parseInt(clientPort.getText());
            }
            catch (NumberFormatException e) {
                AppAlert appAlert = new AppAlert(Alert.AlertType.ERROR,
                        "Client",
                        "Invalid port");
                appAlert.show();

                return;
            }
        }

        for (ModeListener listener : listeners) {
            listener.client(name, image, address, port);
        }
    }

    public interface ModeListener {

        void server(int port);

        void client(String name, Image image, InetAddress address, int port);

    }

}
