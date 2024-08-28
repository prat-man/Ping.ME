package in.pratanumandal.pingme.components;

import in.pratanumandal.pingme.controller.ChatController;
import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.server.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class UserListCell extends ListCell<User> {

    @FXML private HBox avatarBox;
    @FXML private Label name;
    @FXML private Label role;

    private final AtomicReference<Server> server;

    public UserListCell(AtomicReference<Server> server) {
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

                Avatar avatar = new Avatar();
                avatar.setImage(user.getAvatar());
                avatar.setPrefSize(30, 30);
                avatarBox.getChildren().add(avatar);

                name.setText(user.getName());
                if (this.isSelected()) name.setStyle(null);
                else name.setStyle("-fx-text-fill: " + user.getColor());

                if (user.isCurrentUser()) role.setText("you");
                else role.setText(null);

                this.setText(null);
                this.setGraphic(root);

                if (server.get() != null) {
                    ContextMenu menu = new ContextMenu();
                    this.setContextMenu(menu);

                    MenuItem removeItem = new MenuItem("Remove");
                    FontIcon fontIcon = new FontIcon("fas-ban");
                    removeItem.setGraphic(fontIcon);
                    menu.getItems().add(removeItem);

                    removeItem.setOnAction(event -> server.get().removeUser(user));
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
