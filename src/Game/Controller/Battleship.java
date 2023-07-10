package Game.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * Displays Battleship splash screen before beginning the game
 */
public class Battleship extends JWindow {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 1L;

    /** Splash screen duration */
    private final int duration;

    /**
     * Creates a new splash screen object that is displayed a set amount of time
     * @param duration Duration
     */
    public Battleship(final int duration) {
        super();
        this.duration = duration;
    }

    /**
     * Shows a splash screen in the center of the desktop for the amount of time
     * given in the constructor.
     */
    public void showSplashScreen() {
        setSize(300, 200);
        setLocationRelativeTo(null);

        ImageIcon loadingIcon = null;
        try {
            InputStream stream = getClass().getResourceAsStream("/res/game_about.png");
            loadingIcon = new ImageIcon(ImageIO.read(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int imageWidth = loadingIcon.getIconWidth();
        final int imageHeight = loadingIcon.getIconHeight();
        setSize(imageWidth, imageHeight);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel(loadingIcon);
        JLabel textLabel = new JLabel("<html><font color='white'>Loading...</font></html>", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 24));

        contentPane.setBackground(Color.black);
        contentPane.add(loadingLabel, BorderLayout.CENTER);
        contentPane.add(textLabel, BorderLayout.SOUTH);
        setContentPane(contentPane);

        setVisible(true);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);

        dispose();
    }

    /**
     * Entry point for the application. Displays splash screen and launches the game
     * @param args Command line arguments - not used
     */
    public static void main(String[] args){
        Battleship splashScreen = new Battleship(4000);
        splashScreen.showSplashScreen();

        BattleshipController controller = new BattleshipController();
        controller.configure();
    }
}
