package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.engine.Message;
import in.pratanumandal.pingme.engine.client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.Cell;
import org.fxmisc.flowless.VirtualFlow;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ChatController extends AbstractController {

    @FXML private TextArea message;

    @FXML private Label characters;

    @FXML private Button send;

    @FXML private VBox container;

    private ObservableList<HBox> messages;
    private VirtualFlow<HBox, ?> chatPane;

    private Client client;

    @FXML
    protected void initialize() {
        message.textProperty().addListener((obs, oldVal, newVal) -> send.setDisable(newVal.isEmpty()));

        message.addEventFilter(KeyEvent.ANY, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();

                if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                    if (event.isShiftDown()) {
                        message.replaceText(message.getSelection(), "\n");
                    }
                    else if (!message.getText().isBlank()) {
                        sendMessage();
                    }
                }
            }
        });

        characters.setText(Constants.MAX_MESSAGE_LENGTH + " character(s) remaining");
        message.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getControlNewText();
            if (text.length() > Constants.MAX_MESSAGE_LENGTH) {
                return null;
            }

            int remaining = Constants.MAX_MESSAGE_LENGTH - text.length();
            characters.setText(remaining + " character(s) remaining");

            return change;
        }));

        chatState.getMessageList().addListener((ListChangeListener<? super Message>) change -> {
            change.next();
            List<? extends Message> messageList = change.getAddedSubList();
            for (Message message : messageList) {
                addChatMessage(message);
            }
        });

        messages = FXCollections.observableArrayList();
        chatPane = VirtualFlow.createVertical(messages, Cell::wrapNode);

        VirtualizedScrollPane<VirtualFlow<HBox, ?>> scrollPane = new VirtualizedScrollPane<>(chatPane);
        scrollPane.getStyleClass().add("chat-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        container.getChildren().add(scrollPane);

        AtomicReference<HBox> first = new AtomicReference<>();
        AtomicReference<HBox> last = new AtomicReference<>();
        messages.addListener((ListChangeListener<? super HBox>) change -> {
            if (first.get() != null) first.get().getStyleClass().remove("first");
            first.set(messages.getFirst());
            if (first.get() != null) first.get().getStyleClass().add("first");

            if (last.get() != null) last.get().getStyleClass().remove("last");
            last.set(messages.getLast());
            if (last.get() != null) last.get().getStyleClass().add("last");
        });
    }

    @FXML
    private void sendMessage() {
        Message message = new Message(this.chatState.getCurrentUser(), this.message.getText());
        addChatMessage(message);
        this.message.clear();
        this.message.requestFocus();

        client.sendMessage(message);
    }

    public void setClient(Client client) {
        this.client = client;

        client.runningProperty().addListener((obs, oldVal, newVal) -> {
            message.setDisable(!newVal);
            send.setDisable(!newVal);
        });
    }

    private void addChatMessage(Message message) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/fxml/message.fxml"));
            messages.add(loader.load());
            chatPane.show(messages.size() - 1);

            MessageController controller = loader.getController();
            controller.setMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
