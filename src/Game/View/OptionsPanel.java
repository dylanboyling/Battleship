package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static Game.Util.Constants.DEFAULT_GRID_DIMENSION;

/**
 * Creates the options panel for the Battleship game
 */
public class OptionsPanel extends JPanel {

    /**
     * State of the game
     */
    private final BoardState boardState;

    /**
     * Language options a user may select to change the game's language
     */
    private final String[] LANGUAGE_OPTIONS  = new String[]{"English", "French"};

    /**
     * Dimension options a user may select to change the dimensions of the game board
     */
    private final String[] DIMENSION_OPTIONS = new String[]{"2x2", "4x4", "6x6", "8x8", "10x10", "12x12", "14x14",
        "16x16", "18x18", "20x20"};

    private BattleshipController controller;

//    /**
//     * Creates new options panel
//     * @param boardState State of the game
//     */
    public OptionsPanel(BattleshipController controller, final BoardState boardState, final BoardState systemBoardState){
        this.controller = controller;
        this.boardState = boardState;
    }

    /**
     * Initializes the options panel by creating the UI elements for selecting options
     */
    public void initializePanel(){
        setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        final JLabel languageLabel = new JLabel("Language: ");
        final JComboBox<String> languages = new JComboBox<String>(LANGUAGE_OPTIONS);
        languages.addActionListener(e->{
            System.out.printf("[DEBUG] Language was changed to %s%n", languages.getSelectedItem());
            controller.changeLanguage(languages.getSelectedItem().toString());
        });

        final JLabel dimensionLabel = new JLabel("Dimensions: ");
        final JComboBox<String> dimensions = new JComboBox<String>(DIMENSION_OPTIONS);
        dimensions.setSelectedIndex(DEFAULT_GRID_DIMENSION / 2 - 1);
        dimensions.addActionListener(e->{
            System.out.printf("[DEBUG] Dimensions were changed to %s%n", dimensions.getSelectedItem());
            controller.changeBoardDimension((dimensions.getSelectedIndex()+1) * 2);
        });

        final JButton randomizeShips = new JButton("Random");
        randomizeShips.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to randomize ship locations");
            controller.randomizePlayerShipLocations();
        });

        final JButton designShips = new JButton("Design");
        designShips.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to manually place ship locations");
            // TODO make controller do this
            boardState.updateHistoryPanel("User has chosen to place their ship locations manually\n");
        });

        final JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to reset");
            controller.resetGameBoards();
        });

        final JButton playButton = new JButton("Play");
        playButton.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to play the game");
            // TODO make controller do this
            boardState.updateHistoryPanel("User has started the game\n");
        });

        Box firstRow = Box.createHorizontalBox();
        firstRow.add(languageLabel);
        firstRow.add(languages);
        firstRow.add(Box.createRigidArea(new Dimension(50, 0)));
        firstRow.add(randomizeShips);
        firstRow.add(Box.createRigidArea(new Dimension(50, 0)));
        firstRow.add(resetButton);

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.insets = new Insets(0,0,25,0);
        add(firstRow, layoutConstraints);

        Box secondRow = Box.createHorizontalBox();
        secondRow.add(dimensionLabel);
        secondRow.add(dimensions);
        secondRow.add(Box.createRigidArea(new Dimension(50, 0)));
        secondRow.add(designShips);
        secondRow.add(Box.createRigidArea(new Dimension(50, 0)));
        secondRow.add(playButton);
        layoutConstraints.gridy = 1;
        add(secondRow, layoutConstraints);

        Border optionsBorder = BorderFactory.createTitledBorder("Options");
        setBorder(optionsBorder);
    }

}
