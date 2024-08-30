package in.pratanumandal.pingme.common;

import in.pratanumandal.pingme.FXApplication;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.Objects;
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

    public static Image loadImage(String path) {
        return new Image(Objects.requireNonNull(FXApplication.class.getResourceAsStream(path)));
    }

}
