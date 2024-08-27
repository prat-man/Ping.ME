package in.pratanumandal.pingme.components;

import in.pratanumandal.pingme.FXApplication;
import io.github.gleidson28.AvatarType;
import io.github.gleidson28.GNAvatarView;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Avatar extends GNAvatarView {

    private final List<AvatarListener> listeners;

    public Avatar() {
        listeners = new ArrayList<>();

        this.setType(AvatarType.CIRCLE);

        for (Node node : this.getChildrenUnmodifiable()) {
            if (node instanceof Circle circle) {
                circle.setCursor(Cursor.HAND);
                circle.hoverProperty().addListener((obs, oldVal, newVal) ->
                        this.setStroke(newVal ? Color.gray(0.3) : Color.WHITE));
                circle.setOnMousePressed(mouseEvent -> {
                    for (AvatarListener listener : listeners) {
                        listener.onAction();
                    }
                });
            }
        }
    }

    public void addListener(AvatarListener listener) {
        listeners.add(listener);
    }

    public void setImage(int index) {
        try (InputStream inputStream = FXApplication.class.getResourceAsStream("/images/avatars/" + index + ".png")) {
            assert inputStream != null;
            Image image = new Image(inputStream);
            super.setImage(image);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void randomImage() {
        int index = new Random().nextInt(12);
        this.setImage(index);
    }

    @FunctionalInterface
    public interface AvatarListener {

        void onAction();

    }

}
