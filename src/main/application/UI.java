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

        drawHUD();
    }

    /**
     * DRAW HUD
     * Draws the HUD during playstate
     */
    private void drawHUD() {
        drawChargeBar();
        drawDebug();
    }

    /**
     * DRAW CHARGE BAR
     * Draws the spin attack charge bar
     */
    private void drawChargeBar() {

        // If player is charging spin attack
        if (gp.player.charge > 0) {

            // Position above player's head
            int x = gp.player.getScreenX() - 7;
            int y = gp.player.getScreenY() - 20;
            int width = 62;
            int height = 10;

            // Draw black bar
            Color barColor = Color.BLACK;
            g2.setColor(barColor);
            g2.fillRect(x, y, width, height);

            // White outline if not ready, green fill if ready
            int charge = gp.player.charge;
            barColor = charge < 120 ? Color.WHITE : new Color(0, 240, 0);

            g2.setColor(barColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, width, height);

            barColor = getChargeColor(charge);
            g2.setColor(barColor);

            // Bar fill, slowly increase width
            x++;
            y++;
            height -= 2;
            width = charge / 2;
            g2.fillRect(x, y, width, height);
        }
    }

    /**
     * GET CHARGE COLOR
     * Gets the color of the spin attack charge bar based on charge
     * @param charge Current player charge value
     * @return The new color of the charge bar
     */
    private Color getChargeColor(int charge) {
        if (charge < 40) return new Color(0, 105, 0);
        if (charge < 80) return new Color(0, 155, 0);
        if (charge < 120) return new Color(0, 205, 0);

        return new Color(0, 240, 0);
    }

    /**
     * DRAW DEBUG
     * UI for debug information
     */
    private void drawDebug() {

        int x = 10;
        int y = gp.tileSize * 6;
        int lineHeight = 20;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));

        // Draw coordinates
        g2.drawString("WorldX: " + gp.player.worldX, x, y);
        y += lineHeight;
        g2.drawString("WorldY: " + gp.player.worldY, x, y);
        y += lineHeight;
        g2.drawString("Column: " + (gp.player.worldX + gp.player.hitbox.x) / gp.tileSize, x, y);
        y += lineHeight;
        g2.drawString("Row: " + (gp.player.worldY + gp.player.hitbox.y) / gp.tileSize, x, y);

        // Draw player hitbox
        g2.setColor(Color.RED);
        g2.drawRect(gp.player.screenX + gp.player.hitbox.x, gp.player.screenY + gp.player.hitbox.y,
                gp.player.hitbox.width, gp.player.hitbox.height);
    }
}
