package in.pratanumandal.pingme.engine.packet;

public class JoinPacket implements Packet {

    private String name;

    public JoinPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public PacketType getType() {
        return PacketType.JOIN;
    }

}
