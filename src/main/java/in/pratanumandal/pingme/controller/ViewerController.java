package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.entity.attachment.Attachment;
import in.pratanumandal.pingme.engine.entity.attachment.AudioAttachment;
import in.pratanumandal.pingme.engine.entity.attachment.ImageAttachment;
import in.pratanumandal.pingme.engine.entity.attachment.VideoAttachment;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class ViewerController {

    @FXML private AnchorPane player;

    @FXML private HBox toolbar;

    @FXML private HBox controls;

    @FXML private ImageView imageView;

    @FXML private MediaView mediaView;

    @FXML private Button playButton;

    @FXML private Button pauseButton;

    @FXML private Slider seekBar;

    private Attachment attachment;

    private MediaPlayer mediaPlayer;

    private boolean isUserSeeking = false;

    @FXML
    private void initialize() {
        imageView.fitWidthProperty().bind(player.widthProperty().subtract(100));
        imageView.fitHeightProperty().bind(player.heightProperty().subtract(toolbar.heightProperty()).subtract(controls.heightProperty()).subtract(100));
        mediaView.fitWidthProperty().bind(player.widthProperty().subtract(100));
        mediaView.fitHeightProperty().bind(player.heightProperty().subtract(toolbar.heightProperty()).subtract(controls.heightProperty()).subtract(100));

        player.managedProperty().bind(player.visibleProperty());
        toolbar.managedProperty().bind(toolbar.visibleProperty());
        controls.managedProperty().bind(controls.visibleProperty());
        playButton.managedProperty().bind(playButton.visibleProperty());
        pauseButton.managedProperty().bind(pauseButton.visibleProperty());

        this.pause();
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;

        if (attachment.getType() == Attachment.AttachmentType.IMAGE) {
            imageView.setImage(((ImageAttachment) attachment).getImage());
            imageView.setVisible(true);
            mediaView.setVisible(false);
            controls.setVisible(false);
        }
        else if (attachment.getType() == Attachment.AttachmentType.AUDIO) {
            imageView.setImage(attachment.getThumbnail());
            imageView.setVisible(true);
            mediaView.setVisible(false);
            controls.setVisible(true);

            AudioAttachment audioAttachment = (AudioAttachment) attachment;
            mediaPlayer = new MediaPlayer(audioAttachment.getMedia());
            this.initMediaPlayer();
        }
        else if (attachment.getType() == Attachment.AttachmentType.VIDEO) {
            imageView.setVisible(false);
            mediaView.setVisible(true);
            controls.setVisible(true);

            VideoAttachment videoAttachment = (VideoAttachment) attachment;
            mediaPlayer = new MediaPlayer(videoAttachment.getMedia());
            mediaView.setMediaPlayer(mediaPlayer);
            this.initMediaPlayer();
        }
    }

    @FXML
    public void download() {
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

    @FXML
    public void show() {
        player.setVisible(true);
    }

    @FXML
    public void hide() {
        this.pause();
        player.setVisible(false);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void initMediaPlayer() {
        // Set length of slider
        mediaPlayer.setOnReady(() -> {
            seekBar.setMax(mediaPlayer.getTotalDuration().toSeconds());
            this.play();
        });

        mediaPlayer.setOnError(() -> {
            if (mediaPlayer.getError().getType() == MediaException.Type.UNKNOWN) {
                this.setAttachment(attachment);
            }
            else {
                MediaException error = mediaPlayer.getError();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to play media");
                alert.setContentText(error.getType() + ": " + error.getMessage());
                alert.initOwner(PrimaryStage.getInstance().getStage());
                alert.showAndWait();

                this.hide();
            }
        });

        // Update slider during playback
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!isUserSeeking) {
                seekBar.setValue(newTime.toSeconds());
            }
        });

        // Handle dragging (scrubbing)
        seekBar.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            isUserSeeking = isChanging;
            if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(seekBar.getValue()));
            }
        });

        // Handle clicks on the track
        seekBar.setOnMousePressed(e -> isUserSeeking = true);
        seekBar.setOnMouseReleased(e -> {
            mediaPlayer.seek(Duration.seconds(seekBar.getValue()));
            isUserSeeking = false;
        });
    }

    @FXML
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
        playButton.setVisible(false);
        pauseButton.setVisible(true);
    }

    @FXML
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        playButton.setVisible(true);
        pauseButton.setVisible(false);
    }

}
