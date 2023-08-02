package Game.BattleshipMVC.Model;

/**
 * Represents a boat of a given size during the design phase
 */
public class GridBoat {

    /**
     * Row boat is located
     */
    private int row;

    /**
     * Column boat is located
     */
    private int column;

    /**
     * Size of the boat
     */
    private final int boatSize;

    /**
     * True if boat is horizontal, false if it is vertical
     */
    private boolean isHorizontal;

    /**
     * Health of the boat
     */
    private int boatHealth;

    /**
     * Creates a boat of a given size
     *
     * @param boatSize Size of the boat
     */
    public GridBoat(final int boatSize) {
        this.boatSize = boatSize;
        this.boatHealth = boatSize;
    }

    public GridBoat(int row, int column, int boatSize, boolean isHorizontal) {
        this.row = row;
        this.column = column;
        this.boatSize = boatSize;
        this.isHorizontal = isHorizontal;
        this.boatHealth = boatSize;
    }

    /**
     * Returns row boat is located
     *
     * @return Row boat is located
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns column boat is located
     *
     * @return Column boat is located
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets status on if boat is horizontal or not
     *
     * @return True if boat is horizontal, false if it is vertical
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Gets the size of the boat
     *
     * @return Size of the boat
     */
    public int getBoatSize() {
        return boatSize;
    }

    /**
     * Returns boat health (number squares that have not been hit)
     *
     * @return Boat health (number squares that have not been hit)
     */
    public int getBoatHealth() {
        return boatHealth;
    }

    /**
     * Decrements the boat's health by 1 and returns its new health points
     */
    public void takeHit() {
        boatHealth--;
    }
}
