package in.pratanumandal.pingme.engine.entity.attachment;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

public class ImageAttachment extends Attachment {

    private final ChatImage payload;
    private final ChatImage thumbnail;
    private final String format;

    public ImageAttachment(Path path) throws IOException {
        super(path, AttachmentType.IMAGE);

        // load image
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            Image image = new Image(fis);
            payload = new ChatImage(image);
        }

        // load thumbnail
        BufferedImage bufferedImage = Thumbnails.of(path.toFile())
                .forceSize(Constants.THUMBNAIL_SIZE, Constants.THUMBNAIL_SIZE)
                .asBufferedImage();
        thumbnail = new ChatImage(SwingFXUtils.toFXImage(bufferedImage, null));

        // load format
        try (ImageInputStream input = ImageIO.createImageInputStream(path.toFile())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (readers.hasNext()) {
                format = readers.next().getFormatName();
            }
            else {
                format = "png";
            }
        }
    }

    @Override
    public byte[] getBytes() {
        return payload.getBytes();
    }

    @Override
    public Image getThumbnail() {
        return thumbnail.getImage();
    }

    @Override
    public void write(Path path) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(payload.getImage(), null);
        ImageIO.write(bufferedImage, format, path.toFile());
    }

    public Image getImage() {
        return payload.getImage();
    }

}
