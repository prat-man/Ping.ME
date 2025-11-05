package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.entity.attachment.Attachment;
import in.pratanumandal.pingme.engine.entity.attachment.AudioAttachment;
import in.pratanumandal.pingme.engine.entity.attachment.ImageAttachment;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ViewerController {

    @FXML private AnchorPane player;

    @FXML private HBox toolbar;

    @FXML private HBox controls;

    @FXML private ImageView imageView;

    @FXML private MediaView mediaView;

    private Attachment attachment;

    @FXML
    private void initialize() {
        imageView.fitWidthProperty().bind(player.widthProperty().subtract(100));
        imageView.fitHeightProperty().bind(player.heightProperty().subtract(toolbar.heightProperty()).subtract(controls.heightProperty()).subtract(100));
        mediaView.fitWidthProperty().bind(player.widthProperty().subtract(100));
        mediaView.fitHeightProperty().bind(player.heightProperty().subtract(toolbar.heightProperty()).subtract(controls.heightProperty()).subtract(100));

        player.managedProperty().bind(player.visibleProperty());
        toolbar.managedProperty().bind(toolbar.visibleProperty());
        controls.managedProperty().bind(controls.visibleProperty());
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;

        if (attachment.getType() == Attachment.AttachmentType.IMAGE) {
            imageView.setImage(((ImageAttachment) attachment).getImage());
            imageView.setVisible(true);
            mediaView.setVisible(false);
            controls.setVisible(false);
        }
        else if (attachment.getType() == Attachment.AttachmentType.AUDIO) {
            imageView.setImage(attachment.getThumbnail());
            imageView.setVisible(true);
            mediaView.setVisible(false);
            controls.setVisible(true);
        }
    }

    @FXML
    private void download() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(attachment.getFileName());
        File file = fileChooser.showSaveDialog(PrimaryStage.getInstance().getStage());

        if (file != null) {
            try {
                attachment.write(file.toPath());
            }
            catch (IOException e) {
                // TODO: Handle exceptions
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void show() {
        player.setVisible(true);
    }

    @FXML
    public void hide() {
        player.setVisible(false);
    }

}
