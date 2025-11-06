package in.pratanumandal.pingme.common;

import in.pratanumandal.pingme.FXApplication;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Utils {

    public static void runAndWait(Runnable action) {
        if (action == null)
            throw new NullPointerException("action");

        // run synchronously on JavaFX thread
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        // queue on JavaFX thread and wait for completion
        final CountDownLatch doneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public static <T> T tryOrDefault(Callable<T>... attempts) {
        for (int i = 0; i < attempts.length - 1; i++) {
            try {
                return attempts[i].call();
            } catch (Exception ignored) {}
        }
        try {
            return attempts[attempts.length - 1].call();
        } catch (Exception e) {
            throw new RuntimeException("All attempts failed", e);
        }
    }

    public static Image loadImage(String path) {
        return new Image(Objects.requireNonNull(FXApplication.class.getResourceAsStream(path)));
    }

    public static Path convertMedia(String fileName, byte[] payload) throws IOException {
        // Write payload to temporary file
        Path tempFile = Files.createTempFile(FilenameUtils.getBaseName(fileName), "." + FilenameUtils.getExtension(fileName));
        Files.write(tempFile, payload);

        // Temporary target MP4 file
        Path tempFileMP4 = Files.createTempFile(FilenameUtils.getBaseName(fileName), ".mp4");

        MultimediaObject multimedia = new MultimediaObject(tempFile.toFile());

        boolean needsVideoEncoding = true;
        boolean needsAudioEncoding = true;

        try {
            MultimediaInfo info = multimedia.getInfo();

            // Check if video is already compatible
            if (info.getVideo() != null) {
                for (String encoding : Constants.VIDEO_ENCODINGS) {
                    if (info.getVideo().getDecoder().contains(encoding)) {
                        needsVideoEncoding = false;
                        break;
                    }
                }
            }

            // Check if audio is already compatible
            if (info.getAudio() != null) {
                for (String encoding : Constants.AUDIO_ENCODINGS) {
                    if (info.getAudio().getDecoder().contains(encoding)) {
                        needsAudioEncoding = false;
                        break;
                    }
                }
            }
        } catch (EncoderException ignored) {
            // If info fails, fallback to full encoding
        }

        // Setup video attributes
        VideoAttributes video = new VideoAttributes();
        if (needsVideoEncoding) {
            video.setCodec("libx264");
            video.setBitRate(800000);
            video.setFrameRate(30);
            video.setPreset("ultrafast");

        }
        else {
            video.setCodec("copy");
        }

        // Setup audio attributes
        AudioAttributes audio = new AudioAttributes();
        if (needsAudioEncoding) {
            audio.setCodec("aac");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);
        }
        else {
            audio.setCodec("copy");
        }

        // Setup encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setVideoAttributes(video);
        attrs.setAudioAttributes(audio);

        // Perform encoding / remux
        Encoder encoder = new Encoder();
        try {
            encoder.encode(multimedia, tempFileMP4.toFile(), attrs);
        } catch (EncoderException e) {
            for (String error : encoder.getUnhandledMessages()) {
                System.err.println(error);
            }
            throw new IOException(e);
        }

        // Cleanup
        Files.delete(tempFile);
        tempFileMP4.toFile().deleteOnExit();

        return tempFileMP4;
    }

}
