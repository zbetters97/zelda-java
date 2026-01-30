package application;

import entity.Entity;

public record CollisionChecker(GamePanel gp) {

    /**
     * CHECK TILE
     * Checks if the given entity will collide with a tile
     * @param entity Entity to check collision for
     */
    public void checkTile(Entity entity) {

        // Collision box (left side, right side, top, bottom)
        int entityLeftWorldX = entity.worldX + entity.hitbox.x;
        int entityRightWorldX = entity.worldX + entity.hitbox.x + entity.hitbox.width;
        int entityTopWorldY = entity.worldY + entity.hitbox.y;
        int entityBottomWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Prevent collision detection out of bounds
        if (entityTopRow <= 0) {
            return;
        }
        if (entityBottomRow >= gp.maxWorldRow - 1) {
            return;
        }
        if (entityLeftCol <= 0) {
            return;
        }
        if (entityRightCol >= gp.maxWorldCol - 1) {
            return;
        }

        // Detect the two tiles player is interacting with
        int tileNum1 = 0, tileNum2 = 0;

        // Find tile player will interact with, factoring in speed
        GamePanel.Direction direction = entity.getMoveDirection();

        switch (direction) {
            case UP:
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;

                // Tiles at top-left and top-right
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                break;
            case UPLEFT:

                // Tiles at top-left and left-top
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];

                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];

                break;
            case UPRIGHT:

                // Tiles at top-right and right-top
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];

                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];

                break;
            case DOWN:
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;

                // Tiles at bottom-left and bottom-right
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                break;
            case DOWNLEFT:

                // Tiles at bottom-left and left-bottom
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];

                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];

                break;
            case DOWNRIGHT:

                // Tiles at bottom-right and right-bottom
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                break;
            case LEFT:
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;

                // Tiles at left-top and left-bottom
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];

                break;
            case RIGHT:
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;

                // Tiles at right-top and right-bottom
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                break;
            default:
                entity.collisionOn = false;
                return;
        }

        if (gp.tileM.tiles[tileNum1].hasCollision || gp.tileM.tiles[tileNum2].hasCollision) {
            entity.collisionOn = true;
        }
    }
}