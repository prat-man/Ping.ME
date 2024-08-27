package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.components.Avatar;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

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
