package in.pratanumandal.pingme.engine.packet;

import java.security.PublicKey;

public class JoinPacket implements Packet {

    private final String name;
    private final PublicKey publicKey;

    public JoinPacket(String name, PublicKey publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public PacketType getType() {
        return PacketType.JOIN;
    }

}
