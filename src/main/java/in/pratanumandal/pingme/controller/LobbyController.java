package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.components.UserListCell;
import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.server.Server;
import in.pratanumandal.pingme.state.ChatState;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.concurrent.atomic.AtomicReference;

public class LobbyController {

    @FXML private ListView<User> lobby;

    @FXML private Label connectedUsers;

    private final AtomicReference<Server> server = new AtomicReference<>();

    @FXML
    protected void initialize() {
        SortedList<User> sortedLobbyList = new SortedList<>(ChatState.getInstance().getLobbyList(), (user1, user2) -> {
            if (user1.isCurrentUser()) return -1;
            if (user2.isCurrentUser()) return 1;
            return user1.getName().compareTo(user2.getName());
        });
        ListProperty<User> lobbyListProperty = new SimpleListProperty<>(sortedLobbyList);

        lobby.itemsProperty().bind(lobbyListProperty);

        lobby.setCellFactory(view -> new UserListCell(server));

        lobby.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                lobby.getSelectionModel().clearSelection();
            }
        });

        connectedUsers.textProperty().bind(lobbyListProperty.sizeProperty().asString());
    }

    public void setServer(Server server) {
        this.server.set(server);
    }

}
