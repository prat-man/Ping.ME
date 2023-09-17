package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.Message;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageController {

    @FXML private HBox container;
    @FXML private VBox wrapper;

    @FXML private Label sender;
    @FXML private Label message;
    @FXML private Label timestamp;

    @FXML
    protected void initialize() {
    }

    public void setMessage(Message message) {
        if (message.getUser() == null) {
            this.sender.setText(null);
            this.sender.setVisible(false);
            this.sender.setManaged(false);

            this.container.setAlignment(Pos.TOP_RIGHT);

            this.wrapper.getStyleClass().add("self");
        }
        else {
            this.sender.setText("~ " + message.getUser().getName());
            this.sender.setStyle("-fx-text-fill: " + message.getUser().getHexColor());
            this.sender.setVisible(true);
            this.sender.setManaged(true);

            this.container.setAlignment(Pos.TOP_LEFT);

            this.wrapper.getStyleClass().removeAll("self");
            this.wrapper.setStyle("-fx-border-color: " + message.getUser().getHexColor());
        }

        this.message.setText(message.getMessage());

        this.timestamp.setText(message.getTimestampString());
    }

}
