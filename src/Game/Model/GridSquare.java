package Game.Model;

/**
 * Represents a square on the battleship grid
 */
public class GridSquare {
    /**
     * Status if the ship piece on (x,y) is a ive or not, true if the ship is alive and false if not
     */
    private GridSquareStatus isAlive;

    /**
     * True if square belongs to player, false if belongs to system
     */
    private final boolean isPlayer;

    private final int shipSize;



    /**
     * does stuff
     * @param shipSize
     * @param isPlayer
     * @param isAlive
     */
    public GridSquare(int shipSize, boolean isPlayer, GridSquareStatus isAlive){
        this.shipSize = shipSize;
        this.isPlayer = isPlayer;
        this.isAlive = isAlive;
    }

    public int getShipSize() {
        return shipSize;
    }

    /**
     * Returns the status of the ship part at this location. True if it is alive and false otherwise
     * @return Ship status on this grid square
     */
    public GridSquareStatus isAlive(){
        return isAlive;
    }

    /**
     * Changes the status of the grid square
     * @param isAlive New status of the grid square
     */
    public void setAlive(GridSquareStatus isAlive){
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
