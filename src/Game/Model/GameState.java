package Game.Model;

import Game.Model.Enums.GameStatus;

import java.util.Observable;
import java.util.Random;

/**
 * This class represents the current state of the game and tracks turns, score, and current status/phase of the game
 */
@SuppressWarnings("deprecation")
public class GameState extends Observable {

    /**
     * Tracks if locale was recently changed, true if yes false if no
     */
    private  boolean hasLocaleChanged;
    /**
     * Number of games that the player has won
     */
    private int playerGamesWon = 0;

    /**
     * Number of games that the system has won
     */
    private int systemGamesWon = 0;

    /**
     * Indicates if it is the player's turn or not
     */
    private boolean isPlayersTurn;

    /**
     * True if player won, false otherwise
     */
    private boolean didPlayerWin;

    /**
     * Current status of the game
     */
    private GameStatus status;

    /**
     * Creates a new GameState and randomly picks who go first
     */
    public GameState() {
        Random coinFlip = new Random();
        isPlayersTurn = coinFlip.nextBoolean();
    }

    /**
     * Gets total number of games won by the player
     *
     * @return Total number of games won by the player
     */
    public int getPlayerGamesWon() {
        return playerGamesWon;
    }

    /**
     * Gets total number of games won by the system
     *
     * @return Total number of games won by the system
     */
    public int getSystemGamesWon() {
        return systemGamesWon;
    }

    /**
     * Increments the number of games for the player or system by 1
     *
     * @param isPlayer True if player won, false if system won
     */
    public void incrementWin(final boolean isPlayer) {
        if (isPlayer)
            playerGamesWon++;
        else
            systemGamesWon++;
    }

    /**
     * Cycles the turn to the next player
     */
    public void nextTurn() {
        isPlayersTurn = !isPlayersTurn;
    }

    /**
     * Gets current status of the game
     *
     * @return Game status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Checks if it is the player's turn
     *
     * @return Boolean indicating if its the players turn
     */
    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    /**
     * Returns true if player won the game, false if they lost
     *
     * @return Boolean indicating if player won
     */
    public boolean didPlayerWin() {
        return didPlayerWin;
    }

    /**
     * Returns true if player won the game, false if they lost
     *
     * @param didPlayerWin true if player won, false if lost
     */
    public void setDidPlayerWin(boolean didPlayerWin) {
        this.didPlayerWin = didPlayerWin;
    }

    /**
     * Checks if locale has recently changed
     * @return True if locale has recently changed
     */
    public boolean hasLocaleChanged() {
        return hasLocaleChanged;
    }

    /**
     * Modifies the status of the locale changing flag. True if it has recently been changed and false otherwise
     * @param hasLocaleChanged True if recently changed, false otherwise
     */
    public void setHasLocaleChanged(boolean hasLocaleChanged) {
        this.hasLocaleChanged = hasLocaleChanged;
        setChanged();
        notifyObservers();
    }

    /**
     * Changes the status of the game to a new one
     *
     * @param gameStatus New status game is being changed to
     */
    public void setStatus(GameStatus gameStatus) {
        System.out.println("[DEBUG] Game status is being set to " + gameStatus);
        this.status = gameStatus;
        setChanged();
        notifyObservers();
    }
}
