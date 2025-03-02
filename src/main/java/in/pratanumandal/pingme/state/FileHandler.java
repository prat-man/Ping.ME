package in.pratanumandal.pingme.state;

import in.pratanumandal.pingme.common.Utils;
import javafx.application.HostServices;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class FileHandler {

    private static FileHandler instance;

    private final HostServices hostServices;

    private FileHandler(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void openFile(Path path) {
        hostServices.showDocument(path.toUri().toString());
    }

    public void openFile(File file) {
        hostServices.showDocument(file.toURI().toString());
    }

    public static void initialize(HostServices hostServices) {
        if (instance != null) {
            throw new RuntimeException(FileHandler.class.getSimpleName() + " is already initialized");
        }
        instance = new FileHandler(hostServices);
    }

    public static FileHandler getInstance() {
        if (instance == null) {
            throw new RuntimeException(FileHandler.class.getSimpleName() + " is not initialized");
        }
        return instance;
    }

}
