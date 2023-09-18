package in.pratanumandal.pingme;

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
        stage.setTitle("Ping.ME");

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
    }

    public static void main(String[] args) {
        loadFonts();

        launch();
    }
}
