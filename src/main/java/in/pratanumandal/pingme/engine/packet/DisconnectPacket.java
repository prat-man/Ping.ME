package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.AuthenticatedUser;
import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.state.ChatState;

public class DisconnectPacket implements Packet {

    private final AuthenticatedUser user;

    public DisconnectPacket() {
        this(ChatState.getInstance().getCurrentUser());
    }

    public DisconnectPacket(User user) {
        this.user = new AuthenticatedUser(user);
    }

    public User getUser() {
        return user.getUser();
    }

    @Override
    public PacketType getType() {
        return PacketType.DISCONNECT;
    }

}
