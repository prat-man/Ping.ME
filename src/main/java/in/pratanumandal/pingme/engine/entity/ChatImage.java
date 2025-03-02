package in.pratanumandal.pingme.engine.entity;

import javafx.scene.image.*;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class ChatImage implements Serializable {

    private final int width;
    private final int height;
    private final byte[] imageBytes;

    public ChatImage(Image image) {
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        PixelReader pixelReader = image.getPixelReader();
        imageBytes = new byte[width * height * 4];
        WritablePixelFormat<ByteBuffer> pixelFormat = WritablePixelFormat.getByteBgraInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, imageBytes, 0, width * 4);
    }

    public Image getImage() {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        WritablePixelFormat<ByteBuffer> pixelFormat = WritablePixelFormat.getByteBgraInstance();
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, imageBytes, 0, width * 4);
        return image;
    }

    public byte[] getBytes() {
        return imageBytes;
    }

}
