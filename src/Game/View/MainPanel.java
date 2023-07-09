package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;
import Game.Model.DesignState;
import Game.Model.GameState;
import Game.Util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * This class creates and initializes the main panel which includes the game boards, event log, and options menu
 */
@SuppressWarnings("deprecation")
public class MainPanel extends JFrame implements Observer {
    /**
     * Width of the main window
     */
    public static final int MAIN_WINDOW_WIDTH = 1600;

    /**
     * Height of the main window
     */
    public static final int MAIN_WINDOW_HEIGHT = 1000;

    /**
     * Main frame which contains all UI elements
     */
    private JFrame main;

    /**
     * Displays move history and various information related to the game in a text box
     */
    private EventPanel eventPanel;

    /**
     * Grid of buttons for the player's game board
     */
    private GridPanel playerGrid;

    /**
     * Contains selection boxes and buttons to change the game's options
     */
    private OptionsPanel optionsPanel;

    /**
     * Grid of buttons for the system's game board
     */
    private GridPanel systemGrid;

    /**
     * Controller for the game which will process requests from the panel
     */
    private final BattleshipController controller;

    /**
     * Creates a new main panel and links it to the controller for the game
     * @param controller Controller for the Battleship game used to respond to requests from the main panel
     */
    public MainPanel(BattleshipController controller) {
        super();
        this.controller = controller;
    }

    /**
     * Initializes the main window and initializes different sections of the UI
     * @param gameState Current state of the game
     * @param designState Current state of the design phase
     * @param playerBoardState Current state of the player's board and their ships
     * @param systemBoardState Current state of the system's board and it's ships
     */
    public void initializePanel(GameState gameState, DesignState designState, BoardState playerBoardState,
                                BoardState systemBoardState) {
        setLookAndFeel();

        int w = (int) (MAIN_WINDOW_WIDTH / 2F);
        int h = (int) (MAIN_WINDOW_HEIGHT * 0.8);

        main = new JFrame(Utils.getLocalizedString("title_bar"));
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        MenuBar menuBar = new MenuBar();
        menuBar.initializeMenuBar(this, controller);
        main.setJMenuBar(menuBar);

        eventPanel = new EventPanel();
        eventPanel.initializePanel();
        eventPanel.setPreferredSize(new Dimension(w, (int) (MAIN_WINDOW_HEIGHT * 0.2F)));

        playerGrid = new GridPanel(controller);
        playerGrid.setPreferredSize(new Dimension(w, h));
        playerGrid.initializePanel(gameState, playerBoardState);
        playerBoardState.addObserver(this);

        systemGrid = new GridPanel(controller);
        systemGrid.setPreferredSize(new Dimension(w, h));
        systemGrid.initializePanel(gameState, systemBoardState);
        systemBoardState.addObserver(this);

        optionsPanel = new OptionsPanel(controller);
        optionsPanel.initializePanel(designState);
        optionsPanel.setPreferredSize(new Dimension(w, (int) (MAIN_WINDOW_HEIGHT * 0.2)));

        // First row (Player and System grids)
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        main.add(playerGrid, layoutConstraints);
        layoutConstraints.gridx = 1;
        main.add(systemGrid, layoutConstraints);

        // Second row (History panel and options panel)
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;
        layoutConstraints.fill = GridBagConstraints.BOTH;
        main.add(eventPanel, layoutConstraints);
        layoutConstraints.gridx = 1;
        main.add(optionsPanel, layoutConstraints);

        main.pack();
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
    }

    /**
     * Updates the main Battleship board, primarily used when the state of the game has changed and may need to be repainted.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BoardState) {
            System.out.println("[DEBUG] GameState has changed, refreshing grids");
            BoardState boardState = (BoardState) o;
            GameState gameState = controller.getGameState();
            if (boardState.isPlayer()) {
                playerGrid.initializePanel(gameState, boardState);
                optionsPanel.initializePanel(controller.getDesignState());
            } else
                systemGrid.initializePanel(gameState, boardState);
        }

        main.repaint();
        main.revalidate();
    }

    /**
     * Updates the event log window by outputting a message to it
     * @param event Game event that is to be displayed to the user
     */
    public void updateLogPanel(String event) {
        eventPanel.updateLogPanel(event + "\n");
        eventPanel.repaint();
        eventPanel.revalidate();
    }

    /**
     * Sets the look and feel of the game to match the operating system and it's settings that it's running on
     */
    private void setLookAndFeel() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
    }
}
