package entity;

import application.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static application.GamePanel.Direction.*;

public class Entity {

    protected enum Action {
        IDLE(true, true),
        ATTACKING(false, false),
        CHARGING(true, true),
        SPINNING(false, false),
        ROLLING(false, true),
        GUARDING(true, false);

        private final boolean allowsFacing;
        private final boolean allowsTranslation;

        Action(boolean allowsFacing, boolean allowsTranslation) {
            this.allowsFacing = allowsFacing;
            this.allowsTranslation = allowsTranslation;
        }

        protected boolean allowsFacing() {
            return allowsFacing;
        }

        protected boolean allowsTranslation() {
            return allowsTranslation;
        }
    }

    protected GamePanel gp;

    /* GENERAL ATTRIBUTES */
    public int worldX, worldY;
    protected int worldXStart, worldYStart;
    protected int tempScreenX, tempScreenY;
    protected String name;

    /* MOVEMENT VALUES */
    public GamePanel.Direction direction = DOWN;
    public int speed = 1;
    protected int defaultSpeed;
    protected boolean moving = false;

    /* ANIMATION VALUES */
    private int actionLockCounter = 0;
    protected int animationSpeed;

    /* ATTACK VALUES */
    protected int swingSpeed1;
    protected int swingSpeed2;
    protected int swingSpeed3;

    /* COLLISION VALUES */
    public boolean collisionOn = false;
    protected boolean canMove = true;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48);
    public int hitboxDefaultX;
    public int hitboxDefaultY;
    protected int hitboxDefaultWidth = hitbox.width;
    protected int hitboxDefaultHeight = hitbox.height;
    protected Rectangle attackBox = new Rectangle(0, 0, 0, 0);

    /* SPRITE ATTRIBUTES */
    protected BufferedImage image, up1, up2,  down1, down2, left1, left2, right1, right2;
    protected int spriteNum = 1;
    protected int spriteCounter = 0;

    /**
     * CONSTRUCTOR
     * @param gp GamePanel
     */
    public Entity(GamePanel gp) {
        this.gp = gp;
        getImages();
    }

    /* CHILD ONLY */
    /**
     * GET IMAGE
     */
    protected void getImages() { }

    /**
     * SET ACTION
     */
    protected void setAction() { }
    /* END CHILD ONLY */

    /**
     * SETUP IMAGE
     * @param imagePath Path to image file
     * @param width Width of image
     * @param height Height of image
     * @return Scaled image
     */
    protected BufferedImage setupImage(String imagePath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(imagePath + ".png")
            ));
            return GamePanel.utility.scaleImage(image, width, height);
        }
        catch (IOException e) {
            System.out.println("Error loading image:" + e.getMessage());
            return null;
        }
    }

    /**
     * SETUP IMAGE
     * @param imagePath Path to image file
     * @return Scaled image
     */
    protected BufferedImage setupImage(String imagePath) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(imagePath + ".png")
            ));
            image = GamePanel.utility.scaleImage(image, gp.tileSize, gp.tileSize);
        }
        catch (IOException e) {
            System.out.println("Error loading image:" + e.getMessage());
        }

        return image;
    }

    /**
     * UPDATE
     * Updates the entity
     * Called every frame by GamePanel
     */
    public void update() {
        setAction();
        updateDirection();
        manageValues();
    }

    /**
     * UPDATE DIRECTION
     * Handles logic involving moving the entity
     */
    protected void updateDirection() {
        checkCollision();
        move(direction);

        if (moving) {
            cycleSprites();
        }
    }

    /**
     * CHECK COLLISION
     * Checks if the entity collides with something
     */
    protected void checkCollision() {

        collisionOn = false;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
    }

    /**
     * MOVE
     * Repositions the entity's X, Y based on direction and speed
     * Called by updateDirection() if o collision
     */
    protected void move(GamePanel.Direction direction) {

        if (!canMove || collisionOn) {
            moving = false;
            return;
        }

        moving = true;

        switch (direction) {
            case UP -> worldY -= speed;
            case UPLEFT -> {
                worldY -= (int) (speed - 0.5);
                worldX -= (int) (speed - 0.5);
            }
            case UPRIGHT -> {
                worldY -= (int) (speed - 0.5);
                worldX += (int) (speed - 0.5);
            }
            case DOWN -> worldY += speed;
            case DOWNLEFT -> {
                worldY += (int) (speed - 0.5);
                worldX -= (int) (speed - 0.5);
            }
            case DOWNRIGHT -> {
                worldY += speed;
                worldX += (int) (speed - 0.5);
            }
            case LEFT -> worldX -= speed;
            case RIGHT-> worldX += speed;
        }
    }

    /**
     * CYCLE SPRITES
     * Changes the animation counter for draw to render the correct sprite
     */
    protected void cycleSprites() {
        spriteCounter++;
        if (spriteCounter > animationSpeed && animationSpeed != 0) {

            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }

            spriteCounter = 0;
        }
    }

    /**
     * SET DIRECTION
     * Randomly re-assigns the direction the Entity is facing
     * @param rate Integer frequency of updates (60 = 1 sec)
     */
    protected void setDirection(int rate) {

        actionLockCounter++;
        if (actionLockCounter >= rate) {

            int dir = 1 + (int) (Math.random() * 4);
            if (dir == 1) {
                direction = UP;
            }
            else if (dir == 2) {
                direction = DOWN;
            }
            else if (dir == 3) {
                direction = LEFT;
            }
            else {
                direction = RIGHT;
            }

            actionLockCounter = 0;
        }
    }

    /**
     * GET MOVE DIRECTION
     * Called by CollisionDetector
     * @return Current direction of the entity
     */
    public GamePanel.Direction getMoveDirection() {
        return direction;
    }

    /**
     * MANAGE VALUES
     * Resets or reassigns entity attributes
     * called at the end of update
     */
    protected void manageValues() {

    }

    /**
     * DRAW
     * Draws the sprite data to the graphics
     * @param g2 GamePanel
     */
    public void draw(Graphics2D g2) {

        offCenter();

        // Match image to sprite direction
        if (spriteNum == 1) {
            image = switch (direction) {
                case UP, UPLEFT, UPRIGHT -> up1;
                case DOWN, DOWNLEFT, DOWNRIGHT -> down1;
                case LEFT -> left1;
                case RIGHT -> right1;
            };
        } else if (spriteNum == 2) {
            image = switch (direction) {
                case UP, UPLEFT, UPRIGHT -> up2;
                case DOWN, DOWNLEFT, DOWNRIGHT -> down2;
                case LEFT -> left2;
                case RIGHT -> right2;
            };
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Draw hitbox (debug)
        g2.drawRect(tempScreenX + hitbox.x, tempScreenY + hitbox.y, hitbox.width, hitbox.height);

        // Reset opacity
        changeAlpha(g2, 1f);
    }

    /**
     * OFF CENTER
     * Adjusts X, Y if near edge
     */
    protected void offCenter() {
        tempScreenX = getScreenX();
        tempScreenY = getScreenY();

        if (gp.player.worldX < gp.player.screenX) {
            tempScreenX = worldX;
        }
        if (gp.player.worldY < gp.player.screenY) {
            tempScreenY = worldY;
        }

        // From player to right-edge of screen
        int rightOffset = gp.screenWidth - gp.player.screenX;

        //  From player to right-edge of world
        if (rightOffset > gp.worldWidth - gp.player.worldX) {
            tempScreenX = gp.screenWidth - (gp.worldWidth - worldX);
        }

        //  From player to bottom-edge of screen
        int bottomOffSet = gp.screenHeight - gp.player.screenY;

        //  From player to bottom-edge of world
        if (bottomOffSet > gp.worldHeight - gp.player.worldY) {
            tempScreenY = gp.screenHeight - (gp.worldHeight - worldY);
        }
    }

    /**
     * GET SCREEN X
     * @return Screen X relative to player
     */
    private int getScreenX() {
        return worldX - gp.player.worldX + gp.player.screenX;
    }

    /**
     * GET SCREEN Y
     * @return Screen Y relative to player
     */
    private int getScreenY() {
        return worldY - gp.player.worldY + gp.player.screenY;
    }

    /**
     * CHANGE ALPHA
     * Changes the opacity of the image
     * @param g2 Graphics2D
     * @param alphaValue Opacity value
     */
    protected void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
}
