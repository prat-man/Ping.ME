package in.pratanumandal.pingme.state;

import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.packet.Packet;
import in.pratanumandal.pingme.engine.entity.ServerLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerLogs {

    private static ServerLogs instance;

    private final ObservableList<ServerLog> logs;

    private ServerLogs() {
        logs = FXCollections.observableArrayList();
    }

    public ObservableList<ServerLog> getLogs() {
        return logs;
    }

    public void addLog(ServerLog.Channel channel, User user, Packet packet) {
        logs.add(new ServerLog(channel, user, packet));
    }

    public static void initialize() {
        instance = new ServerLogs();
    }

    public static ServerLogs getInstance() {
        if (instance == null) {
            throw new RuntimeException(ServerLogs.class.getSimpleName() + " is not initialized");
        }
        return instance;
    }

}
