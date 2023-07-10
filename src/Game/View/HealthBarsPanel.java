package Game.View;

import Game.Model.BoardState;

import javax.swing.*;
import java.awt.*;

public class HealthBarsPanel extends JPanel {

    /**
     * Serializable UUID
     */
    private static final long serialVersionUID = 10L;

    /**
     * Health bar for the player, displays remaining health out of 100
     */
    private JProgressBar playerHealthBar;

    /**
     * Health bar for the system, displays remaining health out of 100
     */
    private JProgressBar systemHealthBar;

    public void initializeHealthBarsPanel(){
        removeAll();
        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setValue(100);

        systemHealthBar = new JProgressBar(0, 100);
        systemHealthBar.setStringPainted(true);
        systemHealthBar.setValue(100);

        setLayout(new GridLayout(1, 2));
        add(playerHealthBar);
        add(systemHealthBar);
    }

    /**
     * Updates the health bars of the player or the system depending on which has recently been changed
     * @param boardState State of the board being updated
     */
    public void updateHealthBars(final BoardState boardState){
        if (boardState.isPlayer()) {
            playerHealthBar.setValue((int) ((boardState.getHitPointsRemaining()
                    / ((float) boardState.getTotalHitPoints())) * 100));
            playerHealthBar.repaint();
        }
        else {
            systemHealthBar.setValue((int) ((boardState.getHitPointsRemaining()
                    / ((float) boardState.getTotalHitPoints())) * 100));
            systemHealthBar.repaint();
        }
    }
}
