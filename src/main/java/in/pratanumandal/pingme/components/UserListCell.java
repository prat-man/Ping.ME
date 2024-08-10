package in.pratanumandal.pingme.components;

import in.pratanumandal.pingme.controller.ChatController;
import in.pratanumandal.pingme.engine.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserListCell extends ListCell<User> {

    @FXML private Label indicator;
    @FXML private Label name;
    @FXML private Label role;

    private AtomicBoolean server;

    public UserListCell(AtomicBoolean server) {
        this.server = server;
    }

    @Override
    protected void updateItem(User user, boolean b) {
        super.updateItem(user, b);

        if (user == null) {
            this.setText(null);
            this.setGraphic(null);
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/fxml/user-cell.fxml"));
                loader.setController(this);
                HBox root = loader.load();

                indicator.setStyle("-fx-text-fill: " + user.getColor());
                indicator.setVisible(!user.isCurrentUser());

                name.setText(user.getName());
                if (this.isSelected() || user.isCurrentUser()) name.setStyle(null);
                else name.setStyle("-fx-text-fill: " + user.getColor());

                if (user.isCurrentUser()) role.setText("you");
                else role.setText(null);

                this.setText(null);
                this.setGraphic(root);

                if (server.get()) {
                    ContextMenu menu = new ContextMenu();
                    this.setContextMenu(menu);

                    MenuItem removeItem = new MenuItem("Remove");
                    menu.getItems().add(removeItem);

                    removeItem.setOnAction(event -> {
                        // TODO: Remove the user
                    });
                }
                else {
                    this.setContextMenu(null);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
