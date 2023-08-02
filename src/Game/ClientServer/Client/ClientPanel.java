package Game.ClientServer.Client;

import Game.BattleshipMVC.View.SplashScreens;
import Game.ClientServer.Config;
import Game.Util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class initializes the UI for the Battleship client
 */
public class ClientPanel implements Observer {
    /**
     * Client back-end which processes requests and connects to the server
     */
    private final Client client;

    /**
     * Main frame which contains client Ui
     */
    private JFrame main;

    /**
     * Connects to the server at the provided address and port number
     */
    private JButton connectButton;

    /**
     * Disconnects client from the server
     */
    private JButton endButton;

    /**
     * Client log which logs events related to connections, sending game data, etc
     */
    private JTextArea textArea;

    /**
     * Creates a new panel
     *
     * @param client Client
     */
    public ClientPanel(final Client client) {
        this.client = client;
    }

    /**
     * Creates and initializes the UI client
     */
    public void initializePanel() {
        main = new JFrame(Utils.getLocalizedString("client_title"));
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel clientLabel = initializeClientLabel();
        final Box connectionButtons = initializeConnectionRow();
        final Box gameConfigButtons = initializeGameManagementRow();
        final Box eventWindowBox = initializeEventBox();

        final JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        clientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        clientPanel.add(clientLabel);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clientPanel.add(connectionButtons);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clientPanel.add(gameConfigButtons);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clientPanel.add(eventWindowBox);

        main.add(clientPanel);
        main.pack();
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
    }

    /**
     * Initializes the Battleship client UI logo/label
     *
     * @return JLabel containing logo for Battleship client application
     */
    public JLabel initializeClientLabel() {
        ImageIcon clientIcon = null;
        try {
            InputStream stream = SplashScreens.class.getResourceAsStream("/res/client.png");
            clientIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JLabel clientIconLabel = new JLabel(clientIcon);
        clientIconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        return clientIconLabel;
    }

    /**
     * Initializes the row containing buttons relating to game and layout management
     *
     * @return Box containing buttons for game management
     */
    private Box initializeGameManagementRow() {
        final JButton newGameButton = new JButton(Utils.getLocalizedString("client_new_game"));
        final JButton sendGameButton = new JButton(Utils.getLocalizedString("client_send_game"));
        final JButton receiveLayoutButton = new JButton(Utils.getLocalizedString("client_receive_layout"));
        final JButton sendDataButton = new JButton(Utils.getLocalizedString("client_send_data"));
        final JButton playButton = new JButton(Utils.getLocalizedString("client_play"));

        newGameButton.addActionListener(e -> {
            updateLogPanel("Launching new MVC Battleship game ...\n");
            client.createNewGame();
            newGameButton.setEnabled(false);
        });
        receiveLayoutButton.addActionListener(e -> {
            client.receiveConfiguration();
        });
        sendGameButton.addActionListener(e -> {
            if (client.sendConfigurationToServer()) {
                updateLogPanel("Sending ship configuration to the server ...\n");
            }
        });
        sendDataButton.setEnabled(false); // TODO enable when game is over
        playButton.addActionListener(e -> {
            if (client.playGame()) {
                newGameButton.setEnabled(false);
                sendGameButton.setEnabled(false);
                receiveLayoutButton.setEnabled(false);
                sendDataButton.setEnabled(false);
                playButton.setEnabled(false);
                newGameButton.setEnabled(false);
            }
        });

        final Box gameConfigurationRow = Box.createHorizontalBox();
        gameConfigurationRow.add(newGameButton);
        gameConfigurationRow.add(Box.createRigidArea(new Dimension(10, 0)));
        gameConfigurationRow.add(sendGameButton);
        gameConfigurationRow.add(Box.createRigidArea(new Dimension(10, 0)));
        gameConfigurationRow.add(receiveLayoutButton);
        gameConfigurationRow.add(Box.createRigidArea(new Dimension(10, 0)));
        gameConfigurationRow.add(sendDataButton);
        gameConfigurationRow.add(Box.createRigidArea(new Dimension(10, 0)));
        gameConfigurationRow.add(playButton);

        return gameConfigurationRow;
    }

    /**
     * Initializes the row containing buttons related to managing the client's connection
     *
     * @return Box containing buttons related to managing the client's connection
     */
    private Box initializeConnectionRow() {
        final JLabel userLabel = new JLabel(Utils.getLocalizedString("client_user") + ":");
        final JTextField userField = new JTextField(5);
        final JLabel serverLabel = new JLabel(Utils.getLocalizedString("client_server") + ":");
        final JTextField serverField = new JTextField(5);
        final JLabel portLabel = new JLabel(Utils.getLocalizedString("port") + ":");
        final JTextField portField = new JTextField(5);
        connectButton = new JButton(Utils.getLocalizedString("client_connect"));
        endButton = new JButton(Utils.getLocalizedString("end_connection"));

        userField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                final int MAX = 12;
                if (userField.getText().length() >= MAX) {
                    e.consume();
                }
            }
        });
        userField.setText(Config.DEFAULT_USER);
        serverField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                final int MAX = 20;
                if (serverField.getText().length() >= MAX) {
                    e.consume();
                }
            }
        });
        serverField.setText(Config.DEFAULT_ADDRESS);

        portField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                final char c = e.getKeyChar();
                final int MAX = 5;

                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
                if (portField.getText().length() >= MAX) {
                    e.consume();
                }
            }
        });
        portField.setText(String.valueOf(Config.DEFAULT_PORT));
        portField.setEnabled(true);
        portField.setEditable(true);
        connectButton.addActionListener(e -> {
            if (serverField.getText().isEmpty() || portField.getText().isEmpty()) {
                System.out.println("[DEBUG] Must enter a server address and port to connect to a server");
            } else {
//                updateLogPanel(String.format("Connecting user %s to server at %s on port %d%n",
//                        userField.getText(), serverField.getText(), Integer.parseInt(portField.getText())));
                System.out.printf("[DEBUG] Connecting user %s to server at %s on port %d%n",
                        userField.getText(), serverField.getText(), Integer.parseInt(portField.getText()));
                client.connectToPort(userField.getText(), serverField.getText(), Integer.parseInt(portField.getText()));

            }
        });
        endButton.addActionListener(e -> {
            client.closeConnection();
        });
        endButton.setEnabled(false);

        final Box serverOptions = Box.createHorizontalBox();
        serverOptions.add(userLabel);
        serverOptions.add(Box.createRigidArea(new Dimension(3, 0)));
        serverOptions.add(userField);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(serverLabel);
        serverOptions.add(Box.createRigidArea(new Dimension(3, 0)));
        serverOptions.add(serverField);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(portLabel);
        serverOptions.add(portField);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(connectButton);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(endButton);

        return serverOptions;
    }

    /**
     * Initializes the window containing a scrollable text box for the client's history log
     *
     * @return Box containing the client event box for the client
     */
    public Box initializeEventBox() {
        final Box eventPanelBox = Box.createHorizontalBox();
        textArea = new JTextArea(7, 0);
        textArea.setEditable(false);

        final JScrollPane scroll = new JScrollPane(textArea);
        final Border scrollBorderTitle = BorderFactory.createTitledBorder("Server History Log");
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(scrollBorderTitle);
        eventPanelBox.add(scroll);

        return eventPanelBox;
    }

    /**
     * Updates the log window with a new entry
     *
     * @param event New log entry to be added to the window
     */
    public void updateLogPanel(final String event) {
        textArea.append(event);
    }

    /**
     * Displays a dialogue box containing a message to the user
     *
     * @param dialogMessage Message to be displayed to the user
     */
    public void showDialogBox(final String dialogMessage) {
        JOptionPane.showMessageDialog(main, dialogMessage);
    }

    /**
     * Enables/disables connect/end connection buttons depending on if the client is connected to a server
     *
     * @param isConnected True if client is connected, false otherwise
     */
    public void setConnectedState(final boolean isConnected) {
        connectButton.setEnabled(!isConnected);
        endButton.setEnabled(isConnected);
    }

    @Override
    public void update(Observable o, Object arg) {
        initializeGameManagementRow();
    }
}
