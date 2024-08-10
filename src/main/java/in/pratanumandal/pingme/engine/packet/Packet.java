package in.pratanumandal.pingme.engine.packet;

import java.io.Serializable;

public interface Packet extends Serializable {

    PacketType getType();

}
