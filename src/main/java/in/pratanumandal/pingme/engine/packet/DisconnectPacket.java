package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.ChatState;
import in.pratanumandal.pingme.engine.User;

public class DisconnectPacket implements Packet {

    private User user;

    public DisconnectPacket() {
        this(ChatState.getInstance().getCurrentUser());
    }

    public DisconnectPacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public PacketType getType() {
        return PacketType.DISCONNECT;
    }

}
