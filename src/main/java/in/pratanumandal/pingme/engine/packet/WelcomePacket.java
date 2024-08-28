package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.User;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class WelcomePacket implements Packet {

    private final User user;
    private final List<User> lobbyList;
    private final PublicKey publicKey;

    public WelcomePacket(User user, List<User> lobbyList, PublicKey publicKey) {
        this.user = user;
        this.lobbyList = new ArrayList<>(lobbyList);
        this.publicKey = publicKey;
    }

    public User getUser() {
        return user;
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
