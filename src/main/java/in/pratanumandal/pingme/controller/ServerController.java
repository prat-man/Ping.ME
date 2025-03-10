package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.ServerLog;
import in.pratanumandal.pingme.engine.server.Server;
import in.pratanumandal.pingme.state.PrimaryStage;
import in.pratanumandal.pingme.state.ServerLogs;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Comparator;

public class ServerController {

    @FXML private TextField publicIPAddress;
    @FXML private ComboBox<String> localIPAddress;
    @FXML private TextField serverPort;

    @FXML private TableView<ServerLog> logsTable;
    @FXML private TableColumn<ServerLog, ServerLog.Channel> channelColumn;
    @FXML private TableColumn<ServerLog, String> timestampColumn;
    @FXML private TableColumn<ServerLog, String> usernameColumn;
    @FXML private TableColumn<ServerLog, String> ipAddressColumn;
    @FXML private TableColumn<ServerLog, String> packetTypeColumn;

    @FXML private LobbyController lobbyController;

    public void connect(int port) throws IOException {
        ServerLogs.initialize();

        Server server = new Server(port);
        server.start();

        lobbyController.setServer(server);

        PrimaryStage.getInstance().getStage().setOnCloseRequest(event -> server.disconnect());
        PrimaryStage.getInstance().getStage().setTitle(Constants.APP_NAME + " (Server)");

        publicIPAddress.setText(server.getPublicIPAddress());
        localIPAddress.getItems().addAll(server.getLocalIPAddresses());
        localIPAddress.getSelectionModel().selectFirst();
        serverPort.setText(server.getPort());

        SortedList<ServerLog> sortedList = new SortedList<>(ServerLogs.getInstance().getLogs(), Comparator.naturalOrder());
        logsTable.setItems(sortedList);

        channelColumn.setCellValueFactory(new PropertyValueFactory<>("channel"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("IPAddress"));
        packetTypeColumn.setCellValueFactory(new PropertyValueFactory<>("packetType"));

        channelColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(ServerLog.Channel channel, boolean empty) {
                super.updateItem(channel, empty);
                if (empty || channel == null) {
                    setGraphic(null);
                } else {
                    FontIcon icon = switch (channel) {
                        case INCOMING -> new FontIcon("fas-arrow-down");
                        case OUTGOING -> new FontIcon("fas-arrow-up");
                    };
                    icon.getStyleClass().add(channel.name().toLowerCase());
                    setGraphic(icon);
                }
            }
        });
    }

    @FXML
    private void copyPublicIPAddress() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(publicIPAddress.getText());
        clipboard.setContent(content);
    }

    @FXML
    private void copyLocalIPAddress() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(localIPAddress.getSelectionModel().getSelectedItem());
        clipboard.setContent(content);
    }

    @FXML
    private void copyServerPort() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(serverPort.getText());
        clipboard.setContent(content);
    }

}
