package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.components.UserListCell;
import in.pratanumandal.pingme.engine.User;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.concurrent.atomic.AtomicBoolean;

public class LobbyController extends AbstractController {

    @FXML private ListView<User> lobby;

    @FXML private Label connectedUsers;

    private AtomicBoolean server;

    @FXML
    protected void initialize() {
        server = new AtomicBoolean(false);

        SortedList<User> sortedList = new SortedList<>(chatState.getLobbyList(), (user1, user2) -> {
            if (user1.isCurrentUser()) return -1;
            if (user2.isCurrentUser()) return 1;
            return user1.getName().compareTo(user2.getName());
        });
        ListProperty<User> lobbyListProperty = new SimpleListProperty<>(sortedList);

        lobby.itemsProperty().bind(lobbyListProperty);

        lobby.setCellFactory(view -> new UserListCell(server));

        lobby.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                lobby.getSelectionModel().clearSelection();
            }
        });

        connectedUsers.textProperty().bind(lobbyListProperty.sizeProperty().asString());
    }

    public void setServer(boolean server) {
        this.server.set(server);
    }

}
