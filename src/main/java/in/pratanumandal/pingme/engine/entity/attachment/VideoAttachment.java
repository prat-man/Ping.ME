package in.pratanumandal.pingme.engine.entity.attachment;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.common.Utils;
import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VideoAttachment extends Attachment {

    protected transient Path cachedMP4;
    protected final ChatImage thumbnail;

    public VideoAttachment(Path path) throws IOException, EncoderException {
        super(path, AttachmentType.VIDEO);

        // probe media type
        MultimediaObject obj = new MultimediaObject(path.toFile());
        MultimediaInfo info = obj.getInfo();
        if (info.getVideo() == null) {
            throw new RuntimeException("Not a video file");
        }

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

    private void convert() throws IOException {
        if (cachedMP4 != null && Files.exists(cachedMP4)) {
            return;
        }
        cachedMP4 = Utils.convertMedia(getFileName(), payload);
    }

    @Override
    public byte[] getBytes() {
        return payload;
    }

    @Override
    public Image getThumbnail() {
        return thumbnail.getImage();
    }

    public Media getMedia() {
        try {
            this.convert();
            return new Media(cachedMP4.toUri().toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
