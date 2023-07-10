package Game.Model;

import Game.Model.Enums.GridSquareStatus;

/**
 * Represents a square on the battleship grid
 */
public class GridSquare {
    /**
     * Boat that this square belongs to
     */
    private final GridBoat gridBoat;
    /**
     * Status if the ship piece on (x,y) is a ive or not, true if the ship is alive and false if not
     */
    private GridSquareStatus status;

    /**
     * True if square belongs to player, false if belongs to system
     */
    private final boolean isPlayer;

    /**
     * Size of the ship that this square belongs to
     */
    private final int shipSize;

    /**
     * Creates a new grid square for the Battleship board
     *
     * @param gridBoat Boat that this square belongs to
     * @param shipSize Size of the ship that this square belongs to
     * @param isPlayer True if the square belongs to the player, false otherwise
     * @param status   True if the square has not been hit yet, false if it has
     */
    public GridSquare(final GridBoat gridBoat, int shipSize, boolean isPlayer, GridSquareStatus status) {
        this.gridBoat = gridBoat;
        this.shipSize = shipSize;
        this.isPlayer = isPlayer;
        this.status = status;
    }

    /**
     * Gets the size of the ship this square belongs to
     *
     * @return Size of the ship this square belongs to
     */
    public int getShipSize() {
        return shipSize;
    }

    /**
     * Returns the status of the ship part at this location. True if it is alive and false otherwise
     *
     * @return Ship status on this grid square
     */
    public GridSquareStatus getStatus() {
        return status;
    }

    /**
     * Changes the status of the grid square
     *
     * @param status New status of the grid square
     */
    public void setStatus(GridSquareStatus status) {
        this.status = status;
    }

    /**
     * Returns true if square belongs to the player, false if belongs to system
     *
     * @return True if square belongs to player, false otherwise
     */
    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * Returns the grid boat that this square belongs to
     *
     * @return Grid boat the square belongs to
     */
    public GridBoat getGridBoat() {
        return gridBoat;
    }
}
