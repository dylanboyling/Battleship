package Game.ClientServer.Server;

import Game.ClientServer.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

import static Game.ClientServer.Config.PROTOCOL_RECEIVE_GAME;
import static Game.ClientServer.Config.PROTOCOL_SEPARATOR;

/**
 * Handler which runs on its own thread in order to handle server requests from the client it is currently connected to
 */
public class ClientHandler implements Runnable {

    /**
     * Server that the client handler belongs to
     */
    private final Server server;

    /**
     * Server UI for the backend of the server, used for sending messages to the user
     */
    private ServerPanel serverPanel;

    /**
     * Socket that the handler is connected to the client with
     */
    private final Socket clientSocket;

    /**
     * Out stream for writing to the client socket this server socket is connected to
     */
    PrintStream out;

    /**
     * In stream for reading from the client socket this server socket is connected to
     */
    BufferedReader in;

    /**
     * ID assigned to this client handler
     */
    private final int clientId;

    /**
     * Constructs a ClientHandler which can send and receive data from the client it is currently connected with
     *
     * @param server       Server that the ClientHandler belongs to
     * @param clientSocket Socket that the server is connected to the client with
     * @param clientId     ID that has been assigned to the client
     */
    public ClientHandler(final Server server, final Socket clientSocket, final int clientId, final ServerPanel serverPanel) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        this.serverPanel = serverPanel;
    }

    @Override
    public void run() {
        try {
            out = new PrintStream(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println(clientId);
            while (!clientSocket.isClosed()) {
                System.out.println("[DEBUG] Waiting for data from client " + clientId);
                final String data = in.readLine();
                System.out.println("[DEBUG] Received: " + data);

                if (data == null) {
                    System.out.printf("[DEBUG] Client %s has terminated the connection%n", clientId);

                    break;
                }

                final int separatorIndex = data.indexOf(PROTOCOL_SEPARATOR);
                if (separatorIndex != -1) {
                    final String clientId = data.substring(0, separatorIndex);
                    final String protocolType = data.substring(separatorIndex + 1, separatorIndex + 3); // assumes protocol type is only ever 2 characters long

                    System.out.println("[DEBUG] Received protocol: " + protocolType);
                    if (!protocolType.equals(Config.PROTOCOL_END)) {
                        if (protocolType.equals(Config.PROTOCOL_SEND_GAME)) {
                            receiveGameConfigFromClient(data);
                        } else if (protocolType.equals(Config.PROTOCOL_RECEIVE_GAME)) {
                            sendGameConfigToClient();
                        }
                    } else {
                        System.out.printf("[DEBUG] Client %s is requesting to terminate connection%n", clientId);
                        serverPanel.updateLogPanel(String.format("Client %s is requesting to terminate connection%n", clientId));
                        clientSocket.close();
                        server.clientDisconnected();
                    }
                }
            }

            out.close();
            in.close();
            clientSocket.close();
            System.out.println("[DEBUG] Connection with client " + clientId + " closed.");
            serverPanel.updateLogPanel("Connection with client " + clientId + " closed.\n");
        } catch (SocketException e) {
            System.out.println("[DEBUG] Client has terminated their connection");
            serverPanel.updateLogPanel("Client has terminated their connection.\n");
        } catch (IOException e) {
            // dunno why this would happen, I forgot
            e.printStackTrace();
        }
    }

    public void sendEndConnectionRequest() throws IOException {
        out.println(Config.PROTOCOL_END + PROTOCOL_SEPARATOR);
        out.close();
        in.close();
        clientSocket.close();
    }

    /**
     * Receives a game configuration from the client and stores it in the server
     *
     * @param data String containing the game configuration
     */
    private void receiveGameConfigFromClient(final String data) {
        final int boatConfigIndex = data.indexOf(PROTOCOL_SEPARATOR, data.indexOf(PROTOCOL_SEPARATOR) + 1);
        final String boatConfig = data.substring(boatConfigIndex + 1);
        System.out.printf("[DEBUG] Received ship configuration from client %s, config: %s%n", clientId, boatConfig);
        serverPanel.updateLogPanel(String.format("Received ship configuration from client %s%n", clientId));
        server.storeConfiguration(boatConfig);
    }

    /**
     * Randomly selects a stored game configuration in the server and sents it to the client
     */
    private void sendGameConfigToClient() {
        System.out.println("[DEBUG] Sending config to client " + clientId);
        serverPanel.updateLogPanel("Sending config to client " + clientId + "\n");
        final String randomConfiguration = server.getRandomConfiguration();
        System.out.println(randomConfiguration);
        if (randomConfiguration == null) {
            out.println("The server currently has no configurations stored on it. Please try again later\n");
        } else {
            out.println(PROTOCOL_RECEIVE_GAME + PROTOCOL_SEPARATOR + randomConfiguration);
        }
    }
}
