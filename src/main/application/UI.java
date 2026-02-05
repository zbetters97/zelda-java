package application;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class UI {

    /* CONFIG */
    private final GamePanel gp;
    private Graphics2D g2;
    private Font PK_DS;

    /**
     * CONSTRUCTOR
     * Instance created by GamePanel
     * @param gp GamePanel
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        // FONT DECLARATION
        try {
            InputStream is = getClass().getResourceAsStream("/font/pokemon-ds.ttf");
            PK_DS = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is));
        }
        catch (FontFormatException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * DRAW
     * Draws the UI
     * @param g2 Graphics2D enginge
     */
    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(PK_DS);
        g2.setColor(Color.white);

        drawDebug();
    }

    /**
     * DRAW DEBUG
     * UI for debug information
     */
    private void drawDebug() {
        // Draw player hitbox
        g2.setColor(Color.RED);
        g2.drawRect(gp.player.screenX + gp.player.hitbox.x, gp.player.screenY + gp.player.hitbox.y,
                gp.player.hitbox.width, gp.player.hitbox.height);
    }
}
