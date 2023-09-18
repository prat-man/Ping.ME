package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.Message;
import in.pratanumandal.pingme.engine.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.util.UUID;

public class MainController extends AbstractController {

    @FXML private SplitPane container;

    private ChatController chatController;
    private LobbyController lobbyController;

    @FXML
    protected void initialize() throws IOException {
        FXMLLoader loader;
        Node root;

        loader = new FXMLLoader(MainController.class.getResource("/fxml/chat.fxml"));
        root = loader.load();
        container.getItems().add(root);
        chatController = loader.getController();

        loader = new FXMLLoader(MainController.class.getResource("/fxml/lobby.fxml"));
        root = loader.load();
        container.getItems().add(root);
        SplitPane.setResizableWithParent(root, false);
        lobbyController = loader.getController();

        // TODO: Remove debug
        User user = new User(UUID.randomUUID(), "Pratanu");
        state.getLobbyList().add(user);
        state.setCurrentUser(user);
        state.setHost(user);

        state.getLobbyList().add(new User(UUID.randomUUID(), "Rishi"));
        state.getLobbyList().add(new User(UUID.randomUUID(), "Kendu"));

        state.getMessageList().add(new Message(state.getLobbyList().get(0), "Hello World!\nThis is a test."));
        state.getMessageList().add(new Message(state.getLobbyList().get(1), "Yo wassup?"));
        state.getMessageList().add(new Message(state.getLobbyList().get(0), "All good!"));
        state.getMessageList().add(new Message(state.getLobbyList().get(2), "Come as you are."));
        state.getMessageList().add(new Message(state.getLobbyList().get(1), "Quoth the raven, nevermore."));
        state.getMessageList().add(new Message(state.getLobbyList().get(1), "Yo wassup?"));
        state.getMessageList().add(new Message(state.getLobbyList().get(0), "Hello World!\nThis is a test."));

        state.getLobbyList().add(new User(UUID.randomUUID(), "Ashif"));

        state.getMessageList().add(new Message(state.getLobbyList().get(2), "Come as you are."));
        state.getMessageList().add(new Message(state.getLobbyList().get(1), "Hello World!\nThis is a test."));
        state.getMessageList().add(new Message(state.getLobbyList().get(2), "Yo wassup?"));
        state.getMessageList().add(new Message(state.getLobbyList().get(0), "All good!"));
        state.getMessageList().add(new Message(state.getLobbyList().get(1), "Come as you are."));
    }

}
