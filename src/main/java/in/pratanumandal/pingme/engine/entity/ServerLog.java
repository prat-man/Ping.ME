package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.packet.Packet;

import java.time.LocalDateTime;

public class ServerLog implements Comparable<ServerLog> {

    private final Channel channel;
    private final LocalDateTime timestamp;
    private final User user;
    private final Packet packet;

    public ServerLog(Channel channel, User user, Packet packet) {
        this.timestamp = LocalDateTime.now();
        this.channel = channel;
        this.user = user;
        this.packet = packet;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getTimestamp() {
        return timestamp.format(Constants.DATE_TIME_FORMATTER);
    }

    public String getUsername() {
        if (user == null) return null;
        return user.getName();
    }

    public String getIPAddress() {
        if (user == null || user.getAddress() == null) return null;
        return user.getAddress().getHostAddress();
    }

    public String getPacketType() {
        if (packet == null || packet.getType() == null) return null;
        return packet.getType().name();
    }

    @Override
    public int compareTo(ServerLog other) {
        return other.timestamp.compareTo(this.timestamp);
    }


    public enum Channel {
        INCOMING,
        OUTGOING
    }

}
