package Game.BattleshipMVC.Controller;

import Game.BattleshipMVC.View.SplashScreens;

/**
 * Loads and launches the Battleship game
 */
public class Battleship {
    /**
     * Splash screen duration
     */
    public static final int LOADING_DURATION = 3000;


    /**
     * Entry point for the application. Displays splash screen and launches the game
     * @param args Command line arguments - not used
     */
    public static void main(String[] args){
        SplashScreens.displayLoadingSplashScreen(LOADING_DURATION);
        BattleshipController controller = new BattleshipController();
        controller.configure(null);
    }
}
