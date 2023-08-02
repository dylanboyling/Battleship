package Game.ClientServer.Client;

import Game.BattleshipMVC.Controller.BattleshipController;
import Game.BattleshipMVC.View.SplashScreens;
import Game.ClientServer.Config;
import Game.Util.Utils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Observable;

import static Game.BattleshipMVC.Controller.Battleship.LOADING_DURATION;
import static Game.ClientServer.Config.*;

/**
 * This class initializes the UI for the cli
 */
public class Client extends Observable {

    /**
     * ID of the client assigned to them by the server
     */
    private int clientId;

    /**
     * Socket in which the client is currently connected to the server on
     */
    private Socket serverSocket;

    /**
     * Reads from the server socket's stream in which the client is currently connected to
     */
    private BufferedReader in;

    /**
     * Writes out to the server socket in which the client is currently connected to
     */
    private PrintStream out;

    /**
     * Panel which contains the UI for the client
     */
    private static ClientPanel clientPanel;

    /**
     * Controller for the MVC Battleship game
     */
    BattleshipController controller;

    /**
     * Entry point for the client program
     *
     * @param args Command line arguments, not used
     */
    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "CA"));
        Client client = new Client();
        clientPanel = new ClientPanel(client);
        clientPanel.initializePanel();
    }

    /**
     * Attempts to connect the client to the server located at hostName on the given portNumber
     *
     * @param username   User-friendly identification for the client (not used really)
     * @param hostName   Address of the host being connected to
     * @param portNumber Port number the client is connected to the server on
     */
    public void connectToPort(final String username, final String hostName, final int portNumber) {
        // connect on worker thread so that UI is not locked
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    serverSocket = new Socket(hostName, portNumber);
                    in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                    out = new PrintStream(serverSocket.getOutputStream());

                    final String welcomeMessage = in.readLine();
                    clientId = Integer.parseInt(welcomeMessage);
                    clientPanel.updateLogPanel("Connected to the Battleship server! Your ID is " + clientId + ".\n");
                    clientPanel.setConnectedState(true);
                    System.out.println("[DEBUG] Connected to the Battleship server! Your ID is " + clientId);

                    String data;
                    while (!serverSocket.isClosed() && (data = in.readLine()) != null) {
                        System.out.println("[DEBUG] Client received data :)");
                        int separatorIndex = data.indexOf(PROTOCOL_SEPARATOR);
                        if (separatorIndex != -1) {
                            final String protocol = data.substring(0, separatorIndex);
                            System.out.println("[DEBUG] Receiving protocol " + protocol + " ...");
                            if (protocol.equals(PROTOCOL_RECEIVE_GAME)) {
                                System.out.println("[DEBUG] Loading configuration from server: " + data.substring(separatorIndex + 1));
                                clientPanel.updateLogPanel("Loading configuration from server ...\n");
                                if (controller != null) {
                                    System.out.print("[DEBUG] Game already launched, applying config");
                                    controller.loadBoatConfiguration(data.substring(separatorIndex + 1));
                                } else {
                                    System.out.print("[DEBUG] Launching game and then applying config ");
                                    createNewGame();
                                    controller.loadBoatConfiguration(data.substring(separatorIndex + 1));
                                }
                            } else if (protocol.equals(PROTOCOL_END)) {
                                serverSocket.close();
                                clientPanel.updateLogPanel("Server is shutting down, closing connection ...\n");
                            }
                        } else {
                            clientPanel.updateLogPanel(data + "\n");
                        }
                    }
                } catch (ConnectException e) {
                    clientPanel.showDialogBox("Error! There is no server listening on that port.");
                } catch (SocketException e) {
                    clientPanel.showDialogBox("Disconnected! Server has most likely shut down without warning.");
                    clientPanel.setConnectedState(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        worker.execute();
    }

    /**
     * Launches a new instance of the Battleship game
     */
    public void createNewGame() {
        SplashScreens.displayLoadingSplashScreen(LOADING_DURATION);
        controller = new BattleshipController();
        controller.configure(this);
    }

    /**
     * Attempts to launch the Battleship game
     *
     * @return True if the game was successfully launched, false otherwise
     */
    public boolean playGame() {
        if (controller == null) {
            clientPanel.showDialogBox("Must launch the game by clicking 'New Game' and\n" +
                    "place your ships on the board before playing.");
            return false;
        }

        final boolean isGameStarted = controller.playGame();
        if (isGameStarted) {
            clientPanel.updateLogPanel("The Battleship game has started!\n");
        } else {
            clientPanel.updateLogPanel(Utils.getLocalizedString("error_boats_missing") + "\n");
        }
        return isGameStarted;
    }

    /**
     * Closes connection to the server it is currently connected to
     */
    public void closeConnection() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                out.println(clientId + PROTOCOL_SEPARATOR + PROTOCOL_END);
                serverSocket.close();
                clientPanel.updateLogPanel("Connection with server closed\n");
                System.out.println("[DEBUG] Connection with server closed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the current board configuration to the server
     *
     * @return True if the configuration was successfully accepted and sent, false otherwise
     */
    public boolean sendConfigurationToServer() {
        if (serverSocket == null || serverSocket.isClosed()) {
            clientPanel.showDialogBox("Must connect to a server before sending a game configuration.");
            return false;
        }

        if (controller == null) {
            clientPanel.showDialogBox("Game not launched, there is no layout to send.\n" +
                    "Click 'New Game' to launch the game and create a configuration to send.");
            return false;
        }

        if (controller.isShipConfigurationValid()) {
            final String boatConfig = controller.getShipConfiguration();
            final String boardConfigurationProtocol = clientId + Config.PROTOCOL_SEPARATOR + Config.PROTOCOL_SEND_GAME
                    + PROTOCOL_SEPARATOR + boatConfig;
            out.println(boardConfigurationProtocol);
            System.out.println("[DEBUG] Sending the ship config to the server:\n" + boardConfigurationProtocol);
            return true;
        }

        clientPanel.updateLogPanel("Ship configuration is not valid. Make sure you place all ships on the board.\n");
        return false;
    }

    /**
     * Receives a random ship configuration from the server
     */
    public void receiveConfiguration() {
        if (serverSocket != null && !serverSocket.isClosed())
            out.println(clientId + PROTOCOL_SEPARATOR + PROTOCOL_RECEIVE_GAME);
        else {
            clientPanel.showDialogBox("You must be connected to server to receive a configuration!");
        }
    }

    /**
     * Notifies observers, namely the client UI, that the client has updated (but really the controller)
     */
    public void refreshClientUI() {
        // very ugly solution but i want the controller to tell the client when
        // it begins a new game so that the 'game management' buttons can be re-enabled
        setChanged();
        notifyObservers();
    }

}
