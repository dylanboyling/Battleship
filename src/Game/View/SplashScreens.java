package Game.View;

import Game.Controller.BattleshipController;
import Game.Model.BoardState;
import Game.Model.GameState;
import Game.Util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import static java.lang.System.exit;

/**
 * This class displays various splashscreens for different parts of the game
 */
public class SplashScreens {

    /**
     * Displays a splash screen containing information about the game
     */
    public static void displayAboutSplashScreen(){
        ImageIcon loadingIcon = null;
        try {
            InputStream stream = SplashScreens.class.getResourceAsStream("/res/game_about.png");
            loadingIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JLabel aboutIconLabel = new JLabel(loadingIcon);
        final JLabel aboutMsgLabel = new JLabel(Utils.getLocalizedString("about_msg"));

        final JPanel splashScreenAboutPanel = new JPanel();
        splashScreenAboutPanel.setLayout(new BoxLayout(splashScreenAboutPanel, BoxLayout.Y_AXIS));
        splashScreenAboutPanel.add(aboutIconLabel);
        aboutIconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        splashScreenAboutPanel.add(aboutMsgLabel);
        aboutMsgLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JOptionPane.showMessageDialog(null, splashScreenAboutPanel,
                Utils.getLocalizedString("about"), JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays a splash screen when loading the game
     * @param duration Duration the splash screen appears on the screen for
     */
    public static void displayLoadingSplashScreen(final int duration) {
        ImageIcon loadingIcon = null;
        try {
            InputStream stream = SplashScreens.class.getResourceAsStream("/res/game_about.png");
            loadingIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int imageWidth = loadingIcon.getIconWidth();
        final int imageHeight = loadingIcon.getIconHeight();
        final JWindow loadingWindow = new JWindow();
        loadingWindow.setSize(imageWidth, imageHeight);
        loadingWindow.setLocationRelativeTo(null);

        final JPanel contentPane = new JPanel(new BorderLayout());
        final JLabel loadingLabel = new JLabel(loadingIcon);
        final JLabel textLabel = new JLabel("<html><font color='white'>Loading...</font></html>", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 24));

        contentPane.setBackground(Color.black);
        contentPane.add(loadingLabel, BorderLayout.CENTER);
        contentPane.add(textLabel, BorderLayout.SOUTH);
        loadingWindow.setContentPane(contentPane);

        loadingWindow.setVisible(true);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadingWindow.setVisible(false);

        loadingWindow.dispose();
    }

    /**
     * Displays a game over splash screen with the results of the game. Prompts the user to play again
     * @param controller       Controller for the Battleship game used begin a new game
     * @param gameState        Current state of the game
     * @param playerBoardState Current state of the player's board and their ships
     * @param systemBoardState Current state of the system's board and it's ships
     */
    public static void displayGameOverSplashScreen(final BattleshipController controller, final GameState gameState,
                                                   final BoardState playerBoardState, final BoardState systemBoardState){
        ImageIcon loadingIcon = null;
        try {
            InputStream stream = gameState.didPlayerWin() ?
                    SplashScreens.class.getResourceAsStream("/res/game_winner.jpg") :
                    SplashScreens.class.getResourceAsStream("/res/game_lost.jpg");
            loadingIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JLabel gameOverIcon = new JLabel(loadingIcon);
        gameOverIcon.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        final JLabel gameOverLabel = new JLabel(
                "<html>" +
                        String.format(Utils.getLocalizedString("game_over"),
                                gameState.didPlayerWin() ? Utils.getLocalizedString("player") : Utils.getLocalizedString("system"),
                                systemBoardState.getTotalHitPoints() - systemBoardState.getHitPointsRemaining(),
                                playerBoardState.getTotalHitPoints() - playerBoardState.getHitPointsRemaining())
                        + "<br><center>" + String.format(Utils.getLocalizedString("win_loss"), gameState.getPlayerGamesWon(), gameState.getSystemGamesWon())
                        + "<br>" + Utils.getLocalizedString("game_over_restart")
                        + "</center></html>"
        );
        gameOverLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        final JPanel splashScreenGameOverPanel = new JPanel();
        splashScreenGameOverPanel.setLayout(new BoxLayout(splashScreenGameOverPanel, BoxLayout.Y_AXIS));
        splashScreenGameOverPanel.add(gameOverLabel);
        splashScreenGameOverPanel.add(gameOverIcon);

        final int userChoice = JOptionPane.showConfirmDialog(null, splashScreenGameOverPanel,
                Utils.getLocalizedString("new_game_title"), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (userChoice == JOptionPane.YES_OPTION) {
            controller.newGame();
        } else{
            exit(0);
        }
    }
}
