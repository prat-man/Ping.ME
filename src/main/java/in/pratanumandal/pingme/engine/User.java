package in.pratanumandal.pingme.engine;

import in.pratanumandal.pingme.common.ColorSpace;
import javafx.scene.paint.Color;

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

}
