package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.entity.attachment.Attachment;
import in.pratanumandal.pingme.engine.entity.attachment.ImageAttachment;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ViewerController {

    @FXML private VBox viewer;

    @FXML private ButtonBar buttons;

    @FXML private ImageView imageView;

    private Attachment attachment;

    @FXML
    private void initialize() {
        imageView.fitWidthProperty().bind(viewer.widthProperty());
        imageView.fitHeightProperty().bind(viewer.heightProperty().subtract(buttons.heightProperty()));
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;

        if (attachment.getType() == Attachment.AttachmentType.IMAGE) {
            imageView.setImage(((ImageAttachment) attachment).getImage());
        }
    }

    @FXML
    private void download() {
        if (attachment.getType() == Attachment.AttachmentType.IMAGE) {
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
    }

}
