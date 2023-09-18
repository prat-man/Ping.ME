package in.pratanumandal.pingme.engine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class State {

    private static State instance;

    private User currentUser;
    private User host;
    private ObservableList<User> lobbyList;
    private ObservableList<Message> messageList;

    private State() {
        lobbyList = FXCollections.observableArrayList();
        messageList = FXCollections.observableArrayList();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public ObservableList<User> getLobbyList() {
        return lobbyList;
    }

    public ObservableList<Message> getMessageList() {
        return messageList;
    }

    public static State getInstance() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

}
