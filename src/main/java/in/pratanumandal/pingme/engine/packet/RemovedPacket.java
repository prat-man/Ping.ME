package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.ChatState;
import in.pratanumandal.pingme.engine.User;

public class RemovedPacket implements Packet {

    private User user;

    public RemovedPacket() {
        this(ChatState.getInstance().getCurrentUser());
    }

    public RemovedPacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public PacketType getType() {
        return PacketType.REMOVED;
    }

}
