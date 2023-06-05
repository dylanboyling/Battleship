package Game;

import Game.Logic.GameState;
import Game.UI.GridPanel;
import Game.UI.LogPanel;
import Game.UI.OptionsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Main class for the game Battleship
 * @author Dylan Boyling
 */
public class Battleship implements Observer {

    /**
     * Width of the main window
     */
    public static final int MAIN_WINDOW_WIDTH   = 1200;

    /**
     * Height of the main window
     */
    public static final int MAIN_WINDOW_HEIGHT  = 800;

    /**
     * Title bar for main window
     */
    private static final String TITLE_BAR       = "Battleship by Dylan Boyling";

    /**
     * Default dimension of the Battleship grid
     */
    private static final int DEFAULT_DIMENSION  = 10;

    /**
     * Displays move history and various information related to the game in a text box
     */
    private LogPanel logPanel;

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
     * Manages state of the game and validates moves
     */
    private GameState gameState;

    /**
     * Entry point for the game, launches the application.
     * @param args Command line arguments
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }});

        final Battleship game = new Battleship();
        game.configure();
    }

    /**
     * Configures the primary window, initializes the UI elements, and initializes the game state
     */
    private void configure(){
        final JFrame main = new JFrame(TITLE_BAR);
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main.setPreferredSize(new Dimension(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT));
        main.setLayout(new GridBagLayout());
        final GridBagConstraints layoutConstraints = new GridBagConstraints();

        logPanel = new LogPanel();
        logPanel.initializePanel();

        gameState = new GameState(logPanel, DEFAULT_DIMENSION);
        gameState.addObserver(this);
        gameState.randomizeShipLocations();

        playerGrid = new GridPanel(gameState);
        playerGrid.initializePanel(DEFAULT_DIMENSION,true);

        systemGrid = new GridPanel(gameState);
        systemGrid.initializePanel(DEFAULT_DIMENSION,false);

        optionsPanel = new OptionsPanel(gameState);
        optionsPanel.initializePanel();

        // First row (Player and System grids)
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        main.add(playerGrid, layoutConstraints);

        layoutConstraints.gridx = 1;
        main.add(systemGrid, layoutConstraints);

        // Second row (History panel and options panel)
        layoutConstraints.gridx=0;
        layoutConstraints.gridy=1;
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1.0;
        layoutConstraints.weighty = 1.0;
        main.add(logPanel, layoutConstraints);

        layoutConstraints.gridx=1;
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1.0;
        layoutConstraints.weighty = 1.0;
        main.add(optionsPanel, layoutConstraints);

        main.pack();
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
    }

    /**
     * Updates the main Battleship board, primarily used when the state of the game has changed and may need to be repainted.
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof GameState){
            // TODO do something
        }
    }
}