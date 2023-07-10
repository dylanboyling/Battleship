package Game.Model.Enums;

/**
 * Contains various statuses representing what stage the game is in
 */
public enum GameStatus {
    /**
     * User is in the DESIGN phase and is placing ships on the board
     */
    DESIGN,
    /**
     * User is in RANDOM phase and has randomized their ship locations
     */
    RANDOM,
    /**
     * Game is IN_PROGRESS and is currently being played, i.e. play button has been hit
     */
    IN_PROGRESS,
    /**
     * Game is in GAME_OVER status and the current game has ended
     */
    GAME_OVER
}
