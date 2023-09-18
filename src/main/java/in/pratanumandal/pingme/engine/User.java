package in.pratanumandal.pingme.engine;

import in.pratanumandal.pingme.common.ColorSpace;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.UUID;

public class User {

    private static final ColorSpace colorSpace = new ColorSpace();

    private UUID id;
    private String name;
    private Color color;

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.color = colorSpace.generateColor();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getHexColor() {
        return "#" + Integer.toHexString(color.hashCode());
    }

    public boolean isCurrentUser() {
        return this.equals(State.getInstance().getCurrentUser());
    }

    public boolean isHost() {
        return this.equals(State.getInstance().getHost());
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
