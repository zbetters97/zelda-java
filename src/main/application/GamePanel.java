package application;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    public enum Direction {
        UP,
        UPLEFT,
        UPRIGHT,
        DOWN,
        DOWNLEFT,
        DOWNRIGHT,
        LEFT,
        RIGHT
    }

    /* GENERAL CONFIG */
    private Graphics2D g2;
    private Thread gameThread;
    public static UtilityTool utility = new UtilityTool();

    /* CONTROLS / SOUND / UI */
    public KeyHandler keyH = new KeyHandler();

    /* SCREEN SETTINGS */
    private final int originalTileSize = 16; // 16x16 tile
    private final int scale = 3; // scale rate to accommodate for large screen
    public final int tileSize = originalTileSize * scale; // scaled tile (16*3 = 48px)
    public final int maxScreenCol = 16; // columns (width)
    public final int maxScreenRow = 12; // rows (height)
    public final int screenWidth = tileSize * maxScreenCol; // screen width (in tiles) 768px
    public final int screenHeight = tileSize * maxScreenRow;

    /* WORLD SIZE */
    public int maxWorldCol = 100;
    public int maxWorldRow = 100;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight  = tileSize * maxWorldRow;

    /* MAPS */
    public final String[] mapFiles = {"map_world.txt"};
    public final int maxMap = mapFiles.length;
    public int currentMap = 0;

    /* FULL SCREEN SETTINGS */
    public boolean fullScreenOn = false;
    private int screenWidth2 = screenWidth;
    private int screenHeight2 = screenHeight;
    private BufferedImage tempScreen;

    /* GAME STATES */
    public int gameState;
    public final int titleState = 0;
    public final int musicState = 6;

    /* AREA STATES */
    public int currentArea;
    public final int outside = 1;

    /* HANDLERS */
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);

    /* ENTITIES */
    public Player player = new Player(this);
    private final ArrayList<Entity> entityList = new ArrayList<>();

    /**
     * CONSTRUCTOR
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // screen size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // improves rendering performance

        this.addKeyListener(keyH);
        this.setFocusable(true); // GamePanel in focus to receive input
    }

    /**
     * SETUP GAME
     * Prepares the game with default settings
     */
    protected void setupGame() {

        gameState = titleState;
        currentArea = outside;
        currentMap = 0;

        // TEMP GAME WINDOW (before drawing to window)
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        tileM.loadMap();

        player.setDefaultValues();

        if (fullScreenOn) {
            setFullScreen();
        }
    }

    /**
     * SET FULL SCREEN
     * Changes the graphics to full screen mode
     */
    private void setFullScreen() {

        // GET SYSTEM SCREEN
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Driver.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Driver.window.getWidth();
        screenHeight2 = Driver.window.getHeight();
    }

    /**
     * START GAME THREAD
     * Runs a new thread
     */
    protected void startGameThread() {
        gameThread = new Thread(this); // new Thread with GamePanel class
        gameThread.start(); // calls run() method
    }

    /**
     * RUN
     * Draws and updates the game 60 times a second
     */
    @Override
    public void run() {

        long currentTime;
        long lastTime = System.nanoTime();
        double drawInterval = 1000000000.0 / 60.0; // 1/60th of a second
        double delta = 0;

        // Update and repaint gameThread
        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval; // Time passed (1/60th second)
            lastTime = currentTime;

            if (delta >= 1) {

                // Update game information
                update();

                // Draw temp screen with new information
                drawToTempScreen();

                // Send temp screen to monitors
                drawToScreen();

                delta = 0;
            }
        }
    }

    /**
     * UPDATE
     * Runs each time the frame is updated
     */
    private void update() {
        player.update();
    }

    /**
     * DRAW TO TEMP SCREEN
     * Draws to temporary screen before drawing to front-end
     */
    private void drawToTempScreen() {
        // DRAW TILES
        tileM.draw(g2);

        // DRAW ENTITIES
        entityList.add(player);

        for (Entity e : entityList) {
            e.draw(g2);
        }

        entityList.clear();
    }

    /**
     * DRAW TO SCREEN
     * Draws graphics to screen
     */
    private void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
}
