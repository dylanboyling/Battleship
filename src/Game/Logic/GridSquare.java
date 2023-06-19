package Game.Logic;

/**
 * Represents a square on the battleship grid
 */
public class GridSquare {
    //TODO this class doesn't perform any logic, maybe it belongs more in the "model"? idk. revisit this idea.

    /**
     * Status if the ship piece on (x,y) is a ive or not, true if the ship is alive and false if not
     */
    private boolean isAlive;

    /**
     * True if square belongs to player, false if belongs to system
     */
    private final boolean isPlayer;

    /**
     * X coordinate of the square
     */
    private int x; // TODO assess usefulness of x and y, since GridSquares are contained in a 2D array

    /**
     * Y coordinate of the square
     */
    private int y;

    public GridSquare(boolean isPlayer, boolean isAlive, int x, int y){
        this.isPlayer = isPlayer;
        this.isAlive = isAlive;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the status of the ship part at this location. True if it is alive and false otherwise
     * @return Ship status on this grid square
     */
    public boolean isAlive(){
        return isAlive;
    }

    /**
     * Changes the status of the grid square
     * @param isAlive New status of the grid square
     */
    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    /**
     * Returns true if square belongs to the player, false if belongs to system
     * @return True if square belongs to player, false otherwise
     */
    public boolean isPlayer() {
        return isPlayer;
    }
}
