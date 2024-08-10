package in.pratanumandal.pingme.engine;

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

    public static ChatState getInstance() {
        if (instance == null) {
            instance = new ChatState();
        }
        return instance;
    }

}
