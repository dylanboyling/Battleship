package Game.BattleshipMVC.View;

import Game.BattleshipMVC.Controller.BattleshipController;
import Game.BattleshipMVC.Model.BoardState;
import Game.BattleshipMVC.Model.Enums.GameStatus;
import Game.BattleshipMVC.Model.Enums.GridSquareStatus;
import Game.BattleshipMVC.Model.GameState;
import Game.BattleshipMVC.Model.GridSquare;
import Game.Util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class creates and initializes a grid of buttons used for a player's Battleship game board
 */
public class GridPanel extends JPanel {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 4L;

    /**
     * Controller for the game which will process button clicks
     */
    private final BattleshipController controller;

    /**
     * Default constructor for creating a GridPanel
     *
     * @param controller Controller for the game which will process button clicks
     */
    public GridPanel(final BattleshipController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the panel representing a player's board
     *
     * @param gameState  Current state of the game
     * @param boardState Current state of the board being used to display the grid
     */
    public void initializePanel(final GameState gameState, final BoardState boardState) {
        final int dimension = boardState.getGridDimension();
        final boolean isPlayer = boardState.isPlayer();

        removeAll();
        setLayout(new GridLayout(0, dimension + 1, 2, 2));
        final JLabel emptyCornerLabel = new JLabel();
        emptyCornerLabel.setPreferredSize(new Dimension(5, 5));
        add(emptyCornerLabel);

        for (int col = 0; col < dimension; col++) {
            final JLabel colHeaderLabel = new JLabel();
            colHeaderLabel.setText(String.valueOf(Utils.getLetterCoordinate(col)));
            colHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            colHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(colHeaderLabel);
        }

        for (int col = 0; col < dimension; col++) {
            final JLabel rowHeaderLabel = new JLabel();
            rowHeaderLabel.setText(String.format("%d", col + 1));
            rowHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
            rowHeaderLabel.setVerticalAlignment(JLabel.CENTER);
            add(rowHeaderLabel);

            for (int row = 0; row < dimension; row++) {
                final GridSquare gridSquare = boardState.getGridSquare(row, col);
                final GridButton gridButton = new GridButton(new Point(row, col));

                gridButton.addActionListener(e -> {
                    final int x = (int) gridButton.getCoordinates().getX();
                    final int y = (int) gridButton.getCoordinates().getY();
                    controller.handleGridClick(isPlayer, x, y);
                });

                final GameStatus gameStatus = gameState.getStatus();
                if (gameStatus != GameStatus.IN_PROGRESS && !isPlayer) {
                    gridButton.setEnabled(false);
                }

                if (gridSquare != null) {
                    if (isPlayer && gridSquare.getStatus() == GridSquareStatus.ALIVE || gameStatus == GameStatus.GAME_OVER) {
                        gridButton.setText(Integer.toString(gridSquare.getShipSize()));
//                        gridButton.setFont(new Font("Arial", Font.PLAIN, 6));
                        gridButton.setMargin(new Insets(0, 0, 0, 0));
                        gridButton.setBackground(Color.gray);
                        gridButton.setOpaque(true);
                    } else if (gridSquare.getStatus() == GridSquareStatus.HIT) {
                        gridButton.setText(Integer.toString(gridSquare.getShipSize()));
                        gridButton.setBackground(Color.red);
                        gridButton.setOpaque(true);
                        gridButton.setEnabled(false); // TODO disabling buttons doesn't fill them - fix?
                    } else if (gridSquare.getStatus() == GridSquareStatus.MISSED) {
                        gridButton.setBackground(Color.blue);
                        gridButton.setOpaque(true);
                        gridButton.setEnabled(false);
                    }
                } else if (gameStatus == GameStatus.IN_PROGRESS && gameState.isPlayersTurn() && !isPlayer) {
                    gridButton.setEnabled(true);
                }

                add(gridButton);
            }
        }

        final Border gridTitle = BorderFactory.createTitledBorder(isPlayer ?
                Utils.getLocalizedString("player") : Utils.getLocalizedString("system"));
        setBorder(gridTitle);
    }

    /**
     * Grid button represents a clickable square on a player's board.
     */
    private static class GridButton extends JButton {
        /**
         * Serializable UUID
         */
        private static final long serialVersionUID = 5L;

        /**
         * Point containing (x,y) location on player's board
         */
        private final Point coordinates;

        /**
         * Constructor for the grid button
         *
         * @param coordinates (x,y) coordinates corresponding to the game board
         */
        public GridButton(final Point coordinates) {
            this.coordinates = coordinates;
        }

        /**
         * Gets coordinates for button on the game grid
         *
         * @return Integer coordinates of button on game grid
         */
        public Point getCoordinates() {
            return coordinates;
        }

    }
}
