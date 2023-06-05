package Game.Controller;

import Game.Model.BoardState;
import Game.View.GridPanel;
import Game.View.LogPanel;
import Game.View.MainPanel;
import Game.View.OptionsPanel;

import javax.swing.*;

import static Game.Util.Constants.DEFAULT_GRID_DIMENSION;

/**
 * Main class for the game Battleship
 * @author Dylan Boyling
 */
public class BattleshipController {

    /**
     * Width of the main window
     */
    public static final int MAIN_WINDOW_WIDTH  = 1200;

    /**
     * Height of the main window
     */
    public static final int MAIN_WINDOW_HEIGHT  = 800;

    /**
     * Title bar for main window
     */
    private static final String TITLE_BAR       = "Battleship by Dylan Boyling";

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
     * Manages player state of the game and validates moves
     */
    private BoardState playerBoardState;

    /**
     * Manages system state of the game and validates moves
     */
    private BoardState systemBoardState;

    private MainPanel main;

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

        final BattleshipController game = new BattleshipController();
        game.configure();
    }

    /**
     * Configures the primary window, initializes the UI elements, and initializes the game state
     */
    private void configure(){
        playerBoardState = new BoardState(DEFAULT_GRID_DIMENSION, true);
        systemBoardState = new BoardState(DEFAULT_GRID_DIMENSION, false);

        playerBoardState.randomizeShipLocations(); // TODO don't randomize when game starts, let user pick (keep for testing)
        systemBoardState.randomizeShipLocations();

        main = new MainPanel();
        main.initializePanel(this, playerBoardState, systemBoardState);
    }

    public void randomizePlayerShipLocations(){
        playerBoardState.randomizeShipLocations();
        main.updateLogPanel("User has randomized their ship locations\n");
    }

    public void changeBoardDimension(final int newBoardDimension){
        playerBoardState.resizeGrid(newBoardDimension);
        systemBoardState.resizeGrid(newBoardDimension);
        systemBoardState.randomizeShipLocations();
        main.updateLogPanel(String.format("Dimensions were changed to %dx%d%n", newBoardDimension, newBoardDimension));
    }

    public void changeLanguage(String language){
        // TODO do something, also use enums?
        main.updateLogPanel(String.format("Language was changed to %s%n", language));
    }

    public void resetGameBoards() {
        if (playerBoardState.getGridDimension() != DEFAULT_GRID_DIMENSION) {
            systemBoardState.reset();
            systemBoardState.randomizeShipLocations();
        }
        playerBoardState.reset();
        playerBoardState.updateHistoryPanel("User has reset the board\n");
    }

    public void validateGuess(boolean isPlayer, int x, int y) {
        // TODO do stuff
    }
}