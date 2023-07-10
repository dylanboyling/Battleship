package Game.Model;

public class DesignState {
    /**
     * True if the selected boat is horizontal, false otherwise
     */
    private Boolean isHorizontal;

    /**
     * Size of the selected boat
     */
    private int boatSize;

    public DesignState() {
        isHorizontal = true;
        boatSize = 1;
    }

    /**
     * Changes the currently selected ship to be oriented horizontally
     *
     * @param isHorizontal True if selected ship is horizontal, false otherwise
     */
    public void setIsHorizontal(final Boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    /**
     * Returns true if the currently selected direction in design mode is horizontal or not
     *
     * @return True if currently selected ship is horizontal, false otherwise
     */
    public Boolean getIsHorizontal() {
        return isHorizontal;
    }

    /**
     * Changes the size of the selected boat
     *
     * @param boatSize Size of the selected boat
     */
    public void setBoatSize(final int boatSize) {
        this.boatSize = boatSize;
    }

    /**
     * Gets the size of the currently selected boat
     *
     * @return Size of the selected boat
     */
    public int getBoatSize() {
        return boatSize;
    }
}
