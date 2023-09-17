package in.pratanumandal.pingme.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML private BorderPane borderPane;

    @FXML
    protected void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/chat.fxml"));
        borderPane.setCenter(loader.load());
    }

}
