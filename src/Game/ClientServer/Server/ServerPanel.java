package Game.ClientServer.Server;

import Game.BattleshipMVC.View.SplashScreens;
import Game.ClientServer.Config;
import Game.Util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;

public class ServerPanel {
    /**
     * Main window which contains all of the server UI elements
     */
    JFrame main;

    /**
     * Backend for the server that this UI is for
     */
    Server server;

    /**
     * Server log which logs events related to connections, sending game data, etc
     */
    private JTextArea textArea;

    /**
     * Creates a new instance of the server UI for the backend of the server
     *
     * @param server Server that the UI is being created for
     */
    public ServerPanel(final Server server) {
        this.server = server;
    }

    /**
     * Creates and initializes the UI for the server
     */
    public void initializePanel() {
        main = new JFrame(Utils.getLocalizedString("server_title"));
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel serverLabel = initializeServerLabel();
        final Box serverOptions = initializeServerButtons();
        final Box eventWindowBox = initializeEventBox();

        final JPanel serverPanel = new JPanel();
        serverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.Y_AXIS));
        serverPanel.add(serverLabel);
        serverPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        serverPanel.add(serverOptions);
        serverPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        serverPanel.add(eventWindowBox);

        main.add(serverPanel);
        main.pack();
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
    }

    /**
     * Creates and initializes the label containing the logo for the Battleship server
     *
     * @return Label containing Battleship server logo
     */
    public JLabel initializeServerLabel() {
        ImageIcon loadingIcon = null;
        try {
            final InputStream stream = SplashScreens.class.getResourceAsStream("/res/server.png");
            loadingIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JLabel aboutIconLabel = new JLabel(loadingIcon);
        aboutIconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        return aboutIconLabel;
    }

    /**
     * Creates and initializes the server buttons to start or end connections
     *
     * @return Box containing all buttons for managing the server
     */
    public Box initializeServerButtons() {
        final JLabel portLabel = new JLabel(Utils.getLocalizedString("port") + ":");
        final JTextField portField = new JTextField(5);
        final JButton startButton = new JButton(Utils.getLocalizedString("server_start"));
        final JButton resultsButton = new JButton(Utils.getLocalizedString("server_results")); // not implemented
        final JCheckBox finalizeCheck = new JCheckBox(Utils.getLocalizedString("server_finalize"));
        final JButton endButton = new JButton(Utils.getLocalizedString("end_connection"));

        portField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                final char c = e.getKeyChar();
                final int MAX = 5;
                // todo prevent long numbers and strings from being copy pasted in (too lazy in hindsight)
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

        startButton.addActionListener(e -> {
            if (!portField.getText().isEmpty()) {
                System.out.println("[DEBUG] Attempting to listen to connections on port " + portField.getText());
                server.listenForConnections(Integer.parseInt(portField.getText()));
            } else {
                System.out.println("[DEBUG] Must enter a port to listen for connections on");
                showDialogBox("Must enter a port to listen for connections on");
            }
        });
        resultsButton.setEnabled(false);
        finalizeCheck.addItemListener(e -> {
            server.setFinalized(e.getStateChange() == ItemEvent.SELECTED);
        });
        endButton.addActionListener(e -> {
            server.closeConnections();
        });

        final Box serverOptions = Box.createHorizontalBox();
        serverOptions.add(portLabel);
        serverOptions.add(Box.createRigidArea(new Dimension(3, 0)));
        serverOptions.add(portField);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(startButton);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(resultsButton);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(finalizeCheck);
        serverOptions.add(Box.createRigidArea(new Dimension(5, 0)));
        serverOptions.add(endButton);

        return serverOptions;
    }

    /**
     * Initializes the window containing a scrollable text box for the client's history log
     *
     * @return Box containing event log which has server history
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

}
