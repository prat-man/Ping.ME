package in.pratanumandal.pingme.common;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static final String APP_NAME = "Ping.ME";

    public static final String AUTHOR = "Pratanu Mandal";

    public static final int MAX_MESSAGE_LENGTH = 1000;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final int THUMBNAIL_SIZE = 50;

    public static final List<String> AUDIO_ENCODINGS = List.of("aac", "mp3");

    public static final List<String> VIDEO_ENCODINGS = List.of("h264", "hevc");

}
