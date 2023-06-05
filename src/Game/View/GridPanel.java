package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;
import Game.Model.GridSquare;
import Game.Model.GridSquareStatus;
import Game.Util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridPanel extends JPanel {

    /**
     * Default minimum size for one square of the battleship board
     */
    public static final int SQUARE_DIMENSION = 50;

/*    private final List<GridButton> */

    /**
     * Manages the state of the game
     */
    private final BoardState boardState;

    private final BattleshipController controller;

    /**
     * Default constructor for creating a GridPanel
     * @param boardState State of the current game
     */
    public GridPanel(final BattleshipController controller, final BoardState boardState){
        this.controller = controller;
        this.boardState = boardState;
    }

    /**
     * Initializes the game grid for ships
     *
     * @param dimension Dimension of one side of the square grid to be created
     * @param isPlayer True if human grid, false if system grid
     */
    public void initializePanel(final int dimension, final boolean isPlayer){
        GridSquare[][] grid = boardState.getGrid();
        removeAll();

        setLayout(new GridLayout(0, dimension+1, 2, 2));
        final JLabel emptyCornerLabel = new JLabel();
        emptyCornerLabel.setPreferredSize(new Dimension(30,30));
        add(emptyCornerLabel);

        for(int col = 0; col < grid.length; col++){
            final JLabel colHeaderLabel = new JLabel();
            colHeaderLabel.setText(String.valueOf(Utils.getLetterCoordinate(col)));
            colHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            colHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(colHeaderLabel);
        }

        for(int row = 0; row < grid.length; row++){
            final JLabel rowHeaderLabel = new JLabel();
            rowHeaderLabel.setText(String.format("%d", row+1));
            rowHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            rowHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(rowHeaderLabel);

            for(int col = 0; col < grid.length; col++){
                final GridButton gridButton = new GridButton(isPlayer, new Point(col, row));
                gridButton.addActionListener(e -> {
                    final int x = (int) gridButton.getCoordinates().getX();
                    final int y = (int) gridButton.getCoordinates().getY();
                    controller.validateGuess(isPlayer, x, y);
                });

                gridButton.setOpaque(true);
                gridButton.setBorderPainted(false);
                gridButton.setMargin(new Insets(0, 0, 0, 0));
                if(grid[row][col] != null ) {
                    if(grid[row][col].isAlive() == GridSquareStatus.ALIVE && isPlayer){
                        gridButton.setText(Integer.toString(grid[row][col].getShipSize()));
                        gridButton.setBackground(Color.gray);
                    }
                    else if(grid[row][col].isAlive() == GridSquareStatus.NOT_ALIVE) {
                        gridButton.setEnabled(false);
                        gridButton.setBackground(Color.red);
                    } else if(grid[row][col].isAlive() == GridSquareStatus.MISSED){
                        gridButton.setEnabled(false);
                        gridButton.setBackground(Color.blue);
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
