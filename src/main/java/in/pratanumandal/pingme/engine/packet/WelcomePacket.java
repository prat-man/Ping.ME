package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.User;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class WelcomePacket implements Packet {

    private User currentUser;
    private List<User> lobbyList;
    private PublicKey publicKey;

    public WelcomePacket(User currentUser, List<User> lobbyList, PublicKey publicKey) {
        this.currentUser = currentUser;
        this.lobbyList = new ArrayList<>(lobbyList);
        this.publicKey = publicKey;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getLobbyList() {
        return lobbyList;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public PacketType getType() {
        return PacketType.WELCOME;
    }

}
