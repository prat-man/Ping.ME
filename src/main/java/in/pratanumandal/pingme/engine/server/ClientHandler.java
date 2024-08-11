package in.pratanumandal.pingme.engine.server;

import in.pratanumandal.pingme.common.Utils;
import in.pratanumandal.pingme.engine.ChatState;
import in.pratanumandal.pingme.engine.User;
import in.pratanumandal.pingme.engine.packet.ConnectPacket;
import in.pratanumandal.pingme.engine.packet.ConnectedPacket;
import in.pratanumandal.pingme.engine.packet.DisconnectPacket;
import in.pratanumandal.pingme.engine.packet.MessagePacket;
import in.pratanumandal.pingme.engine.packet.Packet;
import in.pratanumandal.pingme.engine.packet.PacketType;
import in.pratanumandal.pingme.engine.packet.WelcomePacket;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientHandler extends Thread {

    private User user;
    private Server server;
    private Socket clientSocket;
    private SimpleBooleanProperty running;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandler(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
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
        this.handleDisconnect();

        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running.set(true);

        try {
            Object object;
            while (running.get() && (object = inputStream.readObject()) != null) {
                Packet packet = (Packet) object;
                handlePacket(packet);
            }
        }
        catch (SocketException e) {
            handleDisconnect();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePacket(Packet packet) throws IOException {
        if (packet.getType() == PacketType.CONNECT) {
            handleConnect(packet);
        }
        else if (packet.getType() == PacketType.DISCONNECT) {
            handleDisconnect(packet);
        }
        else if (packet.getType() == PacketType.MESSAGE) {
            handleMessage(packet);
        }
    }

    private void handleConnect(Packet packet) {
        ConnectPacket connectPacket = (ConnectPacket) packet;

        this.user = new User(connectPacket.getName(), clientSocket.getInetAddress(), clientSocket.getPort());

        List<User> lobbyList = ChatState.getInstance().getLobbyList();

        Utils.runAndWait(() -> lobbyList.add(user));

        WelcomePacket welcomePacket = new WelcomePacket(user, lobbyList);
        sendPacket(welcomePacket);

        ConnectedPacket connectedPacket = new ConnectedPacket(user);
        broadcastPacket(connectedPacket);
    }

    private void handleDisconnect(Packet packet) {
        DisconnectPacket disconnectPacket = (DisconnectPacket) packet;

        running.set(false);

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.disconnectClient(this);

        Platform.runLater(() -> {
            ChatState.getInstance().getLobbyList().remove(user);
        });

        broadcastPacket(disconnectPacket);
    }

    private void handleDisconnect() {
        DisconnectPacket disconnectPacket = new DisconnectPacket(user);
        handleDisconnect(disconnectPacket);
    }

    private void handleMessage(Packet packet) {
        MessagePacket messagePacket = (MessagePacket) packet;
        broadcastPacket(messagePacket);
    }

    public void sendPacket(Packet packet) {
        try {
            outputStream.writeObject(packet);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastPacket(Packet packet) {
        server.sendBroadcast(user, packet);
    }

}
