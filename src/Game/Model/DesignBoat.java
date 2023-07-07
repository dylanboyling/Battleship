package Game.Model;

/**
 * Represents a boat of a given size during the design phase
 */
public class DesignBoat {

    /**
     * Size of the boat
     */
    private final int boatSize;

    /**
     * Creates a boat of a given size
     * @param boatSize Size of the boat
     */
    public DesignBoat(final int boatSize){
        this.boatSize = boatSize;
    }

    /**
     * Gets the size of the boat
     * @return Size of the boat
     */
    public int getBoatSize(){
        return boatSize;
    }
}
