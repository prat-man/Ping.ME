package in.pratanumandal.pingme.state;

import javafx.stage.Stage;

public class PrimaryStage {

    private static PrimaryStage instance;

    private final Stage stage;

    private PrimaryStage(Stage stage) {
        this.stage = stage;
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
