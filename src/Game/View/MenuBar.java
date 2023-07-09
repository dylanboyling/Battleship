package Game.View;

import Game.Controller.BattleshipController;
import Game.Util.Utils;

import javax.swing.*;

import static java.lang.System.exit;

/**
 * This class creates the primary menu bar for the game which contains extra options not displayed in the main window
 */
public class MenuBar extends JMenuBar {

    /**
     * Initializes the menu bar and it's sub-items
     * @param parentFrame Frame that this menu bar belongs to
     * @param controller Controller for the game which will process requests from the menu bar
     */
    public void initializeMenuBar(JFrame parentFrame, BattleshipController controller){
        // TODO add behaviors
        JMenuItem newGameMenuItem = new JMenuItem(Utils.getLocalizedString("new_game"));
        newGameMenuItem.addActionListener(e->{
        });
        JMenuItem solutionMenuItem = new JMenuItem(Utils.getLocalizedString("solution"));
        solutionMenuItem.addActionListener(e->{
            controller.displaySolution();
        });
        JMenuItem exitMenuItem = new JMenuItem(Utils.getLocalizedString("exit"));
        exitMenuItem.addActionListener(e->{
        });
        exitMenuItem = new JMenuItem(Utils.getLocalizedString("exit"));
        exitMenuItem.addActionListener(e -> {
            int userChoice = JOptionPane.showConfirmDialog(parentFrame.getContentPane(), Utils.getLocalizedString("exit_ask"),
                    Utils.getLocalizedString("exit_confirm"), JOptionPane.YES_NO_OPTION);
            if (userChoice == JOptionPane.YES_OPTION) {
                exit(0);
            }
        });

        JMenu gameMenu = new JMenu(Utils.getLocalizedString("game"));
        gameMenu.add(newGameMenuItem);
        gameMenu.add(solutionMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        JMenuItem colorsMenuItem = new JMenuItem(Utils.getLocalizedString("colors"));
        colorsMenuItem.addActionListener(e->{
        });
        JMenuItem aboutMenuItem = new JMenuItem(Utils.getLocalizedString("about"));
        aboutMenuItem.addActionListener(e->{
            JOptionPane.showMessageDialog(parentFrame, Utils.getLocalizedString("about_msg"));
        });

        JMenu helpMenu = new JMenu(Utils.getLocalizedString("help"));
        helpMenu.add(colorsMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);

        add(gameMenu);
        add(helpMenu);
    }
}
