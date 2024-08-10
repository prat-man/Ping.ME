package in.pratanumandal.pingme.engine;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy | hh:mm a");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private User user;
    private String message;
    private LocalDateTime timestamp;

    public Message(User user, String message) {
        this.user = user;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
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
