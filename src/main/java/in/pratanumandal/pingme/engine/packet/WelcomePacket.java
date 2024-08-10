package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.User;

import java.util.ArrayList;
import java.util.List;

public class WelcomePacket implements Packet {

    private User currentUser;
    private List<User> lobbyList;

    public WelcomePacket(User currentUser, List<User> lobbyList) {
        this.currentUser = currentUser;
        this.lobbyList = new ArrayList<>(lobbyList);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getLobbyList() {
        return lobbyList;
    }

    @Override
    public PacketType getType() {
        return PacketType.WELCOME;
    }

}
