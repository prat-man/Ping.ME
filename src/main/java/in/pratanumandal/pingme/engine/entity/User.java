package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.common.ColorSpace;
import in.pratanumandal.pingme.state.ChatState;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {

    private static final ColorSpace colorSpace = new ColorSpace();

    private final UUID id;
    private final String name;
    private final ChatImage avatar;
    private final String color;
    private final transient InetAddress address;
    private final transient int port;


    public User(String name, Image avatar, InetAddress address, int port) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.avatar = new ChatImage(avatar);
        this.color = colorSpace.generateColor();
        this.address = address;
        this.port = port;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getAvatar() {
        return avatar.getImage();
    }

    public String getColor() {
        return color;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
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
