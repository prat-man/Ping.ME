package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.common.Constants;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class Attachment implements Serializable {

    private transient final Path path;
    private final String fileName;
    private final AttachmentType attachmentType;
    private final Object attachment;
    private final ChatImage thumbnail;

    public Attachment(Path path) throws IOException {
        this.path = path;

        this.fileName = path.getFileName().toString();

        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            Image image = new Image(fis);

            if (image.isError()) {
                // attachment is a file
                attachmentType = AttachmentType.FILE;
                attachment = new ChatFile(path);

                // load file icon
                Icon icon = FileSystemView.getFileSystemView().getSystemIcon(path.toFile());

                BufferedImage bufferedImage = new BufferedImage(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = bufferedImage.createGraphics();
                icon.paintIcon(null, g, (Constants.THUMBNAIL_SIZE - icon.getIconWidth()) / 2, (Constants.THUMBNAIL_SIZE - icon.getIconHeight()) / 2);
                g.dispose();

                thumbnail = new ChatImage(SwingFXUtils.toFXImage(bufferedImage, null));
            }
            else {
                // attachment is an image
                attachmentType = AttachmentType.IMAGE;
                attachment = new ChatImage(image);

                // load image thumbnail
                BufferedImage bufferedImage = Thumbnails.of(path.toFile())
                        .forceSize(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE)
                        .asBufferedImage();

                thumbnail = new ChatImage(SwingFXUtils.toFXImage(bufferedImage, null));
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isImage() {
        return attachmentType == AttachmentType.IMAGE;
    }

    public boolean isFile() {
        return attachmentType == AttachmentType.FILE;
    }

    public ChatImage getImage() {
        return (ChatImage) attachment;
    }

    public ChatFile getFile() {
        return (ChatFile) attachment;
    }

    public Image getThumbnail() {
        return thumbnail.getImage();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        if (path == null && that.path == null) return false;
        return Objects.equals(path, that.path);
    }

    public enum AttachmentType {
        FILE,
        IMAGE
    }

}
