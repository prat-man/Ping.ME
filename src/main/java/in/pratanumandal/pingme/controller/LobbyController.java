package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.components.UserListCell;
import in.pratanumandal.pingme.engine.User;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LobbyController extends AbstractController {

    @FXML private ListView lobby;

    @FXML private Label connectedUsers;

    private ListProperty<User> lobbyListProperty;

    @FXML
    protected void initialize() {
        SortedList<User> sortedList = new SortedList<>(state.getLobbyList(), (user1, user2) -> {
            if (user1.isCurrentUser()) return -1;
            if (user2.isCurrentUser()) return 1;
            return user1.getName().compareTo(user2.getName());
        });
        lobbyListProperty = new SimpleListProperty<>(sortedList);

        lobby.itemsProperty().bind(lobbyListProperty);

        lobby.setCellFactory(view -> new UserListCell());

        lobby.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                lobby.getSelectionModel().clearSelection();
            }
        });

        connectedUsers.textProperty().bind(lobbyListProperty.sizeProperty().asString());
    }

}
