package Game.Model;

import Game.Model.Enums.GameStatus;
import Game.Model.Enums.Language;

import java.util.Random;

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
     * Current language that the game is being displayed in
     */
    private Language language; // TODO figure out localization (1pt - low priority)

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

    /**
     * Gets language that the game is currently in
     *
     * @return Current game language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Changes the language of the game to another one
     *
     * @param language New language game is to be displayed in
     */
    public void setLanguage(final Language language) {
        this.language = language;
    }
}
