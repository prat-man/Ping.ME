package in.pratanumandal.pingme.engine.server;

import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.packet.DisconnectPacket;
import in.pratanumandal.pingme.engine.packet.Packet;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private final ServerSocket socket;
    private final SimpleBooleanProperty running;
    private final List<ClientHandler> clientHandlerList;

    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.clientHandlerList = new ArrayList<>();
        this.running = new SimpleBooleanProperty(false);

        this.setDaemon(true);
    }

    public String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPublicIPAddress() {
        try {
            URL url = URI.create("https://checkip.amazonaws.com/").toURL();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPort() {
        return String.valueOf(this.socket.getLocalPort());
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleBooleanProperty runningProperty() {
        return running;
    }

    public void run() {
        this.running.set(true);

        while (running.get()) {
            try {
                Socket clientSocket = socket.accept();

                ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                this.clientHandlerList.add(clientHandler);

                clientHandler.start();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendBroadcast(Packet packet) {
        sendBroadcast(null, packet);
    }

    public void sendBroadcast(User sender, Packet packet) {
        for (ClientHandler clientHandler : clientHandlerList) {
            if (clientHandler.isRunning()) {
                User user = clientHandler.getUser();
                if (user != null && (sender == null || !sender.equals(user))) {
                    clientHandler.sendPacket(packet);
                }
            }
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
    }

    public void removeUser(User user) {
        for (ClientHandler clientHandler : clientHandlerList) {
            if (clientHandler.getUser().equals(user)) {
                clientHandler.stopRunning();
                return;
            }
        }
    }

    public void disconnect() {
        if (isRunning()) {
            running.set(false);

            DisconnectPacket disconnectPacket = new DisconnectPacket();
            sendBroadcast(disconnectPacket);
        }
    }

}
