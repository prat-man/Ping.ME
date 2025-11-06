package in.pratanumandal.pingme.engine.entity.attachment;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ImageAttachment extends Attachment {

    protected final ChatImage thumbnail;

    public ImageAttachment(Path path) throws IOException {
        super(path, AttachmentType.IMAGE);

        // load thumbnail
        BufferedImage bufferedImage = Thumbnails.of(path.toFile())
                .size(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE)
                .crop(Positions.CENTER)
                .keepAspectRatio(true)
                .asBufferedImage();

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

    public Image getImage() {
        return new Image(new ByteArrayInputStream(payload));
    }

}
