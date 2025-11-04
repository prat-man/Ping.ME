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

    private final ChatImage thumbnail;

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

    private byte[] convert() throws IOException {
        // Temporary target file
        Path tempPath = Files.createTempFile("audio-", ".mp3");

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
            encoder.encode(new MultimediaObject(path.toFile()), tempPath.toFile(), attrs);
        } catch (EncoderException e) {
            throw new IOException(e);
        }

        // Read MP3 bytes into memory
        byte[] bytes = Files.readAllBytes(tempPath);

        // Delete temp file
        Files.delete(tempPath);

        return bytes;
    }

    public void load() throws IOException {
        this.fileName = path.getFileName().toString() + ".mp3";
        this.payload = this.convert();
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
            Path tempFile = Files.createTempFile(FilenameUtils.getBaseName(getFileName()), FilenameUtils.getExtension(getFileName()));
            Files.write(tempFile, payload);
            return new Media(tempFile.toUri().toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
