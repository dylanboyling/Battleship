package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;
import Game.Model.Enums.GameStatus;
import Game.Model.Enums.GridSquareStatus;
import Game.Model.GameState;
import Game.Model.GridSquare;
import Game.Util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridPanel extends JPanel {
    private final BattleshipController controller;

    /**
     * Default constructor for creating a GridPanel
     *
     * @param controller
     */
    public GridPanel(final BattleshipController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the game grid for ships
     *
     * @param boardState
     */
    public void initializePanel(final GameState gameState, final BoardState boardState) {
        GridSquare[][] grid = boardState.getGrid();
        final int dimension = boardState.getGridDimension();
        final boolean isPlayer = boardState.isPlayer();

        removeAll();
        setLayout(new GridLayout(0, dimension + 1, 2, 2));
        final JLabel emptyCornerLabel = new JLabel();
        emptyCornerLabel.setPreferredSize(new Dimension(30, 30));
        add(emptyCornerLabel);

        for (int col = 0; col < grid.length; col++) {
            final JLabel colHeaderLabel = new JLabel();
            colHeaderLabel.setText(String.valueOf(Utils.getLetterCoordinate(col)));
            colHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            colHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(colHeaderLabel);
        }

        for (int row = 0; row < grid.length; row++) {
            final JLabel rowHeaderLabel = new JLabel();
            rowHeaderLabel.setText(String.format("%d", row + 1));
            rowHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            rowHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(rowHeaderLabel);

            for (int col = 0; col < grid.length; col++) {
                final GridButton gridButton = new GridButton(isPlayer, new Point(col, row));
                gridButton.setMargin(new Insets(0, 0, 0, 0));
                gridButton.addActionListener(e -> {
                    final int x = (int) gridButton.getCoordinates().getX();
                    final int y = (int) gridButton.getCoordinates().getY();
                    controller.handleGridClick(isPlayer, x, y);
                });

                final GridSquare gridSquare = grid[row][col];
                final GameStatus gameStatus = gameState.getStatus();
                if (gameStatus != GameStatus.IN_PROGRESS && !isPlayer) {
                    gridButton.setEnabled(false);
                }

                if (gridSquare != null) {
                    if (isPlayer && gridSquare.isAlive() == GridSquareStatus.ALIVE) {
                        gridButton.setText(Integer.toString(grid[row][col].getShipSize()));
                        gridButton.setBackground(Color.gray);
                        gridButton.setOpaque(true);
                        gridButton.setBorderPainted(false);
                    } else if (gridSquare.isAlive() == GridSquareStatus.HIT) {
                        gridButton.setEnabled(false);
                        gridButton.setBackground(Color.red);
                        gridButton.setOpaque(true);
                        gridButton.setBorderPainted(false);
                    } else if (gridSquare.isAlive() == GridSquareStatus.MISSED) {
                        gridButton.setEnabled(false);
                        gridButton.setBackground(Color.blue);
                        gridButton.setOpaque(true);
                        gridButton.setBorderPainted(false);
                    }
                } else if (gameStatus == GameStatus.IN_PROGRESS && gameState.isPlayersTurn() && !isPlayer){
                    gridButton.setEnabled(true);
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
        private boolean isPlayer;

        /**
         * Point containing (x,y) location on player's board
         */
        private final Point coordinates;

        /**
         * Constructor for the grid button
         *
         * @param isPlayer    true if human player, false if system
         * @param coordinates (x,y) coordinates corresponding to the game board
         */
        public GridButton(final boolean isPlayer, final Point coordinates) {
            this.isPlayer = isPlayer;
            this.coordinates = coordinates;
        }

        /**
         * Default constructor for button with coordinates
         *
         * @param x X location of button on game grid
         * @param y Y location of button on game grid
         */
        public GridButton(final int x, final int y) {
            this.coordinates = new Point(x, y);
        }

        /**
         * Gets coordinates for button on the game grid
         *
         * @return Integer coordinates of button on game grid
         */
        public Point getCoordinates() {
            return coordinates;
        }

        /**
         * Gets isPlayer status for button
         *
         * @return True if button belongs to player, false if belongs to system
         */
        public boolean isPlayer() {
            return isPlayer;
        }
    }
}
