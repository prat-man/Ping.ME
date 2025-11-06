package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.attachment.Attachment;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AttachmentController {

    @FXML private final int thumbnailSize = Constants.THUMBNAIL_SIZE;

    @FXML private StackPane pane;

    @FXML private StackPane close;

    @FXML private StackPane view;

    @FXML private StackPane play;

    @FXML private StackPane download;

    @FXML private ImageView fileIcon;

    private List<AttachmentListener> listeners;

    private AttachmentMode mode;

    private Attachment attachment;

    @FXML
    protected void initialize() {
        GaussianBlur blur = new GaussianBlur(10);

        listeners = new ArrayList<>();
        pane.hoverProperty().addListener((obs, oldVal, newVal) -> {
            close.setVisible(mode == AttachmentMode.CREATE && newVal);
            view.setVisible(mode == AttachmentMode.VIEW && attachment.getType() == Attachment.AttachmentType.IMAGE && newVal);
            play.setVisible(mode == AttachmentMode.VIEW && (attachment.getType() == Attachment.AttachmentType.AUDIO || attachment.getType() == Attachment.AttachmentType.VIDEO) && newVal);
            download.setVisible(mode == AttachmentMode.VIEW && attachment.getType() == Attachment.AttachmentType.UNKNOWN && newVal);

            fileIcon.setEffect(newVal ? blur : null);
        });

        Rectangle clip = new Rectangle(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        fileIcon.setClip(clip);
    }

    @FXML
    public void remove() {
        for (AttachmentListener listener : listeners) {
            listener.onAction();
        }
    }

    @FXML
    public void open() {
        for (AttachmentListener listener : listeners) {
            listener.onAction();
        }
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;

        if (attachment != null) {
            fileIcon.setImage(attachment.getThumbnail());

            Tooltip tooltip = new Tooltip(attachment.getFileName());
            tooltip.setMaxWidth(200);
            tooltip.setWrapText(true);
            tooltip.setShowDelay(Duration.millis(500));
            Tooltip.install(pane, tooltip);
        }
    }
    
    public void setMode(AttachmentMode mode) {
        this.mode = mode;
    }

    public void addListener(AttachmentListener listener) {
        listeners.add(listener);
    }

    @FunctionalInterface
    public interface AttachmentListener {

        void onAction();

    }

    public enum AttachmentMode {

        CREATE,
        VIEW

    }

}
