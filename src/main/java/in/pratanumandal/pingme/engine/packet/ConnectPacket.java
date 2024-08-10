package in.pratanumandal.pingme.engine.packet;

public class ConnectPacket implements Packet {

    private String name;

    public ConnectPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public PacketType getType() {
        return PacketType.CONNECT;
    }

}
