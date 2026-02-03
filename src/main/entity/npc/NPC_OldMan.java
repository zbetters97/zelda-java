package entity.npc;

import application.GamePanel;
import entity.Entity;

import java.awt.*;

import static application.GamePanel.Direction.DOWN;

public class NPC_OldMan extends Entity {

    public static final String npcName = "Old Man";

    public NPC_OldMan(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX * gp.tileSize;
        this.worldY = worldY * gp.tileSize;
        worldXStart = this.worldX;
        worldYStart = this.worldY;

        name = npcName;
        direction = DOWN;
        speed = 1;
        defaultSpeed = speed;
        animationSpeed = 15;

        hitbox = new Rectangle(8, 16, 32, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        hitboxDefaultWidth = hitbox.width;
        hitboxDefaultHeight = hitbox.height;
    }

    protected void getImages() {
        up1 = setupImage("/npc/oldman_up_1");
        up2 = setupImage("/npc/oldman_up_2");
        down1 = setupImage("/npc/oldman_down_1");
        down2 = setupImage("/npc/oldman_down_2");
        left1 = setupImage("/npc/oldman_left_1");
        left2 = setupImage("/npc/oldman_left_2");
        right1 = setupImage("/npc/oldman_right_1");
        right2 = setupImage("/npc/oldman_right_2");
    }

    protected void setAction() {
        setDirection(60);
    }
}