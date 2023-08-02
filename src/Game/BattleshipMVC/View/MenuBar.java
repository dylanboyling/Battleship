package Game.BattleshipMVC.View;

import Game.BattleshipMVC.Controller.BattleshipController;
import Game.Util.Constants;
import Game.Util.Utils;

import javax.swing.*;
import java.util.Locale;

import static java.lang.System.exit;

/**
 * This class creates the primary menu bar for the game which contains extra options not displayed in the main window
 */
public class MenuBar extends JMenuBar {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 7L;

    /**
     * Initializes the menu bar and it's sub-items
     *
     * @param parentFrame Frame that this menu bar belongs to
     * @param controller  Controller for the game which will process requests from the menu bar
     */
    public void initializeMenuBar(MainPanel parentFrame, BattleshipController controller) {

        JMenuItem newGameMenuItem = new JMenuItem(Utils.getLocalizedString("new_game"));
        newGameMenuItem.addActionListener(e -> {
            int userChoice = JOptionPane.showConfirmDialog(parentFrame.getContentPane(), Utils.getLocalizedString("new_game_ask"),
                    Utils.getLocalizedString("new_game_title"), JOptionPane.YES_NO_OPTION);
            if (userChoice == JOptionPane.YES_OPTION) {
                controller.newGame();
            }
        });
        JMenuItem solutionMenuItem = new JMenuItem(Utils.getLocalizedString("solution"));
        solutionMenuItem.addActionListener(e -> {
            controller.displaySolution();
        });
        JMenuItem exitMenuItem = new JMenuItem(Utils.getLocalizedString("exit"));
        exitMenuItem.addActionListener(e -> {
        });
        exitMenuItem = new JMenuItem(Utils.getLocalizedString("exit"));
        exitMenuItem.addActionListener(e -> {
            int userChoice = JOptionPane.showConfirmDialog(parentFrame.getContentPane(), Utils.getLocalizedString("exit_ask"),
                    Utils.getLocalizedString("exit_confirm"), JOptionPane.YES_NO_OPTION);
            if (userChoice == JOptionPane.YES_OPTION) {
                exit(0);
            }
        });

        JMenuItem languageMenu = new JMenu(Utils.getLocalizedString("language"));
        for (Locale locale : Constants.supportedLocales) {
            System.out.println(locale);
            final JMenuItem languageOption = new JMenuItem(String.valueOf(locale));
            languageOption.addActionListener(e -> {
                controller.changeLanguage(locale);
                System.out.println(languageOption.getLocale());
            });
            languageMenu.add(languageOption);
        }

        JMenu gameMenu = new JMenu(Utils.getLocalizedString("game"));
        gameMenu.add(newGameMenuItem);
        gameMenu.add(solutionMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(languageMenu);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        JMenuItem colorsMenuItem = new JMenuItem(Utils.getLocalizedString("colors"));
        colorsMenuItem.addActionListener(e -> {
            parentFrame.updateLogPanel(Utils.getLocalizedString("color_notice"));
        });
        JMenuItem aboutMenuItem = new JMenuItem(Utils.getLocalizedString("about"));
        aboutMenuItem.addActionListener(e -> {
            SplashScreens.displayAboutSplashScreen();
        });

        JMenu helpMenu = new JMenu(Utils.getLocalizedString("help"));
        helpMenu.add(colorsMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);

        add(gameMenu);
        add(helpMenu);
    }
}
