package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.common.Utils;
import in.pratanumandal.pingme.engine.client.Client;
import in.pratanumandal.pingme.engine.entity.Message;
import in.pratanumandal.pingme.engine.entity.attachment.*;
import in.pratanumandal.pingme.state.ChatState;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.Cell;
import org.fxmisc.flowless.VirtualFlow;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ChatController {

    @FXML private final int thumbnailSize = Constants.THUMBNAIL_SIZE;

    @FXML private TextArea message;

    @FXML private Label characters;

    @FXML private Button send;

    @FXML private VBox container;

    @FXML private ScrollPane attachmentScroll;

    @FXML private HBox attachmentTiles;

    private ObservableList<HBox> messages;
    private VirtualFlow<HBox, ?> chatPane;

    private ObservableList<Attachment> attachments;

    private Client client;
    private ViewerController viewer;

    @FXML
    protected void initialize() {
        attachments = FXCollections.observableArrayList();

        message.textProperty().addListener((obs, oldVal, newVal) -> updateSendButton());
        attachments.addListener((ListChangeListener<Attachment>) change -> updateSendButton());

        attachments.addListener((ListChangeListener<Attachment>) change -> {
            change.next();
            change.getAddedSubList().forEach(attachment -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/attachment.fxml"));
                    StackPane root = loader.load();
                    attachmentTiles.getChildren().add(root);

                    AttachmentController controller = loader.getController();
                    controller.setAttachment(attachment);
                    controller.setMode(AttachmentController.AttachmentMode.CREATE);
                    controller.addListener(() -> {
                        attachments.remove(attachment);
                        attachmentTiles.getChildren().remove(root);
                    });
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

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

        ChatState.getInstance().getMessageList().addListener((ListChangeListener<? super Message>) change -> {
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

        attachmentScroll.minViewportHeightProperty().bind(attachmentTiles.heightProperty());
        attachmentScroll.managedProperty().bind(attachmentScroll.visibleProperty());
        attachments.addListener((ListChangeListener<Attachment>) change -> attachmentScroll.setVisible(!attachments.isEmpty()));
    }

    @FXML
    private void addAttachment() {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(PrimaryStage.getInstance().getStage());

        if (files != null) {
            files.forEach(file -> {
                Attachment attachment = Utils.tryOrDefault(
                        () -> new ImageAttachment(file.toPath()),
                        () -> new AudioAttachment(file.toPath()),
                        () -> new VideoAttachment(file.toPath()),
                        () -> new UnknownAttachment(file.toPath())
                );
                if (!attachments.contains(attachment)) {
                    attachments.add(attachment);
                }
            });
        }
    }

    @FXML
    private void sendMessage() {
        Message message = new Message(ChatState.getInstance().getCurrentUser(), this.message.getText(), new ArrayList<>(attachments));
        addChatMessage(message);

        this.message.clear();
        this.message.requestFocus();
        this.attachments.clear();
        this.attachmentTiles.getChildren().clear();

        client.sendMessage(message);
    }

    public void setClient(Client client) {
        this.client = client;

        client.runningProperty().addListener((obs, oldVal, newVal) -> {
            message.setDisable(!newVal);
            send.setDisable(!newVal);
        });
    }

    public void setViewer(ViewerController viewer) {
        this.viewer = viewer;
    }

    private void addChatMessage(Message message) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/fxml/message.fxml"));
            messages.add(loader.load());
            chatPane.show(messages.size() - 1);

            MessageController controller = loader.getController();
            controller.setMessage(message);
            controller.setViewer(viewer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSendButton() {
        send.setDisable(message.getText().isBlank() && attachments.isEmpty());
    }
    
}
