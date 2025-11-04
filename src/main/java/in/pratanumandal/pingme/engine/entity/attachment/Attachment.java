package in.pratanumandal.pingme.engine.entity.attachment;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public abstract class Attachment implements Serializable {

    protected final transient Path path;
    protected final AttachmentType attachmentType;
    protected final String fileName;
    protected final byte[] payload;

    public Attachment(Path path, AttachmentType attachmentType) throws IOException {
        this.path = path;
        this.attachmentType = attachmentType;
        this.fileName = path.getFileName().toString();
        this.payload = Files.readAllBytes(path);
    }

    public String getFileName() {
        return fileName;
    }

    public AttachmentType getType() {
        return attachmentType;
    }

    public abstract byte[] getBytes();

    public abstract Image getThumbnail();

    public void write(Path path) throws IOException {
        Files.write(path, payload);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        if (path == null && that.path == null) return false;
        return Objects.equals(path, that.path);
    }

    public enum AttachmentType {
        IMAGE,
        AUDIO,
        UNKNOWN
    }

}
