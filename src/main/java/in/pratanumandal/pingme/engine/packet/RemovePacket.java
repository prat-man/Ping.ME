package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.state.ChatState;

public class RemovePacket implements Packet {

    private User user;

    public RemovePacket() {
        this(ChatState.getInstance().getCurrentUser());
    }

    public RemovePacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public PacketType getType() {
        return PacketType.REMOVE;
    }

}
