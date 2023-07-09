package Game.Controller;

import Game.Model.BoardState;
import Game.Model.DesignState;
import Game.Model.Enums.GameStatus;
import Game.Model.GameState;
import Game.Util.Utils;
import Game.View.MainPanel;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

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
        Locale.setDefault(new Locale("en", "CA"));
        final BattleshipController controller = new BattleshipController();
        controller.configure();
    }

    /**
     * Configures the primary window, initializes the UI elements, and initializes the game state
     */
    private void configure() {
        gameState = new GameState();
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
        mainWindow.updateLogPanel(Utils.getLocalizedString("user_reset"));
    }

    /**
     * Randomizes the location of the player's ships
     */
    public void randomizePlayerShipLocations() {
        gameState.setStatus(GameStatus.RANDOM);
        playerBoardState.randomizeShipLocations();
        mainWindow.updateLogPanel(Utils.getLocalizedString("user_randomize"));
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
            mainWindow.updateLogPanel(Utils.getLocalizedString("error_max_boats_placed"));
            return;
        }

        if (playerBoardState.placeShipOnBoard(y, x, boatSize, isHorizontal)) {
            mainWindow.updateLogPanel(String.format(Utils.getLocalizedString("boat_created"),
                    boatSize, Utils.getLetterCoordinate(x), y + 1));
            designState.setIsHorizontal(true);
        } else {
            mainWindow.updateLogPanel(String.format(Utils.getLocalizedString("boat_not_created"),
                    boatSize, Utils.getLetterCoordinate(x), y + 1));
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
            mainWindow.updateLogPanel((String.format(Utils.getLocalizedString("guess_hit"),
                    !isPlayersBoard ? Utils.getLocalizedString("player") : Utils.getLocalizedString("system"),
                    isPlayersBoard ? Utils.getLocalizedString("player_possessive") : Utils.getLocalizedString("system_possessive"),
                    Utils.getLetterCoordinate(x), y + 1)));
        } else {
            mainWindow.updateLogPanel((String.format(Utils.getLocalizedString("guess_miss"),
                    !isPlayersBoard ? Utils.getLocalizedString("player") : Utils.getLocalizedString("system"),
                    Utils.getLetterCoordinate(x), y + 1)));
        }
        // TODO after validating a guess, the game state should be validated (somewhere) to determine if end of game conditions have been met
    }

    /**
     * System guesses a location on the player's board by randomly choosing a location
     */
    public void systemLocationGuess(){
        final boolean isPlayersBoard = true;
        final int randomRow = ThreadLocalRandom.current().nextInt(0, playerBoardState.getGridDimension());
        final int randomCol = ThreadLocalRandom.current().nextInt(0, playerBoardState.getGridDimension());
        validateGuess(isPlayersBoard, randomCol, randomRow);
    }

    /**
     * If player has played all of their ships, the game of Battleship begins
     */
    public void beginGame() {
        // TODO start a timer
        if(gameState.getStatus() == GameStatus.RANDOM || playerBoardState.isDesignBoatsEmpty()) {
            mainWindow.updateLogPanel("Player has begun the game!");
            mainWindow.updateLogPanel(String.format(Utils.getLocalizedString("first_turn"),
                    gameState.isPlayersTurn() ? Utils.getLocalizedString("player") : Utils.getLocalizedString("system")));
            gameState.setStatus(GameStatus.IN_PROGRESS);
            playerBoardState.notifyObservers(); // not ideal, but if GameState notifies observers MainPanel needs board states passed too
            systemBoardState.notifyObservers();
        } else {
            mainWindow.updateLogPanel(Utils.getLocalizedString("error_boats_missing"));
        }
    }

    /**
     * Ends the game and displays the solution to the opposing player's board
     */
    public void displaySolution(){
        mainWindow.updateLogPanel(Utils.getLocalizedString("player_forfeit"));
        gameState.setStatus(GameStatus.GAME_OVER);
        playerBoardState.notifyObservers(); // not ideal, but if GameState notifies observers MainPanel needs board states passed too
        systemBoardState.notifyObservers();
    }

    /**
     * Changes the game status to be in DESIGN mode and clears the board in preparation
     */
    public void enterDesignMode() {
        mainWindow.updateLogPanel(Utils.getLocalizedString("design_mode"));
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
        mainWindow.updateLogPanel(String.format(Utils.getLocalizedString("dimensions_changed"), newBoardDimension, newBoardDimension));
    }

    /**
     * Changes the language of the game on menus, event log, etc
     *
     * @param newDefaultLocale Locale that the game is being set to
     */
    public void changeLanguage(final Locale newDefaultLocale) {
        Locale.setDefault(newDefaultLocale);
        mainWindow.updateLogPanel(String.format(Utils.getLocalizedString("language_changed"), newDefaultLocale));
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