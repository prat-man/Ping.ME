package in.pratanumandal.pingme.engine.client;

import in.pratanumandal.pingme.engine.entity.Message;
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
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private String name;
    private Image avatar;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private EllipticCurveDiffieHellmanAES ecdhaes;
    private SimpleBooleanProperty running;

    public Client(String name, Image avatar, InetAddress address, int port) throws IOException {
        this.name = name;
        this.avatar = avatar;
        this.clientSocket = new Socket(address, port);
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.ecdhaes = new EllipticCurveDiffieHellmanAES();
        this.running = new SimpleBooleanProperty(false);

        this.setDaemon(true);
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
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
                e.printStackTrace();
            }
        }
    }

    private void handlePacket(Packet packet) throws IOException {
        if (packet.getType() == PacketType.WELCOME) {
            handleWelcome(packet);
        }
        else if (packet.getType() == PacketType.CONNECT) {
            handleConnect(packet);
        }
        else if (packet.getType() == PacketType.DISCONNECT) {
            handleDisconnect(packet);
        }
        else if (packet.getType() == PacketType.REMOVE) {
            handleRemove(packet);
        }
        else if (packet.getType() == PacketType.MESSAGE) {
            handleMessage(packet);
        }
    }

    private void handleWelcome(Packet packet) {
        WelcomePacket welcomePacket = (WelcomePacket) packet;

        Platform.runLater(() -> {
            ChatState.getInstance().setCurrentUser(welcomePacket.getUser());
            ChatState.getInstance().getLobbyList().clear();
            ChatState.getInstance().getLobbyList().addAll(welcomePacket.getLobbyList());
        });

        ecdhaes.setReceiverPublicKey(welcomePacket.getPublicKey());
    }

    private void handleConnect(Packet packet) {
        ConnectPacket connectedPacket = (ConnectPacket) packet;

        Platform.runLater(() -> {
            ChatState.getInstance().getLobbyList().add(connectedPacket.getUser());
            Message message = new Message(null, connectedPacket.getUser().getName() + " joined the chat");
            ChatState.getInstance().getMessageList().add(message);
        });
    }

    private void handleDisconnect(Packet packet) {
        DisconnectPacket disconnectPacket = (DisconnectPacket) packet;

        if (disconnectPacket.getUser() == null) {
            running.set(false);

            Platform.runLater(() -> {
                Message message = new Message(null, "Server has shutdown");
                ChatState.getInstance().getMessageList().add(message);
            });
        }
        else {
            Platform.runLater(() -> {
                ChatState.getInstance().getLobbyList().remove(disconnectPacket.getUser());
                Message message = new Message(null, disconnectPacket.getUser().getName() + " left the chat");
                ChatState.getInstance().getMessageList().add(message);
            });
        }
    }

    private void handleRemove(Packet packet) {
        RemovePacket removedPacket = (RemovePacket) packet;

        if (removedPacket.getUser().equals(ChatState.getInstance().getCurrentUser())) {
            running.set(false);

            Platform.runLater(() -> {
                Message message = new Message(null, "You have been removed");
                ChatState.getInstance().getMessageList().add(message);
            });
        }
        else {
            Platform.runLater(() -> {
                ChatState.getInstance().getLobbyList().remove(removedPacket.getUser());
                Message message = new Message(null, removedPacket.getUser().getName() + " has been removed");
                ChatState.getInstance().getMessageList().add(message);
            });
        }
    }

    private void handleMessage(Packet packet) {
        MessagePacket messagePacket = (MessagePacket) packet;

        Platform.runLater(() -> {
            ChatState.getInstance().getMessageList().add(messagePacket.getMessage());
        });
    }

    public void connect() {
        JoinPacket joinPacket = new JoinPacket(name, avatar, ecdhaes.getPublicKey());
        sendPacket(joinPacket);
    }

    public void disconnect() {
        if (isRunning()) {
            running.set(false);

            DisconnectPacket disconnectPacket = new DisconnectPacket();
            sendPacket(disconnectPacket);
        }
    }

    public void sendMessage(Message message) {
        MessagePacket messagePacket = new MessagePacket(message);
        sendPacket(messagePacket);
    }

    public void sendPacket(Packet packet) {
        try {
            Object object = packet;
            if (ecdhaes.isReady()) {
                object = ecdhaes.encrypt(object);
            }
            outputStream.writeObject(object);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
