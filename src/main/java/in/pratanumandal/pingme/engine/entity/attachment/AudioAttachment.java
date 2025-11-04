package in.pratanumandal.pingme.engine.entity.attachment;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.commons.io.FilenameUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AudioAttachment extends Attachment {

    protected transient Path cachedMP3;
    protected final ChatImage thumbnail;

    public AudioAttachment(Path path) throws IOException {
        super(path, AttachmentType.AUDIO);

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
        // Check if already cached
        if (cachedMP3 != null && Files.exists(cachedMP3)) {
            return;
        }

        // Write payload to temporary file
        Path tempFile = Files.createTempFile(FilenameUtils.getBaseName(getFileName()), FilenameUtils.getExtension(getFileName()));
        Files.write(tempFile, payload);

        // Temporary target MP3 file
        Path tempFileMP3 = Files.createTempFile(FilenameUtils.getBaseName(getFileName()), ".mp3");

        // Audio attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000); // 128 kbps
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        // Encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);

        // Encode
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(tempFile.toFile()), tempFileMP3.toFile(), attrs);
        } catch (EncoderException e) {
            throw new IOException(e);
        }

        // Cleanup
        Files.delete(tempFile);
        tempFileMP3.toFile().deleteOnExit();

        // Cache converted file
        cachedMP3 = tempFileMP3;
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
            return new Media(cachedMP3.toUri().toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
