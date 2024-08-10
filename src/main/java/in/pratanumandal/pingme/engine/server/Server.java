package in.pratanumandal.pingme.engine.server;

import in.pratanumandal.pingme.engine.User;
import in.pratanumandal.pingme.engine.packet.DisconnectPacket;
import in.pratanumandal.pingme.engine.packet.Packet;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private ServerSocket socket;
    private SimpleBooleanProperty running;
    private List<ClientHandler> clientHandlerList;

    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.clientHandlerList = new ArrayList<>();
        this.running = new SimpleBooleanProperty(false);

        this.setDaemon(true);
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
                clientHandler.start();

                this.clientHandlerList.add(clientHandler);
            }
            catch (IOException e) {
                e.printStackTrace();
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

    public void disconnectClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
    }

    public void disconnect() {
        running.set(false);

        DisconnectPacket disconnectPacket = new DisconnectPacket();
        sendBroadcast(disconnectPacket);
    }

}
