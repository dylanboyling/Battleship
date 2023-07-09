package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;
import Game.Model.DesignState;
import Game.Model.Enums.GameStatus;
import Game.Model.GameState;
import Game.Util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static java.lang.System.exit;

/**
 * This class creates and initializes the main panel which includes the game boards, event log, and options menu
 */
@SuppressWarnings("deprecation")
public class MainPanel extends JFrame implements Observer {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 2L;

    /**
     * Width of the main window
     */
    public static final int MAIN_WINDOW_WIDTH = 1700;

    /**
     * Height of the main window
     */
    public static final int MAIN_WINDOW_HEIGHT = 1100;

    /**
     * Main frame which contains all UI elements
     */
    private JFrame main;

    /**
     * Displays move history and various information related to the game in a text box
     */
    private EventPanel eventPanel;

    /**
     * Grid of buttons for the player's game board
     */
    private GridPanel playerGrid;

    /**
     * Contains selection boxes and buttons to change the game's options
     */
    private OptionsPanel optionsPanel;

    /**
     * Grid of buttons for the system's game board
     */
    private GridPanel systemGrid;

    /**
     * Controller for the game which will process requests from the panel
     */
    private final BattleshipController controller;

    /**
     * Creates a new main panel and links it to the controller for the game
     *
     * @param controller Controller for the Battleship game used to respond to requests from the main panel
     */
    public MainPanel(BattleshipController controller) {
        super();
        this.controller = controller;
    }

    /**
     * Initializes the main window and initializes different sections of the UI
     *
     * @param gameState        Current state of the game
     * @param designState      Current state of the design phase
     * @param playerBoardState Current state of the player's board and their ships
     * @param systemBoardState Current state of the system's board and it's ships
     */
    public void initializePanel(GameState gameState, DesignState designState, BoardState playerBoardState,
                                BoardState systemBoardState) {
        setLookAndFeel();

        int w = (int) (MAIN_WINDOW_WIDTH / 2F);
        int h = (int) (MAIN_WINDOW_HEIGHT * 0.8);

        main = new JFrame(Utils.getLocalizedString("title_bar"));
        main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        initializeMenuBar();

        eventPanel = new EventPanel();
        eventPanel.initializePanel();
        eventPanel.setPreferredSize(new Dimension(w, (int) (MAIN_WINDOW_HEIGHT * 0.2F)));

        gameState.addObserver(this);

        playerGrid = new GridPanel(controller);
        playerGrid.setPreferredSize(new Dimension(w, h));
        playerGrid.initializePanel(gameState, playerBoardState);
        playerBoardState.addObserver(this);

        systemGrid = new GridPanel(controller);
        systemGrid.setPreferredSize(new Dimension(w, h));
        systemGrid.initializePanel(gameState, systemBoardState);
        systemBoardState.addObserver(this);

        optionsPanel = new OptionsPanel(controller);
        optionsPanel.initializePanel(designState);
        optionsPanel.setPreferredSize(new Dimension(w, (int) (MAIN_WINDOW_HEIGHT * 0.2F)));

        // First row (Player and System grids)
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        main.add(playerGrid, layoutConstraints);
        layoutConstraints.gridx = 1;
        main.add(systemGrid, layoutConstraints);

        // Second row (History panel and options panel)
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;
        layoutConstraints.fill = GridBagConstraints.BOTH;
        main.add(eventPanel, layoutConstraints);
        layoutConstraints.gridx = 1;
        main.add(optionsPanel, layoutConstraints);

        main.pack();
        main.setLocationRelativeTo(null);
        main.setResizable(false);
        main.setVisible(true);
    }

    /**
     * Updates the main Battleship board, primarily used when the state of the game has changed and may need to be repainted.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BoardState) {
            System.out.println("[DEBUG] GameState has changed, refreshing grids");
            BoardState boardState = (BoardState) o;
            GameState gameState = controller.getGameState();
            if (boardState.isPlayer()) {
                playerGrid.initializePanel(gameState, boardState);
                optionsPanel.initializePanel(controller.getDesignState());
            } else
                systemGrid.initializePanel(gameState, boardState);
        } else if (o instanceof GameState) {
            final GameState gameState = (GameState) o;
            final BoardState playerBoardState = controller.getBoardState(true);
            final BoardState systemBoardState = controller.getBoardState(false);

            playerGrid.initializePanel(gameState, playerBoardState);
            systemGrid.initializePanel(gameState, systemBoardState);

            if(gameState.hasLocaleChanged()){
                eventPanel.initializePanel();
                optionsPanel.initializePanel(controller.getDesignState());
                initializeMenuBar();
                gameState.setHasLocaleChanged(false);
            }

            if ((gameState.getStatus() == GameStatus.GAME_OVER)) {
                int userChoice = JOptionPane.showConfirmDialog(getContentPane(),
                        String.format(Utils.getLocalizedString("game_over"),
                                gameState.didPlayerWin() ? Utils.getLocalizedString("player") : Utils.getLocalizedString("system"),
                                systemBoardState.getTotalHitPoints() - systemBoardState.getHitPointsRemaining(),
                                playerBoardState.getTotalHitPoints() - playerBoardState.getHitPointsRemaining())
                                + "\n" + String.format(Utils.getLocalizedString("win_loss"), gameState.getPlayerGamesWon(), gameState.getSystemGamesWon())
                                + "\n" + Utils.getLocalizedString("game_over_restart"),
                        Utils.getLocalizedString("new_game_title"), JOptionPane.YES_NO_OPTION);
                if (userChoice == JOptionPane.YES_OPTION) {
                    controller.newGame();
                } else{
                    exit(0);
                }
            }
        }

        main.repaint();
        main.revalidate();
    }

    /**
     * Updates the event log window by outputting a message to it
     *
     * @param event Game event that is to be displayed to the user
     */
    public void updateLogPanel(String event) {
        eventPanel.updateLogPanel(event + "\n");
        eventPanel.repaint();
        eventPanel.revalidate();
    }

    /**
     * Initializes menu bar
     */
    private void initializeMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.initializeMenuBar(this, controller);
        main.setJMenuBar(menuBar);
    }

    /**
     * Sets the look and feel of the game to match the operating system and it's settings that it's running on
     */
    private void setLookAndFeel() {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
