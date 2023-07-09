package Game.Model;

import Game.Model.Enums.GameStatus;

import java.util.Random;

/**
 * This class represents the current state of the game and tracks turns, score, and current status/phase of the game
 */
public class GameState {

    /**
     * Number of games that the player has won
     */
    private int playerGamesWon;

    /**
     * Number of games that the system has won
     */
    private int systemGamesWon;

    /**
     * Indicates if it is the player's turn or not
     */
    private boolean isPlayersTurn;

    /**
     * Current status of the game
     */
    private GameStatus status;

    /**
     * Creates a new GameState and randomly picks who goes first
     */
    public GameState(){
        Random coinFlip = new Random();
        isPlayersTurn = coinFlip.nextBoolean();
    }

    /**
     * Increments the number of games for the player or system by 1
     * @param isPlayer True if player won, false if system won
     */
    public void incrementWin(final boolean isPlayer){
        if(isPlayer)
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
     * @return True if it is the players turn, false otherwise
     */
    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    /**
     * Changes the status of the game to a new one
     *
     * @param gameStatus New status game is being changed to
     */
    public void setStatus(GameStatus gameStatus) {
        System.out.println("[DEBUG] Game status is being set to " + gameStatus);
        this.status = gameStatus;
    }
}
