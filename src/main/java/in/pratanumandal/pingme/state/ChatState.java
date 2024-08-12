package in.pratanumandal.pingme.state;

import in.pratanumandal.pingme.engine.entity.Message;
import in.pratanumandal.pingme.engine.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatState {

    private static ChatState instance;

    private User currentUser;
    private ObservableList<User> lobbyList;
    private ObservableList<Message> messageList;

    private ChatState() {
        lobbyList = FXCollections.observableArrayList();
        messageList = FXCollections.observableArrayList();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ObservableList<User> getLobbyList() {
        return lobbyList;
    }

    public ObservableList<Message> getMessageList() {
        return messageList;
    }

    public static void initialize() {
        if (instance != null) {
            throw new RuntimeException(ChatState.class.getSimpleName() + " is already initialized");
        }
        instance = new ChatState();
    }

    public static ChatState getInstance() {
        if (instance == null) {
            throw new RuntimeException(PrimaryStage.class.getSimpleName() + " is not initialized");
        }
        return instance;
    }

}
