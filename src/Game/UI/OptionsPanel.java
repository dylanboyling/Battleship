package Game.UI;

import Game.Logic.GameState;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Creates the options panel for the Battleship game
 */
public class OptionsPanel extends JPanel {

    /**
     * State of the game
     */
    private final GameState gameState;

    /**
     * Language options a user may select to change the game's language
     */
    private final String[] LANGUAGE_OPTIONS             = new String[]{"English", "French"};

    /**
     * Dimension options a user may select to change the dimensions of the game board
     */
    private final String[] DIMENSION_OPTIONS            = new String[]{"10x10", "9x9", "8x8", "7x7", "6x6",
            "5x5", "4x4", "3x3", "2x2", "1x1"};

    /**
     * Creates new options panel
     * @param gameState State of the game
     */
    public OptionsPanel(final GameState gameState){
        this.gameState = gameState;
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
            gameState.updateHistoryPanel(String.format("Language was changed to %s%n", languages.getSelectedItem()));
        });

        final JLabel dimensionLabel = new JLabel("Dimensions: ");
        final JComboBox<String> dimensions = new JComboBox<String>(DIMENSION_OPTIONS);
        dimensions.addActionListener(e->{
            System.out.printf("[DEBUG] Dimensions were changed to %sx%<s%n", dimensions.getSelectedIndex()+1);
            gameState.updateHistoryPanel(String.format("Dimensions were changed to %sx%<s%n", dimensions.getSelectedIndex()+1));
        });

        final JButton randomizeShips = new JButton("Random");
        randomizeShips.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to randomize ship locations");
            gameState.updateHistoryPanel("User has pressed the button to randomize ship locations\n");
        });

        final JButton designShips = new JButton("Design");
        designShips.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to manually place ship locations");
            gameState.updateHistoryPanel("User has pressed the button to manually place ship locations\n");
        });

        final JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to reset");
            gameState.updateHistoryPanel("User has pressed the button to reset\n");
        });

        final JButton playButton = new JButton("Play");
        playButton.addActionListener(e->{
            System.out.println("[DEBUG] User has pressed the button to play the game");
            gameState.updateHistoryPanel("User has pressed the button to play the game\n");
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
