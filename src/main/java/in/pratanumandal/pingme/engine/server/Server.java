package in.pratanumandal.pingme.engine.server;

import in.pratanumandal.pingme.engine.entity.User;
import in.pratanumandal.pingme.engine.packet.DisconnectPacket;
import in.pratanumandal.pingme.engine.packet.Packet;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
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

    public String getPublicIPAddress() {
        try {
            URL url = URI.create("https://checkip.amazonaws.com/").toURL();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return br.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getLocalIPAddresses() {
        List<String> addresses = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // Ignore interfaces that are down, loopback, or virtual
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();

                    // Skip IPv6, link-local, and loopback addresses
                    if (!(inetAddress instanceof Inet4Address) || inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                        continue;
                    }

                    addresses.add(inetAddress.getHostAddress());
                }
            }
        }
        catch (SocketException e) {
            throw new RuntimeException(e);
        }

        // Sort the addresses
        Collections.sort(addresses);

        return addresses;
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
