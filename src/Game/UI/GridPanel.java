package Game.UI;

import Game.Logic.GameState;
import Game.Logic.GridSquare;
import Game.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridPanel extends JPanel {

    /**
     * Default minimum size for one square of the battleship board
     */
    public static final int SQUARE_DIMENSION = 50;

    /**
     * Manages the state of the game
     */
    private final GameState gameState;

    /**
     * Default constructor for creating a GridPanel
     * @param gameState State of the current game
     */
    public GridPanel(final GameState gameState){
        this.gameState = gameState;
    }

    /**
     * Initializes the game grid for ships
     *
     * @param dimension Dimension of one side of the square grid to be created
     * @param isPlayer True if human grid, false if system grid
     */
    public void initializePanel(final int dimension, final boolean isPlayer){
        GridSquare[][] grid = gameState.getGrid();

        setLayout(new GridLayout(0, dimension+1, 2, 2));

        final JLabel emptyCornerLabel = new JLabel();
        emptyCornerLabel.setPreferredSize(new Dimension(30,30));
        add(emptyCornerLabel);

        for(int col = 0; col < grid.length; col++){
            final JLabel colHeaderLabel = new JLabel();
            colHeaderLabel.setText(Utils.getLetterCoordinate(col+1));
            colHeaderLabel.setPreferredSize(new Dimension(SQUARE_DIMENSION,SQUARE_DIMENSION));
            colHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            colHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(colHeaderLabel);
        }

        for(int row = 0; row < grid.length; row++){
            final JLabel rowHeaderLabel = new JLabel();
            rowHeaderLabel.setText(String.format("%d", row+1));
            rowHeaderLabel.setPreferredSize(new Dimension(SQUARE_DIMENSION,SQUARE_DIMENSION));
            rowHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            rowHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(rowHeaderLabel);

            for(int col = 0; col < grid.length; col++){
                final GridButton gridButton = new GridButton(isPlayer, new Point(col, row));
                gridButton.setPreferredSize(new Dimension(SQUARE_DIMENSION,SQUARE_DIMENSION));
                gridButton.addActionListener(e -> {
                    final int x = (int) gridButton.getCoordinates().getX();
                    final int y = (int) gridButton.getCoordinates().getY();
                    gameState.validateGuess(isPlayer, x, y);
                });

                gridButton.setOpaque(true);
                if(grid[row][col] != null ) {
                    if(grid[row][col].isAlive() && isPlayer){
                        gridButton.setBackground(Color.green);
                        gridButton.setBorderPainted(false);
                    }
                    else if(!grid[row][col].isAlive()) {
                        gridButton.setEnabled(false);
                        gridButton.setBackground(Color.red);
                        gridButton.setBorderPainted(false);
                    }
                }

                add(gridButton);
            }
        }

        final Border gridTitle = BorderFactory.createTitledBorder(isPlayer ? "Player" : "System");
        setBorder(gridTitle);
    }

    /**
     * Grid button represents a clickable square on a player's board.
     */
    private static class GridButton extends JButton {

        /**
         * Indicates whether button belongs to human player (true) or system (false)
         */
        private boolean isPlayer; // TODO rename something better

        /**
         * Point containing (x,y) location on player's board
         */
        private final Point coordinates;

        /**
         * Constructor for the grid button
         * @param isPlayer true if human player, false if system
         * @param coordinates (x,y) coordinates corresponding to the game board
         */
        public GridButton(final boolean isPlayer, final Point coordinates) {
            this.isPlayer = isPlayer;
            this.coordinates = coordinates;
        }

        /**
         * Default constructor for button with coordinates
         * @param x X location of button on game grid
         * @param y Y location of button on game grid
         */
        public GridButton(final int x, final int y) {
            this.coordinates = new Point(x, y);
        }

        /**
         * Gets coordinates for button on the game grid
         * @return Integer coordinates of button on game grid
         */
        public Point getCoordinates() {
            return coordinates;
        }

        /**
         * Gets isPlayer status for button
         * @return True if button belongs to player, false if belongs to system
         */
        public boolean isPlayer() {
            return isPlayer;
        }
    }
}
