package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.AuthenticatedUser;
import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.state.ChatState;

public class RemovePacket implements Packet {

    private final AuthenticatedUser user;

    public RemovePacket() {
        this(ChatState.getInstance().getCurrentUser());
    }

    public RemovePacket(User user) {
        this.user = new AuthenticatedUser(user);
    }

    public User getUser() {
        return user.getUser();
    }

    @Override
    public PacketType getType() {
        return PacketType.REMOVE;
    }

}
