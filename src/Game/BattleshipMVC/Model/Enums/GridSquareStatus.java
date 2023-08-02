package Game.BattleshipMVC.Model.Enums;

/**
 * Contains statuses that a grid square may have
 */
public enum GridSquareStatus {
    /**
     * Grid square is ALIVE, it has not been hit
     */
    ALIVE,
    /**
     * Grid square is HIT, it has been hit
     */
    HIT,
    /**
     * Grid square is MISSED to mark an incorrect guess
     */
    MISSED
}
