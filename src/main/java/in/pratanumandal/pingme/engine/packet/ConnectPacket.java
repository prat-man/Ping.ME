package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.User;

public class ConnectPacket implements Packet {

    private final User user;

    public ConnectPacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public PacketType getType() {
        return PacketType.CONNECT;
    }

}
