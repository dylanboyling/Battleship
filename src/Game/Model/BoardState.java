package Game.Model;

import Game.View.LogPanel;
import Game.Util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Game.Util.Constants.DEFAULT_GRID_DIMENSION;

/**
 * This class tracks the state of a grid for either the player or the system. Validates guesses.
 */
public class BoardState extends Observable {

    /**
     * Dimension of one side of the square grid, e.g. gridDimension x gridDimension
     */
    private int gridDimension;

    /**
     * 2D grid containing GridSquares. If an element is null, there is no ship there and
     * if it exists it will contain a GridSquare with information about the ship
     */
    private GridSquare[][] grid; // TODO track playerGrid and systemGrid. maybe make separate instances of BoardState for each?

    public final boolean isPlayer; // TODO private

    public List<String> events; // TODO private

    /**
     * Constructs a fresh state of the Battleship game and initializes an empty grid
     *
     * @param gridDimension Dimension for one side of the square grid
     */
    public BoardState(int gridDimension, boolean isPlayer){
        this.gridDimension = gridDimension;
        this.isPlayer = isPlayer;
        this.grid = new GridSquare[gridDimension][gridDimension];
        events = new ArrayList<String>();
    }

    public void reset(){
        this.gridDimension = DEFAULT_GRID_DIMENSION;
        this.grid = new GridSquare[DEFAULT_GRID_DIMENSION][DEFAULT_GRID_DIMENSION];
        setChanged();
        notifyObservers();
    }

    /**
     * Places a varying number of ships in random locations on the board. Number of ships placed changes on grid size.
     */
    public void randomizeShipLocations() {
        /* TODO when I will allow manual creation of ships, I should know how many ships of BoatSize there should be
            e.g. 1 ship of size D, 2 ships of size D-1. possibly store ships/ship sizes in an array?*/
        this.grid = new GridSquare[gridDimension][gridDimension];
        boolean isPlayer= true;
        final int DIM = gridDimension / 2;
        for(int boatSize = DIM; boatSize > 0; boatSize--){
            int debug_numberOfBoats = -1;
            for(int numberOfBoats = 1; numberOfBoats <= DIM - boatSize + 1; numberOfBoats++){
                createRandomBoat(boatSize, isPlayer);
                debug_numberOfBoats = numberOfBoats;
            }
            System.out.printf("[DEBUG] %d ships of size %d were created%n", debug_numberOfBoats, boatSize);
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Creates one random boat of a given size on the grid
     * @param boatSize Size of the boat to be created
     */
    private void createRandomBoat(int boatSize, boolean isPlayer){
        final boolean HORIZONTAL = true;
        final boolean VERTICAL = false;
        Random random = new Random();
        boolean boatCreated = false;

        while(!boatCreated){
            int randomRow = ThreadLocalRandom.current().nextInt(0, gridDimension);
            int randomCol = ThreadLocalRandom.current().nextInt(0, gridDimension);

            if(isLocationValid(randomRow, randomCol, boatSize, HORIZONTAL)){
                createBoat(randomRow, randomCol, boatSize, isPlayer, HORIZONTAL);
                boatCreated = true;
            } else if (isLocationValid(randomRow, randomCol, boatSize, VERTICAL)) {
                createBoat(randomRow, randomCol, boatSize, isPlayer, VERTICAL);
                boatCreated = true;
            }
        }
    }

    /**
     * Verifies if the (row, column) location of a proposed ship location is valid
     * @param row Row of proposed ship location
     * @param column Column of proposed ship location
     * @param boatSize Ship size
     * @param isHorizontal True if ship is horizontal (left to right), false if vertical (top to bottom)
     * @return true if ship can be placed, false otherwise
     */
    private boolean isLocationValid(int row, int column, int boatSize, boolean isHorizontal) {
        if (isHorizontal) {
            if (column + boatSize >= gridDimension) {
                return false;
            }

            for (int i = column; i < column + boatSize; i++) {
                if (grid[i][column] != null) {
                    return false;
                }
            }
        } else {
            if (row + boatSize >= gridDimension) {
                return false;
            }

            for (int i = row; i < row + boatSize; i++) {
                if (grid[row][i] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a boat square at the location (row, column)
     * @param row Row of proposed ship location
     * @param column Column of proposed ship location
     * @param boatSize Ship size
     * @param isPlayer true if ship belongs to a player, false otherwise
     * @param isHorizontal True if ship is horizontal (left to right), false if vertical (top to bottom)
     */
    private void createBoat(int row, int column, int boatSize, boolean isPlayer, boolean isHorizontal){
        if(isHorizontal){
            for(int i = 0; i < boatSize; i++){
                grid[row][column+i] = new GridSquare(boatSize, true, GridSquareStatus.ALIVE);
            }
        } else{
            for(int i = 0; i < boatSize; i++){
                grid[row+i][column] = new GridSquare(boatSize, true, GridSquareStatus.ALIVE);
            }
        }
        System.out.printf("[DEBUG] Boat of size %d was created at (%dx%d)%n", boatSize, row+1, column+1);
    }

    /**
     * Resizes the grid to a new dimension and resets the state of the grid
     * @param newGridDimension New dimension of the board
     */
    public void resizeGrid(final int newGridDimension){
        gridDimension = newGridDimension;
        System.out.printf("[DEBUG] New grid dimension is %dx%d", gridDimension, gridDimension);
        grid = new GridSquare[gridDimension][gridDimension];
        setChanged();
        notifyObservers();
    }

    public int getGridDimension(){
        return gridDimension;
    }

    /**
     * Returns the grid containing the current state of the game
     * @return Grid of GridSquares containing info about ships
     */
    public GridSquare[][] getGrid(){
        return this.grid;
    }

    /**
     * Validates a player's/system's guess by checking if the grid contains a ship or not
     * @param isPlayer Indicates if it is checking the player or the system's board
     * @param x X coordinate of the guess on the grid
     * @param y Y coordinate of the guess on the grid
     */
    public void validateGuess(boolean isPlayer, int x, int y){
        System.out.printf("[DEBUG] %s %s%d button was clicked%n", isPlayer ? "Human's" : "System's", Utils.getLetterCoordinate(x), y+1);
        updateHistoryPanel(String.format("%s %s%d button was clicked%n", isPlayer ? "Human's" : "System's", Utils.getLetterCoordinate(x), y+1));

        // TODO validate guess

        notifyObservers();
    }

    /**
     * Updates the history log for the game with the provided message
     * @param message Message to be added to the history log
     */
    public void updateHistoryPanel(String message){
        /* TODO does this belong in this class? maybe split into its own, which could also maybe clear the history panel
            upon starting a new game */
        events.add(message);
    }
}
