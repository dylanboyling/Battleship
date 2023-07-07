package Game.Controller;

import Game.Model.BoardState;
import Game.Model.DesignState;
import Game.Model.Enums.GameStatus;
import Game.Model.Enums.Language;
import Game.Model.GameState;
import Game.Util.Utils;
import Game.View.MainPanel;

import java.util.List;

import static Game.Util.Constants.DEFAULT_GRID_DIMENSION;

/**
 * Main class for the game Battleship
 *
 * @author Dylan Boyling
 */
public class BattleshipController {

    /**
     * Main window that the game is played in
     */
    private MainPanel mainWindow;

    /**
     * Manages player state of the game and validates moves
     */
    private BoardState playerBoardState;

    /**
     * Manages system state of the game and validates moves
     */
    private BoardState systemBoardState;

    /**
     * Manages and tracks the state of the game
     */
    private GameState gameState;

    /**
     * Manages the design state and currently selected design options
     */
    private DesignState designState;

    /**
     * Entry point for the game, launches the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        final BattleshipController controller = new BattleshipController();
        controller.configure();
    }

    /**
     * Configures the primary window, initializes the UI elements, and initializes the game state
     */
    private void configure() {
        gameState = new GameState();
        gameState.setLanguage(Language.ENGLISH);
        gameState.setStatus(GameStatus.DESIGN);

        playerBoardState = new BoardState(true);
        systemBoardState = new BoardState(false);
        systemBoardState.randomizeShipLocations();

        designState = new DesignState();

        mainWindow = new MainPanel(this);
        mainWindow.initializePanel(gameState, designState, playerBoardState, systemBoardState);
    }

    /**
     * Clears the player's board by removing all ships on it
     */
    public void clearPlayerBoard() {
        playerBoardState.clearBoard();
    }

    /**
     * Resets both game boards to their default state
     */
    public void resetGameBoards() {
        gameState.setStatus(GameStatus.DESIGN);
        if (playerBoardState.getGridDimension() != DEFAULT_GRID_DIMENSION) {
            systemBoardState.reset();
            systemBoardState.randomizeShipLocations();
        }
        playerBoardState.reset();
        mainWindow.updateLogPanel("User has reset the board");
    }

    /**
     * Randomizes the location of the player's ships
     */
    public void randomizePlayerShipLocations() {
        gameState.setStatus(GameStatus.RANDOM);
        playerBoardState.randomizeShipLocations();
        mainWindow.updateLogPanel("User has randomized their ship locations");
    }

    /**
     * Responds clicked square event and responds based on what state the game is in
     * @param isPlayersBoard True if square being clicked belongs to player, false otherwise
     * @param x X coordinate of clicked square
     * @param y Y coordinate of clicked square
     */
    public void handleGridClick(final boolean isPlayersBoard, final int x, final int y) {
        if (gameState.getStatus() == GameStatus.DESIGN && isPlayersBoard) {
            placeShip(x, y);
        } else if (gameState.getStatus() == GameStatus.IN_PROGRESS && !isPlayersBoard && gameState.isPlayersTurn()) {
            validateGuess(isPlayersBoard, x, y);
        }
    }

    /**
     * Attempts to place a ship on the player's board at coordinates (x,y)
     *
     * @param x X coordinate that the player would like to place their ship at
     * @param y Y coordinate that the player would like to place their ship at
     */
    public void placeShip(final int x, final int y) {
        final boolean isHorizontal = designState.getIsHorizontal();
        final int boatSize = designState.getBoatSize();

        if(playerBoardState.numberOfBoatSizesRemaining(boatSize) == 0){
            mainWindow.updateLogPanel("Error: You can not place any more boats of this size");
            return;
        }

        if (playerBoardState.placeShipOnBoard(y, x, boatSize, isHorizontal)) {
            mainWindow.updateLogPanel(String.format("Ship of size %d created at %s%d", boatSize, Utils.getLetterCoordinate(x), y + 1));
            designState.setIsHorizontal(true);
        } else {
            mainWindow.updateLogPanel(String.format("Ship of size %d cannot be placed at %s%d", boatSize, Utils.getLetterCoordinate(x), y + 1));
        }
    }

    /**
     * Validates a guess on the player's or system's game board
     *
     * @param isPlayersBoard True if square being guessed belongs to player, false otherwise
     * @param x              X coordinate of the square being guessed
     * @param y              Y coordinate of the square being guessed
     */
    public void validateGuess(final boolean isPlayersBoard, final int x, final int y) {
        System.out.printf("[DEBUG] Validating %s guess at %s%d%n", !isPlayersBoard ? "Human's" : "System's", Utils.getLetterCoordinate(x), y + 1);
        BoardState boardState = isPlayersBoard ? playerBoardState : systemBoardState;
        if (boardState.validateGuess(x, y)) {
            mainWindow.updateLogPanel((String.format("%s successfully hit the player's ship at %s%d", !isPlayersBoard ? "Player" : "System",
                    Utils.getLetterCoordinate(x), y + 1)));
        } else {
            mainWindow.updateLogPanel((String.format("%s guessed %s%d and missed", !isPlayersBoard ? "Player" : "System",
                    Utils.getLetterCoordinate(x), y + 1)));
        }
        // TODO after validating a guess, the game state should be validated (somewhere) to determine if end of game conditions have been met
    }

    /**
     * If player has played all of their ships, the game of Battleship begins
     */
    public void beginGame() {
        // TODO start a timer
        if(gameState.getStatus() == GameStatus.RANDOM || playerBoardState.isDesignBoatsEmpty()) {
            mainWindow.updateLogPanel("Player has begun the game!");
            mainWindow.updateLogPanel(String.format("A coin has been flipped and the %s can make the first guess!",
                    gameState.isPlayersTurn() ? "Player" : "System"));
            gameState.setStatus(GameStatus.IN_PROGRESS);
            playerBoardState.notifyObservers(); // not ideal, but if GameState notifies observers MainPanel needs board states passed too
            systemBoardState.notifyObservers();
        } else {
            mainWindow.updateLogPanel("Error: Game cannot start yet, all of your ships must be placed on the board");
        }
    }

    /**
     * Changes the game status to be in DESIGN mode and clears the board in preparation
     */
    public void enterDesignMode() {
        gameState.setStatus(GameStatus.DESIGN);
        playerBoardState.clearBoard();
    }

    /**
     * Sets the orientation of the selected boat in design mode
     * @param isHorizontal True if boat is horizontal, false otherwise
     */
    public void setSelectedBoatOrientation(final boolean isHorizontal){
        designState.setIsHorizontal(isHorizontal);
    }

    /**
     * Sets the selected boat size to a new value in design mode
     * @param boatSize Size of the currently selected boat
     */
    public void setSelectedBoatSize(final int boatSize){
        designState.setBoatSize(boatSize);
    }

    /**
     * Returns the number of boats of size boatSize that can still be placed on the board
     * @param boatSize Size of the boat
     * @return Number of boats with size boatSize
     */
    public int getBoatsRemaining(final int boatSize){
        return playerBoardState.numberOfBoatSizesRemaining(boatSize);
    }

    /**
     * Gets a list of unique boat sizes that can be placed on the game board
     * @return List of unique boat sizes that can be placed on the game board
     */
    public List<Integer> getBoatSizeOptions(){
        return playerBoardState.getBoatSizeOptions();
    }

    /**
     * Gets the current dimension of the grid
     * @return Dimension of the grid
     */
    public int getDimension(){
        return playerBoardState.getGridDimension();
    }

   /**
     * Changes the dimension of the board and replaces the system's ship on the new board
     *
     * @param newBoardDimension Dimension that the boards are to be changed to
     */
    public void changeBoardDimension(final int newBoardDimension) {
        playerBoardState.resizeGrid(newBoardDimension);
        systemBoardState.resizeGrid(newBoardDimension);
        systemBoardState.randomizeShipLocations();
        mainWindow.updateLogPanel(String.format("Dimensions were changed to %dx%d", newBoardDimension, newBoardDimension));
    }

    /**
     * Changes the language of the game on menus, event log, etc
     *
     * @param language Language that the game is being set to
     */
    public void changeLanguage(final Language language) {
        gameState.setLanguage(language);
        mainWindow.updateLogPanel(String.format("Language was changed to %s", language));
    }

    /**
     * Returns the current state of the game
     *
     * @return State of the game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Returns the current state of the design phase
     * @return Current state of the design phase
     */
    public DesignState getDesignState(){
        return designState;
    }
}