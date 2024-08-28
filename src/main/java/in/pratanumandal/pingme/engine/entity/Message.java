package in.pratanumandal.pingme.engine.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy | hh:mm a");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");


    private final AuthenticatedUser user;
    private final String message;
    private final LocalDateTime timestamp;

    public Message(User user, String message) {
        this.user = new AuthenticatedUser(user);
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public User getUser() {
        return user.getUser();
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
