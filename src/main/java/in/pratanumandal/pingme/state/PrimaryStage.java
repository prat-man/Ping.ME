package in.pratanumandal.pingme.state;

import in.pratanumandal.pingme.common.Utils;
import javafx.stage.Stage;

public class PrimaryStage {

    private static PrimaryStage instance;

    private final Stage stage;

    private PrimaryStage(Stage stage) {
        this.stage = stage;

        stage.getIcons().addAll(
                Utils.loadImage("/images/icon/icon_16.png"),
                Utils.loadImage("/images/icon/icon_24.png"),
                Utils.loadImage("/images/icon/icon_32.png"),
                Utils.loadImage("/images/icon/icon_64.png"),
                Utils.loadImage("/images/icon/icon_128.png"),
                Utils.loadImage("/images/icon/icon_256.png"));
    }

    public Stage getStage() {
        return stage;
    }

    public static void initialize(Stage stage) {
        if (instance != null) {
            throw new RuntimeException(PrimaryStage.class.getSimpleName() + " is already initialized");
        }
        instance = new PrimaryStage(stage);
    }

    public static PrimaryStage getInstance() {
        if (instance == null) {
            throw new RuntimeException(PrimaryStage.class.getSimpleName() + " is not initialized");
        }
        return instance;
    }

}
