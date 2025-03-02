package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.state.FileHandler;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.application.HostServices;
import javafx.scene.image.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChatFile implements Serializable {

    private final String fileName;
    private final byte[] fileBytes;

    public ChatFile(Path path) throws IOException {
        fileName = path.getFileName().toString();
        fileBytes = Files.readAllBytes(path);
    }

    public void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(PrimaryStage.getInstance().getStage());

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileBytes);
            }

            FileHandler.getInstance().openFile(file);
        }
    }

}
