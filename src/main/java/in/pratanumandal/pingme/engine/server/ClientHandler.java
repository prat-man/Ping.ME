package in.pratanumandal.pingme.engine.server;

import in.pratanumandal.pingme.common.Utils;
import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.packet.ConnectPacket;
import in.pratanumandal.pingme.engine.packet.DisconnectPacket;
import in.pratanumandal.pingme.engine.packet.JoinPacket;
import in.pratanumandal.pingme.engine.packet.MessagePacket;
import in.pratanumandal.pingme.engine.packet.Packet;
import in.pratanumandal.pingme.engine.packet.PacketType;
import in.pratanumandal.pingme.engine.packet.RemovePacket;
import in.pratanumandal.pingme.engine.packet.WelcomePacket;
import in.pratanumandal.pingme.security.EllipticCurveDiffieHellmanAES;
import in.pratanumandal.pingme.security.SecureObject;
import in.pratanumandal.pingme.state.ChatState;
import in.pratanumandal.pingme.state.ServerLogs;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private User user;
    private Server server;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private EllipticCurveDiffieHellmanAES ecdhaes;
    private SimpleBooleanProperty running;

    public ClientHandler(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.ecdhaes = new EllipticCurveDiffieHellmanAES();
        this.running = new SimpleBooleanProperty(false);

        this.setDaemon(true);
    }

    public User getUser() {
        return user;
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public void stopRunning() {
        handleRemove();

        try {
            this.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        running.set(true);

        try {
            Object object;
            while (running.get() && (object = inputStream.readObject()) != null) {
                if (ecdhaes.isReady()) {
                    object = ecdhaes.decrypt((SecureObject) object);
                }
                Packet packet = (Packet) object;
                handlePacket(packet);
            }
        }
        catch (IOException e) {
            // DO NOTHING
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handlePacket(Packet packet) throws IOException {
        ServerLogs.getInstance().addLog(ServerLog.Channel.INCOMING, user, packet);

        if (packet.getType() == PacketType.JOIN) {
            handleJoin(packet);
        }
        else if (packet.getType() == PacketType.DISCONNECT) {
            handleDisconnect(packet);
        }
        else if (packet.getType() == PacketType.MESSAGE) {
            handleMessage(packet);
        }
    }

    private void handleJoin(Packet packet) {
        JoinPacket joinPacket = (JoinPacket) packet;

        user = new User(joinPacket.getName(), joinPacket.getAvatar(), clientSocket.getInetAddress(), clientSocket.getPort());

        List<User> lobbyList = ChatState.getInstance().getLobbyList();
        Utils.runAndWait(() -> lobbyList.add(user));

        WelcomePacket welcomePacket = new WelcomePacket(user, lobbyList, ecdhaes.getPublicKey());
        sendPacket(welcomePacket);

        ConnectPacket connectedPacket = new ConnectPacket(user);
        broadcastPacket(connectedPacket);

        ecdhaes.setReceiverPublicKey(joinPacket.getPublicKey());
    }

    private void handleDisconnect(Packet packet) {
        DisconnectPacket disconnectPacket = (DisconnectPacket) packet;

        running.set(false);

        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.removeClient(this);

        Platform.runLater(() -> {
            ChatState.getInstance().getLobbyList().remove(user);
        });

        broadcastPacket(disconnectPacket);
    }

    private void handleRemove() {
        RemovePacket removePacket = new RemovePacket(user);

        sendPacket(removePacket);

        running.set(false);

        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.removeClient(this);

        Platform.runLater(() -> {
            ChatState.getInstance().getLobbyList().remove(user);
        });

        broadcastPacket(removePacket);
    }

    private void handleMessage(Packet packet) {
        MessagePacket messagePacket = (MessagePacket) packet;
        broadcastPacket(messagePacket);
    }

    public void sendPacket(Packet packet) {
        ServerLogs.getInstance().addLog(ServerLog.Channel.OUTGOING, user, packet);

        try {
            Object object = packet;
            if (ecdhaes.isReady()) {
                object = ecdhaes.encrypt(object);
            }
            outputStream.writeObject(object);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastPacket(Packet packet) {
        server.sendBroadcast(user, packet);
    }

}
