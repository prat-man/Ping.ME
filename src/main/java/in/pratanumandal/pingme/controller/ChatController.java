package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.Message;
import in.pratanumandal.pingme.engine.User;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;

public class ChatController {

    @FXML private SplitPane container;

    @FXML private TextArea message;

    @FXML private Button send;

    @FXML private ListView lobby;

    @FXML private Label connectedUsers;

    @FXML private VBox chat;

    @FXML private ScrollPane chatScroll;

    private ObservableList<User> lobbyList = FXCollections.observableArrayList();
    private ListProperty<User> lobbyListProperty = new SimpleListProperty<>(lobbyList);

    @FXML
    protected void initialize() {
        Platform.runLater(() -> {
            BorderPane root = (BorderPane) container.getParent();
            container.prefWidthProperty().bind(root.widthProperty());
            container.prefHeightProperty().bind(root.heightProperty());
        });

        message.textProperty().addListener((obs, oldVal, newVal) -> send.setDisable(newVal.isEmpty()));

        lobby.itemsProperty().bind(lobbyListProperty);
        lobby.setCellFactory(view -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean b) {
                if (user != null) {
                    super.updateItem(user, b);
                    this.setText(user.getName());

                    if (this.isSelected()) {
                        this.setStyle(null);
                    }
                    else {
                        this.setStyle("-fx-text-fill: " + user.getHexColor());
                    }
                }
            }
        });
        lobby.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                lobby.getSelectionModel().clearSelection();
            }
        });

        connectedUsers.textProperty().bind(lobbyListProperty.sizeProperty().asString());

        chatScroll.needsLayoutProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                chatScroll.setVvalue(1.0);
            }
        });

        // TODO: Remove debug
        lobbyList.add(new User(UUID.randomUUID(), "Pratanu"));
        lobbyList.add(new User(UUID.randomUUID(), "Rishi"));
        lobbyList.add(new User(UUID.randomUUID(), "Kendu"));

        addChatMessage(lobbyList.get(0), "Hello World!\nThis is a test.");
        addChatMessage(lobbyList.get(1), "Yo wassup?");
        addChatMessage(null, "All good!");
        addChatMessage(lobbyList.get(2), "Come as you are.");
        addChatMessage(lobbyList.get(1), "Quoth the raven, nevermore.");
        addChatMessage(lobbyList.get(1), "Yo wassup?");
        addChatMessage(null, "Hello World!\nThis is a test.");
        addChatMessage(lobbyList.get(2), "Come as you are.");
        addChatMessage(lobbyList.get(1), "Hello World!\nThis is a test.");
        addChatMessage(lobbyList.get(2), "Yo wassup?");
        addChatMessage(null, "All good!");
        addChatMessage(lobbyList.get(1), "Come as you are.");
    }

    @FXML private void sendMessage() {
        addChatMessage(null, message.getText());
        message.clear();
        message.requestFocus();
    }

    private void addChatMessage(User user, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/fxml/message.fxml"));
            chat.getChildren().add(loader.load());

            MessageController controller = loader.getController();
            controller.setMessage(new Message(user, message));

            chatScroll.setVvalue(1.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
