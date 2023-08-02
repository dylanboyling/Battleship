package Game.ClientServer.Server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains the backend logic for the Battleship server
 */
public class Server {


    /**
     * Socket that the server is currently listening to requests on
     */
    private ServerSocket serverSocket;

    /**
     * Number of clients currently connected to the server
     */
    private int numberOfConnectedClients = 0;

    /**
     * Client ID that is to be assigned to the next connected client
     */
    private int currentClientId = 0;

    /**
     * List of the threads currently created by the server
     */
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * List containing all the boat configurations stored in the server
     */
    private final List<String> boatConfigurations = new ArrayList<>();

    /**
     * True if the server is in a finalized state, false otherwise
     */
    private boolean isFinalized;

    /**
     * Server UI for the backend of the server, used for sending messages to the user
     */
    private static ServerPanel serverPanel;

    /**
     * Entry point for the server. Initializes the server and it's UI
     *
     * @param args Command line arguments, not used
     */
    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "CA"));
        Server server = new Server();
        serverPanel = new ServerPanel(server);
        serverPanel.initializePanel();
    }

    /**
     * Creates a new socket on a given port and waits for connection requests. Requests are handled in separate threads
     *
     * @param portNumber Port number that the socket is to be opened on
     */
    public void listenForConnections(final int portNumber) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverPanel.showDialogBox("This server is already listening on port " + portNumber + "\n");
        }
        // new thread to prevent UI lock up
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portNumber);
                serverPanel.updateLogPanel("Server is now listening for clients on port " + portNumber + "\n");
                System.out.println("[DEBUG] Server listening on port " + portNumber);

                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        numberOfConnectedClients++;
                        ClientHandler clientHandler = new ClientHandler(this, clientSocket, currentClientId, serverPanel);
                        Thread clientThread = new Thread(clientHandler);
                        clientThread.start();
                        clientHandlers.add(clientHandler);
                        serverPanel.updateLogPanel("Client " + currentClientId + " has connected to the server\n");
                        currentClientId++;
                    } catch (SocketException e) {
                        System.out.println("[DEBUG] Socket.accept() has been interrupted, most likely due to ending server.");
                        break;
                    }
                }
            } catch (BindException e) {
                // TODO UI pop up
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Notifies the server that a client has disconnected, so it can update accordingly
     */
    public void clientDisconnected() {
        numberOfConnectedClients--;

        if (isFinalized) {
            if (numberOfConnectedClients == 0) {
                try {
                    serverSocket.close();
                    System.out.println("[DEBUG] All clients disconnected, server shutting down.");
                    serverPanel.showDialogBox("All clients disconnected, server shutting down.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns a randomly selected configuration from the stored configurations
     *
     * @return String containing board dimension and ship locations
     */
    public String getRandomConfiguration() {
        if (!boatConfigurations.isEmpty()) {
            final int randomConfigIndex = ThreadLocalRandom.current().nextInt(0, boatConfigurations.size());
            return boatConfigurations.get(randomConfigIndex);
        }

        return null;
    }

    /**
     * Stores the provided board and boat configuration in the server
     *
     * @param boatConfig String containing information about the board's dimension and location and size of boats
     */
    public void storeConfiguration(String boatConfig) {
        boatConfigurations.add(boatConfig);
    }

    /**
     * Closes the server socket and sends the END CONNECTION protocol to all clients, empties the list of clients
     */
    public void closeConnections() {
        try {
            serverPanel.updateLogPanel("Shutting down the server, sending disconnect requests to clients ...\n");
            serverSocket.close();

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendEndConnectionRequest();
            }
            clientHandlers = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Ignoring blocked sockets in accept()");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the server to a finalized state. That is, if it is in a finalized state then when the last client
     * disconnects, the server will shut down.
     *
     * @param isFinalized True if the server is to be finalized, false otherwise
     */
    public void setFinalized(final boolean isFinalized) {
        System.out.println("[DEBUG] Server is" + (isFinalized ? " " : " not ") + "in a finalized state");
        serverPanel.updateLogPanel("Server is" + (isFinalized ? " " : " not ") + "in a finalized state");
        this.isFinalized = isFinalized;
    }
}
