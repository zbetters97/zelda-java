package application;

import javax.swing.*;
import java.util.Objects;

public class Driver {

    protected static JFrame window;

    /**
     * MAIN
     * The main method of the application
     */
    static void main() {

        window = new JFrame();

        // WINDOW PROPERTIES
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Legend of Zelda");
        new Driver().setGameIcon();

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // LOAD SETTINGS
        if (gamePanel.fullScreenOn) {
            window.setUndecorated(true);
        }

        // RESIZE WINDOW
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // START
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }

    /**
     * SET ICON
     * Sets the game icon to triforce.png
     */
    private void setGameIcon() {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(
                getClass().getClassLoader().getResource("misc/icon_triforce.png")));
        window.setIconImage(icon.getImage());
    }
}