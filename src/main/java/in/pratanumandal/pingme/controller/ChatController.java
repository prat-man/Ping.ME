package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.Message;
import in.pratanumandal.pingme.engine.User;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatController extends AbstractController {

    @FXML private TextArea message;

    @FXML private Button send;

    @FXML private VBox chat;

    @FXML private ScrollPane chatScroll;

    private AtomicBoolean scrollToBottom;

    @FXML
    protected void initialize() {
        message.textProperty().addListener((obs, oldVal, newVal) -> send.setDisable(newVal.isEmpty()));

        state.getMessageList().addListener((ListChangeListener<? super Message>) change -> {
            change.next();
            List<Message> messageList = (List<Message>) change.getAddedSubList();
            for (Message message : messageList) {
                addChatMessage(message);
            }
        });

        state.getLobbyList().addListener((ListChangeListener<? super User>) change -> {
            change.next();
            List<User> userList = (List<User>) change.getAddedSubList();
            for (User user : userList) {
                Message message = new Message(null, user.getName() + " joined the chat");
                addChatMessage(message);
            }
        });

        scrollToBottom = new AtomicBoolean(false);
        chat.heightProperty().addListener((observableValue, number, t1) -> {
            if (scrollToBottom.get()) {
                chatScroll.setVvalue(1.0);
                scrollToBottom.set(false);
            }
        });
    }

    @FXML
    private void sendMessage() {
        Message message = new Message(this.state.getCurrentUser(), this.message.getText());
        addChatMessage(message);
        this.message.clear();
        this.message.requestFocus();
    }

    private void addChatMessage(Message message) {
        try {
            scrollToBottom.set(true);

            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/fxml/message.fxml"));
            chat.getChildren().add(loader.load());

            MessageController controller = loader.getController();
            controller.setMessage(message);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
