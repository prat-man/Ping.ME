package in.pratanumandal.pingme.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ModeController extends AbstractController {

    @FXML private TextField serverPort;

    @FXML private TextField clientName;
    @FXML private TextField clientAddress;
    @FXML private TextField clientPort;

    private List<ModeListener> listeners;

    @FXML
    protected void initialize() {
        listeners = new ArrayList<>();
    }

    public void addListener(ModeListener listener) {
        listeners.add(listener);
    }

    @FXML
    private void server() {
        for (ModeListener listener : listeners) {
            int port = 80;
            if (!serverPort.getText().isBlank()) {
                port = Integer.parseInt(serverPort.getText());
            }

            listener.server(port);
        }
    }

    @FXML
    private void client() throws UnknownHostException {
        for (ModeListener listener : listeners) {
            if (clientName.getText().isBlank()) {
                throw new RuntimeException();
            }
            String name = clientName.getText();

            InetAddress address = InetAddress.getLocalHost();
            if (!clientAddress.getText().isBlank()) {
                address = InetAddress.getByName(clientAddress.getText());
            }

            int port = 80;
            if (!clientPort.getText().isBlank()) {
                port = Integer.parseInt(clientPort.getText());
            }

            listener.client(name, address, port);
        }
    }

    interface ModeListener {

        void server(int port);

        void client(String name, InetAddress address, int port);

    }

}
