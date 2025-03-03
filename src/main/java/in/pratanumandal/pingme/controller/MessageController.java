package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.FXApplication;
import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.common.Utils;
import in.pratanumandal.pingme.components.Avatar;
import in.pratanumandal.pingme.engine.entity.Message;
import in.pratanumandal.pingme.engine.entity.attachment.Attachment;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MessageController {

    @FXML private HBox container;

    @FXML private VBox icon;
    @FXML private HBox avatarBox;
    @FXML private Avatar avatar;

    @FXML private VBox wrapper;
    @FXML private Label sender;
    @FXML private TextFlow messageBox;
    @FXML private Text message;
    @FXML private Label timestamp;

    @FXML private ScrollPane attachmentScroll;
    @FXML private HBox attachmentTiles;

    private ObservableList<Attachment> attachments;

    @FXML
    protected void initialize() {
        attachments = FXCollections.observableArrayList();

        attachments.addListener((ListChangeListener<Attachment>) change -> {
            change.next();
            change.getAddedSubList().forEach(attachment -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/attachment.fxml"));
                    StackPane root = loader.load();
                    attachmentTiles.getChildren().add(root);

                    AttachmentController controller = loader.getController();
                    controller.setAttachment(attachment);
                    controller.setMode(AttachmentController.AttachmentMode.VIEW);
                    controller.addListener(() -> {
                        if (attachment.getType() == Attachment.AttachmentType.IMAGE) {
                            try {
                                FXMLLoader viewerLoader = new FXMLLoader(getClass().getResource("/fxml/viewer.fxml"));
                                VBox viewerRoot = viewerLoader.load();

                                ViewerController viewerController = viewerLoader.getController();
                                viewerController.setAttachment(attachment);

                                Stage stage = new Stage();
                                stage.setTitle(Constants.APP_NAME);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.initOwner(PrimaryStage.getInstance().getStage());

                                stage.getIcons().addAll(
                                        Utils.loadImage("/images/icon/icon_16.png"),
                                        Utils.loadImage("/images/icon/icon_24.png"),
                                        Utils.loadImage("/images/icon/icon_32.png"),
                                        Utils.loadImage("/images/icon/icon_64.png"),
                                        Utils.loadImage("/images/icon/icon_128.png"),
                                        Utils.loadImage("/images/icon/icon_256.png"));

                                Scene scene = new Scene(viewerRoot, 500, 300);
                                stage.setScene(scene);

                                stage.getScene().getStylesheets().add(FXApplication.class.getResource("/css/style.css").toExternalForm());

                                stage.show();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (attachment.getType() == Attachment.AttachmentType.FILE) {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setInitialFileName(attachment.getFileName());
                            File file = fileChooser.showSaveDialog(PrimaryStage.getInstance().getStage());

                            if (file != null) {
                                try {
                                    attachment.write(file.toPath());
                                }
                                catch (IOException e) {
                                    // TODO: Handle exceptions
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        container.managedProperty().bind(container.visibleProperty());
        icon.managedProperty().bind(icon.visibleProperty());
        sender.managedProperty().bind(sender.visibleProperty());
        timestamp.managedProperty().bind(timestamp.visibleProperty());

        container.setVisible(false);

        avatar = new Avatar();
        avatar.setPrefSize(30, 30);
        avatarBox.getChildren().add(avatar);

        attachmentScroll.minViewportHeightProperty().bind(attachmentTiles.heightProperty());
        attachmentScroll.managedProperty().bind(attachmentScroll.visibleProperty());
        attachments.addListener((ListChangeListener<Attachment>) change -> attachmentScroll.setVisible(!attachments.isEmpty()));

        messageBox.managedProperty().bind(messageBox.visibleProperty());
        message.textProperty().addListener((obs, oldVal, newVal) -> messageBox.setVisible(!newVal.isBlank()));
    }

    public void setMessage(Message message) {
        if (message == null) {
            container.setVisible(false);
            return;
        }

        container.setVisible(true);

        if (message.getUser() == null) {
            avatar.setImage(null);
            icon.setVisible(false);
            sender.setText(null);
            sender.setVisible(false);
            timestamp.setVisible(false);

            container.setAlignment(Pos.CENTER);

            wrapper.getStyleClass().add("server");
        }
        else if (message.getUser().isCurrentUser()) {
            avatar.setImage(null);
            icon.setVisible(false);
            sender.setText(null);
            sender.setVisible(false);
            timestamp.setVisible(true);

            container.setAlignment(Pos.CENTER_RIGHT);

            wrapper.getStyleClass().add("self");
        }
        else {
            avatar.setImage(message.getUser().getAvatar());
            icon.setVisible(true);
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

        this.attachments.addAll(message.getAttachments());
    }

}
