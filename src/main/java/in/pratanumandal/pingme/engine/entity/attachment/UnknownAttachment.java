package in.pratanumandal.pingme.engine.entity.attachment;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnknownAttachment extends Attachment {

    private final ChatImage thumbnail;

    public UnknownAttachment(Path path) throws IOException {
        super(path, AttachmentType.UNKNOWN);

        // load thumbnail
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(path.toFile(), 32, 32);

        BufferedImage bufferedImage = new BufferedImage(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE);
        icon.paintIcon(null, g, (Constants.THUMBNAIL_SIZE - icon.getIconWidth()) / 2, (Constants.THUMBNAIL_SIZE - icon.getIconHeight()) / 2);
        g.dispose();

        thumbnail = new ChatImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }

    public void load() throws IOException {
        this.fileName = path.getFileName().toString();
        this.payload = Files.readAllBytes(path);
    }

    @Override
    public byte[] getBytes() {
        return payload;
    }

    @Override
    public Image getThumbnail() {
        return thumbnail.getImage();
    }

}
