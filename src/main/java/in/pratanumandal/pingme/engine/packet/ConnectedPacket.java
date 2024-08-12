package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.User;

public class ConnectedPacket implements Packet {

    private User user;

    public ConnectedPacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public PacketType getType() {
        return PacketType.CONNECTED;
    }

}
