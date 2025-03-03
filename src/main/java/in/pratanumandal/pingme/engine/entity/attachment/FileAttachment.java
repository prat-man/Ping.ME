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

public class FileAttachment extends Attachment {

    private final byte[] payload;
    private final ChatImage thumbnail;

    public FileAttachment(Path path) throws IOException {
        super(path, AttachmentType.FILE);

        // load file
        this.payload = Files.readAllBytes(path);

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

    @Override
    public byte[] getBytes() {
        return payload;
    }

    @Override
    public Image getThumbnail() {
        return thumbnail.getImage();
    }

    @Override
    public void write(Path path) throws IOException {
        Files.write(path, payload);
    }

}
