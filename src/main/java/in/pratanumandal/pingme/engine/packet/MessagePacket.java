package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.Message;

public class MessagePacket implements Packet {

    private Message message;

    public MessagePacket(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public PacketType getType() {
        return PacketType.MESSAGE;
    }

}
