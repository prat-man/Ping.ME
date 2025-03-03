package in.pratanumandal.pingme;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.state.FileHandler;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FileHandler.initialize(getHostServices());
        PrimaryStage.initialize(stage);

        stage.setTitle(Constants.APP_NAME);

        FXMLLoader fxmlLoader = new FXMLLoader(FXApplication.class.getResource("/fxml/main.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.sizeToScene();

        stage.getScene().getStylesheets().add(FXApplication.class.getResource("/css/style.css").toExternalForm());

        stage.show();
    }

    private static void loadFonts() {
        Font.loadFont(FXApplication.class.getResourceAsStream("/fonts/OpenSans-Regular.ttf"), 12);
        Font.loadFont(FXApplication.class.getResourceAsStream("/fonts/OpenSans-Bold.ttf"), 12);
        Font.loadFont(FXApplication.class.getResourceAsStream("/fonts/OpenSans-Italic.ttf"), 12);
        Font.loadFont(FXApplication.class.getResourceAsStream("/fonts/BAUHS93.ttf"), 12);
    }

    public static void main(String[] args) {
        loadFonts();

        launch();
    }

}
