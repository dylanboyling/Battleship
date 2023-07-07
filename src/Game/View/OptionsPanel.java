package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.DesignState;
import Game.Model.Enums.GameStatus;
import Game.Model.Enums.Language;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * Creates the options panel for the Battleship game
 */
public class OptionsPanel extends JPanel {
    /**
     * Dimension options a user may select to change the dimensions of the game board
     */
    private final String[] DIMENSION_OPTIONS = new String[]{"2x2", "4x4", "6x6", "8x8", "10x10", "12x12", "14x14",
            "16x16", "18x18", "20x20"};

    private final BattleshipController controller;

    /**
     * Creates new options panel
     *
     * @param controller Controller for the game, used to send requests to
     */
    public OptionsPanel(final BattleshipController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the options panel by creating the UI elements for selecting options
     */
    public void initializePanel(final DesignState designState) {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel configPanel = initializeConfigurationPanel();
        add(configPanel);

        if (controller.getGameState().getStatus() == GameStatus.DESIGN) {
            JPanel designPanel = initializeDesignPanel(designState);
            add(designPanel);
        }
    }

    /**
     * Creates the configuration panel which contains various game settings
     * @return Configuration panel
     */
    private JPanel initializeConfigurationPanel() {
        // TODO make buttons look better i.e. not so small and make centered
        final JPanel configPanel = new JPanel();
        configPanel.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        final GameStatus gameStatus = controller.getGameState().getStatus();


        final JLabel languageLabel = new JLabel("Language: ");
        final JComboBox<Language> languages = new JComboBox<>(Language.values());
        languages.addActionListener(e -> {
            System.out.printf("[DEBUG] Language was changed to %s%n", languages.getSelectedItem());
            controller.changeLanguage((Language) languages.getSelectedItem());
        });

        final JLabel dimensionLabel = new JLabel("Dimensions: ");
        final JComboBox<String> dimensions = new JComboBox<>(DIMENSION_OPTIONS);
        final JButton randomizeShips = new JButton("Random");
        final JButton designShips = new JButton("Design");
        final JButton resetButton = new JButton("Reset");
        final JButton playButton = new JButton("Play");

        if(gameStatus == GameStatus.IN_PROGRESS){
            randomizeShips.setEnabled(false);
            designShips.setEnabled(false);
            resetButton.setEnabled(false);
            playButton.setEnabled(false);
            dimensions.setEnabled(false); // TODO when game starts, this resets to index 0. fix?
        } else{
            dimensions.setSelectedIndex(controller.getDimension() / 2 - 1);
            dimensions.addActionListener(e -> {
                System.out.printf("[DEBUG] Dimensions were changed to %s%n", dimensions.getSelectedItem());
                controller.changeBoardDimension((dimensions.getSelectedIndex() + 1) * 2);
            });
            randomizeShips.addActionListener(e -> {
                System.out.println("[DEBUG] User has pressed the button to randomize ship locations");
                controller.randomizePlayerShipLocations();
            });
            designShips.addActionListener(e -> {
                System.out.println("[DEBUG] User has pressed the button to manually place ship locations");
                controller.enterDesignMode();
            });
            resetButton.addActionListener(e -> {
                System.out.println("[DEBUG] User has pressed the button to reset");
                controller.resetGameBoards();
            });
            playButton.addActionListener(e -> {
                System.out.println("[DEBUG] User has pressed the button to play the game");
                controller.beginGame();
            });
        }

        final Box firstRow = Box.createHorizontalBox();
        firstRow.add(languageLabel);
        firstRow.add(languages);
        firstRow.add(Box.createRigidArea(new Dimension(50, 0)));
        firstRow.add(randomizeShips);
        firstRow.add(Box.createRigidArea(new Dimension(50, 0)));
        firstRow.add(resetButton);

        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.insets = new Insets(0, 0, 25, 0);
        configPanel.add(firstRow, layoutConstraints);

        final Box secondRow = Box.createHorizontalBox();
        secondRow.add(dimensionLabel);
        secondRow.add(dimensions);
        secondRow.add(Box.createRigidArea(new Dimension(50, 0)));
        secondRow.add(designShips);
        secondRow.add(Box.createRigidArea(new Dimension(50, 0)));
        secondRow.add(playButton);
        layoutConstraints.gridy = 1;
        configPanel.add(secondRow, layoutConstraints);

        Border optionsBorder = BorderFactory.createTitledBorder("Options");
        configPanel.setBorder(optionsBorder);

        return configPanel;
    }

    /**
     * Creates design panel which contains various options for the player to place ships onto the game board
     * @return Panel containing design menu
     */
    private JPanel initializeDesignPanel(DesignState designState) {
        final JPanel designPanel = new JPanel();
        designPanel.setLayout(new BoxLayout(designPanel, BoxLayout.X_AXIS));

        final JLabel boatSizeLabel = new JLabel("Boat Size: ");
        ArrayList<Integer> boatSizeOptions = (ArrayList<Integer>) controller.getBoatSizeOptions();
        Integer[] sizeOptions = boatSizeOptions.toArray(new Integer[boatSizeOptions.size()]);
        final JComboBox<Integer> boatSizes = new JComboBox<>(sizeOptions);

        final JLabel boatCountRemaining = new JLabel(String.format("Boats of size %d remaining: %d", (Integer) boatSizes.getSelectedItem(),
                controller.getBoatsRemaining((Integer) boatSizes.getSelectedItem())));
        boatSizes.addActionListener(e -> {
            System.out.printf("[DEBUG] Boat size to be placed was changed to %s%n", boatSizes.getSelectedItem());
            boatCountRemaining.setText(String.format("Boats of size %d remaining: %d", (Integer) boatSizes.getSelectedItem(),
                controller.getBoatsRemaining((Integer) boatSizes.getSelectedItem())));
            controller.setSelectedBoatSize((Integer) boatSizes.getSelectedItem());
        });
        boatSizes.setSelectedIndex((controller.getDimension() / 2) - designState.getBoatSize()); // auto selects last selected boat

        designPanel.add(Box.createRigidArea(new Dimension(25, 0)));
        designPanel.add(boatSizeLabel);
        designPanel.add(boatSizes);
        designPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        designPanel.add(boatCountRemaining);
        designPanel.add(Box.createRigidArea(new Dimension(50, 0)));

        // TODO add boats remaining text

        final JLabel directionLabel = new JLabel("Boat Size: ");
        final JRadioButton horizontalButton = new JRadioButton("Horizontal");
        final JRadioButton verticalButton = new JRadioButton("Vertical");
        final ButtonGroup directionButtons = new ButtonGroup();

        final boolean HORIZONTAL = true;
        final boolean VERTICAL = false;
        horizontalButton.addActionListener(e->{
            controller.setSelectedBoatOrientation(HORIZONTAL);
        });
        verticalButton.addActionListener(e->{
            controller.setSelectedBoatOrientation(VERTICAL);
        });
        directionButtons.add(horizontalButton);
        directionButtons.add(verticalButton);
        horizontalButton.setSelected(true);

        final JButton clearButton = new JButton("Clear Board");
        clearButton.addActionListener(e -> {
            System.out.println("[DEBUG] User has cleared the board in design mode");
            controller.clearPlayerBoard();
        });

        designPanel.add(directionLabel);
        designPanel.add(horizontalButton);
        designPanel.add(verticalButton);
        designPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        designPanel.add(clearButton);

        designPanel.add(Box.createRigidArea(new Dimension(25, 0)));
        Border designBorder = BorderFactory.createTitledBorder("Design");
        designPanel.setBorder(designBorder);

        return designPanel;
    }
}
