package Game.BattleshipMVC.View;

import Game.Util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class is displays the history log for guesses, option selections, and general info to the user
 */
public class EventPanel extends JPanel {
    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 3L;

    /**
     * Game log containing move history and various game information
     */
    private JTextArea textArea;

    /**
     * Initializes the window containing a scrollable text box for the game's history log
     */
    public void initializePanel() {
        removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        textArea = new JTextArea(0, 0);
        textArea.setEditable(false);
        final JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        final Border scrollBorderTitle = BorderFactory.createTitledBorder(Utils.getLocalizedString("game_history"));
        scroll.setBorder(scrollBorderTitle);
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1.0;
        layoutConstraints.weighty = 1.0;

        add(scroll, layoutConstraints);
    }

    /**
     * Updates the log window with a new entry
     *
     * @param event New log entry to be added to the window
     */
    public void updateLogPanel(final String event) {
        textArea.append(event);
    }
}
