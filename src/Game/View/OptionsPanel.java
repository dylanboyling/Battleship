package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.DesignState;
import Game.Model.Enums.GameStatus;
import Game.Util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * Creates the options panel for the Battleship game
 */
public class OptionsPanel extends JPanel {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 6L;

    /**
     * Dimension options a user may select to change the dimensions of the game board
     */
    private final String[] DIMENSION_OPTIONS = new String[]{"2x2", "4x4", "6x6", "8x8", "10x10", "12x12", "14x14",
            "16x16", "18x18", "20x20"};

    /**
     * Controller for the game, will respond to requests to change various options
     */
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
     *
     * @param designState Current state that the design phase is in
     */
    public void initializePanel(final DesignState designState) {
        removeAll();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel configPanel = initializeConfigPanel();
        add(configPanel, gbc);

        if (controller.getGameState().getStatus() == GameStatus.DESIGN) {
            gbc.gridy = 1;
            JPanel designPanel = initializeDesignPanel(designState);
            add(designPanel, gbc);
        }

        // TODO if game is in progress, replace design window with a timer
    }

    /**
     * Creates the configuration panel which contains various game settings
     *
     * @return Configuration panel
     */
    private JPanel initializeConfigPanel() {
        System.out.println("hello");
        final GameStatus gameStatus = controller.getGameState().getStatus();
        final JPanel configPanel = new JPanel();
        configPanel.setLayout(new GridBagLayout());

        final JLabel dimensionLabel = new JLabel(Utils.getLocalizedString("dimensions"));
        final JComboBox<String> dimensions = new JComboBox<>(DIMENSION_OPTIONS);
        final JButton randomizeShips = new JButton(Utils.getLocalizedString("random"));
        final JButton designShips = new JButton(Utils.getLocalizedString("design"));
        final JButton resetButton = new JButton(Utils.getLocalizedString("reset"));
        final JButton playButton = new JButton(Utils.getLocalizedString("play"));

        if (gameStatus == GameStatus.IN_PROGRESS || gameStatus == GameStatus.GAME_OVER) {
            randomizeShips.setEnabled(false);
            designShips.setEnabled(false);
            resetButton.setEnabled(false);
            playButton.setEnabled(false);
            dimensions.setEnabled(false);
        } else {
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
                controller.playGame();
            });
        }

        final Box firstRow = Box.createHorizontalBox();
        firstRow.add(Box.createRigidArea(new Dimension(25, 0)));
        firstRow.add(randomizeShips);
        firstRow.add(Box.createRigidArea(new Dimension(10, 0)));
        firstRow.add(designShips);
        firstRow.add(Box.createRigidArea(new Dimension(10, 0)));
        firstRow.add(dimensionLabel);
        firstRow.add(dimensions);
        firstRow.add(Box.createRigidArea(new Dimension(10, 0)));
        firstRow.add(resetButton);
        firstRow.add(Box.createRigidArea(new Dimension(10, 0)));
        firstRow.add(playButton);

        configPanel.add(firstRow);

        Border optionsBorder = BorderFactory.createTitledBorder(Utils.getLocalizedString("options"));
        configPanel.setBorder(optionsBorder);

        return configPanel;
    }

    /**
     * Creates design panel which contains various options for the player to place ships onto the game board
     *
     * @param designState Current state that the design phase is in
     * @return Panel containing design menu
     */
    private JPanel initializeDesignPanel(DesignState designState) {
        final JPanel designPanel = new JPanel();
        designPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        designPanel.setAlignmentY(Component.CENTER_ALIGNMENT);


        final JLabel boatSizeLabel = new JLabel(Utils.getLocalizedString("boat_size"));
        final ArrayList<Integer> boatSizeOptions = (ArrayList<Integer>) controller.getBoatSizeOptions();
        final Integer[] sizeOptions = boatSizeOptions.toArray(new Integer[boatSizeOptions.size()]);
        final JComboBox<Integer> boatSizes = new JComboBox<>(sizeOptions);

        final JLabel boatCountRemaining = new JLabel(String.format(Utils.getLocalizedString("boats_remaining"),
                boatSizes.getSelectedItem(),
                controller.getBoatsRemaining((Integer) boatSizes.getSelectedItem())));
        boatSizes.addActionListener(e -> {
            System.out.printf("[DEBUG] Boat size to be placed was changed to %s%n", boatSizes.getSelectedItem());
            boatCountRemaining.setText(String.format(Utils.getLocalizedString("boats_remaining"), boatSizes.getSelectedItem(),
                    controller.getBoatsRemaining((Integer) boatSizes.getSelectedItem())));
            controller.setSelectedBoatSize((Integer) boatSizes.getSelectedItem());
        });
        if (designState.getBoatSize() < sizeOptions.length)
            boatSizes.setSelectedIndex((controller.getDimension() / 2) - designState.getBoatSize()); // auto selects last selected boat
        else
            boatSizes.setSelectedIndex(0);

        designPanel.add(boatSizeLabel);
        designPanel.add(boatSizes);
        designPanel.add(boatCountRemaining);

        final JLabel directionLabel = new JLabel(Utils.getLocalizedString("boat_direction"));
        final JRadioButton horizontalButton = new JRadioButton(Utils.getLocalizedString("horizontal"));
        final JRadioButton verticalButton = new JRadioButton(Utils.getLocalizedString("vertical"));
        final ButtonGroup directionButtons = new ButtonGroup();

        final boolean HORIZONTAL = true;
        final boolean VERTICAL = false;
        horizontalButton.addActionListener(e -> {
            controller.setSelectedBoatOrientation(HORIZONTAL);
        });
        verticalButton.addActionListener(e -> {
            controller.setSelectedBoatOrientation(VERTICAL);
        });
        directionButtons.add(horizontalButton);
        directionButtons.add(verticalButton);

        if(designState.getIsHorizontal())
            horizontalButton.setSelected(true);
        else
            verticalButton.setSelected(true);

        final JButton clearButton = new JButton(Utils.getLocalizedString("clear_board"));
        clearButton.addActionListener(e -> {
            System.out.println("[DEBUG] User has cleared the board in design mode");
            controller.clearPlayerBoard();
        });

        designPanel.add(directionLabel);
        designPanel.add(horizontalButton);
        designPanel.add(verticalButton);
        designPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        designPanel.add(clearButton);

        designPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        Border designBorder = BorderFactory.createTitledBorder(Utils.getLocalizedString("design"));
        designPanel.setBorder(designBorder);

        return designPanel;
    }
}
