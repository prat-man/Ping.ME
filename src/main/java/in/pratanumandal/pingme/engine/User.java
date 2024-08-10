package in.pratanumandal.pingme.engine;

import in.pratanumandal.pingme.common.ColorSpace;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {

    private static final ColorSpace colorSpace = new ColorSpace();

    private UUID id;
    private String name;
    private transient InetAddress address;
    private transient int port;
    private String color;

    public User(String name, InetAddress address, int port) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.port = port;
        this.color = colorSpace.generateColor();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getColor() {
        return color;
    }

    public boolean isCurrentUser() {
        return this.equals(ChatState.getInstance().getCurrentUser());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
