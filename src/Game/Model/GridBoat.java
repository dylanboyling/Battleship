package Game.Model;

/**
 * Represents a boat of a given size during the design phase
 */
public class GridBoat {

    /**
     * Size of the boat
     */
    private final int boatSize;

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
