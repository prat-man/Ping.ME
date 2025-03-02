package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.Attachment;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.coobird.thumbnailator.Thumbnails;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttachmentController {

    @FXML private final int thumbnailSize = Constants.THUMBNAIL_SIZE;

    @FXML private StackPane pane;

    @FXML private StackPane close;

    @FXML private StackPane open;

    @FXML private ImageView fileIcon;

    private List<AttachmentListener> listeners;

    private AttachmentMode mode;

    private Attachment attachment;

    @FXML
    protected void initialize() {
        listeners = new ArrayList<>();
        pane.hoverProperty().addListener((obs, oldVal, newVal) -> {
            close.setVisible(mode == AttachmentMode.CREATE && newVal);
            open.setVisible(mode == AttachmentMode.VIEW && newVal);
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
