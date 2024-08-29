package in.pratanumandal.pingme.state;

import in.pratanumandal.pingme.engine.entity.Message;
import in.pratanumandal.pingme.engine.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.UUID;

public class ChatState {

    private static ChatState instance;

    private User currentUser;
    private final ObservableList<User> lobbyList;
    private final ObservableList<Message> messageList;

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

    public User getUser(UUID userId) {
        if (userId == null) return null;
        Optional<User> optionalUser = lobbyList.stream().filter(user -> user.getId().equals(userId)).findFirst();
        return optionalUser.orElse(null);
    }

    public static void initialize() {
        instance = new ChatState();
    }

    public static ChatState getInstance() {
        if (instance == null) {
            throw new RuntimeException(ChatState.class.getSimpleName() + " is not initialized");
        }
        return instance;
    }

}
