package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.Message;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MessageController extends AbstractController {

    @FXML private HBox container;
    @FXML private VBox wrapper;

    @FXML private Label sender;
    @FXML private Text message;
    @FXML private Label timestamp;

    @FXML
    protected void initialize() {
        container.managedProperty().bind(container.visibleProperty());
        sender.managedProperty().bind(sender.visibleProperty());
        timestamp.managedProperty().bind(timestamp.visibleProperty());

        container.setVisible(false);
    }

    public void setMessage(Message message) {
        if (message == null) {
            container.setVisible(false);
            return;
        }

        container.setVisible(true);

        if (message.getUser() == null) {
            sender.setText(null);
            sender.setVisible(false);
            timestamp.setVisible(false);

            container.setAlignment(Pos.CENTER);

            wrapper.getStyleClass().add("server");
        }
        else if (message.getUser().isCurrentUser()) {
            sender.setText(null);
            sender.setVisible(false);
            timestamp.setVisible(true);

            container.setAlignment(Pos.CENTER_RIGHT);

            wrapper.getStyleClass().add("self");
        }
        else {
            sender.setText("~ " + message.getUser().getName());
            sender.setStyle("-fx-text-fill: " + message.getUser().getColor());
            sender.setVisible(true);
            timestamp.setVisible(true);

            container.setAlignment(Pos.CENTER_LEFT);

            wrapper.getStyleClass().removeAll("self");
            wrapper.setStyle("-fx-border-color: " + message.getUser().getColor());
        }

        this.message.setText(message.getMessage());
        this.timestamp.setText(message.getTimestampString());
    }

}
