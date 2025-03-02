package in.pratanumandal.pingme.engine.entity;

import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChatFile implements Serializable {

    private final String fileName;
    private final byte[] fileBytes;

    public ChatFile(Path path) throws IOException {
        fileName = path.getFileName().toString();
        fileBytes = Files.readAllBytes(path);
    }

    public void downloadFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(PrimaryStage.getInstance().getStage());

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileBytes);
            }
        }
    }

    public byte[] getBytes() {
        return fileBytes;
    }

}
