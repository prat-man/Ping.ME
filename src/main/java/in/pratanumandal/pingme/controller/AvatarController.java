package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.FXApplication;
import in.pratanumandal.pingme.components.Avatar;
import io.github.gleidson28.AvatarType;
import io.github.gleidson28.GNAvatarView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.InputStream;

public class AvatarController {

    @FXML private GridPane avatarPane;

    private SimpleIntegerProperty selectedAvatar;

    @FXML
    private void initialize() {
        selectedAvatar = new SimpleIntegerProperty(-1);

        for (int i = 0; i < avatarPane.getRowCount(); i++) {
            for (int j = 0; j < avatarPane.getColumnCount(); j++) {
                int index = i * avatarPane.getColumnCount() + j;

                Avatar avatar = new Avatar();
                avatar.setImage(index);
                avatarPane.getChildren().add(avatar);

                GridPane.setRowIndex(avatar, i);
                GridPane.setColumnIndex(avatar, j);

                avatar.addListener(() -> selectedAvatar.set(index));
            }
        }
    }

    public int getSelectedAvatar() {
        return selectedAvatar.get();
    }

    public SimpleIntegerProperty selectedAvatarProperty() {
        return selectedAvatar;
    }

}
