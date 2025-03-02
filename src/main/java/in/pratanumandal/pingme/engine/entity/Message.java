package in.pratanumandal.pingme.engine.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy | hh:mm a");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");


    private final AuthenticatedUser user;
    private final String message;
    private final List<Attachment> attachments;
    private final LocalDateTime timestamp;

    public Message(User user, String message, List<Attachment> attachments) {
        this.user = new AuthenticatedUser(user);
        this.message = message;
        this.attachments = attachments;
        this.timestamp = LocalDateTime.now();
    }

    public Message(User user, String message) {
        this(user, message, new ArrayList<>());
    }

    public User getUser() {
        return user.getUser();
    }

    public String getMessage() {
        return message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() {
        if (timestamp.toLocalDate().isEqual(LocalDate.now())) {
            return timeFormatter.format(timestamp);
        }
        return dateTimeFormatter.format(timestamp);
    }

}
