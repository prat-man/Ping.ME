package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.state.ChatState;

import java.io.Serializable;
import java.util.UUID;

public class AuthenticatedUser implements Serializable {

    private transient User user;
    private final UUID userId;

    public AuthenticatedUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public User getUser() {
        if (user == null) {
            user = ChatState.getInstance().getUser(userId);
        }
        return user;
    }

}
