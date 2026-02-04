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

    /**
     * CHECK ENTITY
     * @param entity Entity to check collision for
     * @param targets List of entities to check collision on
     */
    public void checkEntity(Entity entity, Entity[][] targets) {

        for (int i = 0; i < targets[0].length; i++) {
            if (targets[gp.currentMap][i] != null) {

                entity.hitbox.x = entity.worldX + entity.hitbox.x;
                entity.hitbox.y = entity.worldY + entity.hitbox.y;
                
                targets[gp.currentMap][i].hitbox.x = targets[gp.currentMap][i].worldX + targets[gp.currentMap][i].hitbox.x;
                targets[gp.currentMap][i].hitbox.y = targets[gp.currentMap][i].worldY + targets[gp.currentMap][i].hitbox.y;

                switch (entity.direction) {
                    case UP -> entity.hitbox.y -= entity.speed;
                    case UPLEFT -> {
                        entity.hitbox.y -= entity.speed;
                        entity.hitbox.x -= entity.speed;
                    }
                    case UPRIGHT -> {
                        entity.hitbox.y -= entity.speed;
                        entity.hitbox.x += entity.speed;
                    }
                    case DOWN -> entity.hitbox.y += entity.speed;
                    case DOWNLEFT -> {
                        entity.hitbox.y += entity.speed;
                        entity.hitbox.x -= entity.speed;
                    }
                    case DOWNRIGHT -> {
                        entity.hitbox.y += entity.speed;
                        entity.hitbox.x += entity.speed;
                    }
                    case LEFT -> entity.hitbox.x -= entity.speed;
                    case RIGHT -> entity.hitbox.x += entity.speed;
                }

                if (entity.hitbox.intersects(targets[gp.currentMap][i].hitbox)) {
                    if (targets[gp.currentMap][i] != entity) {
                        if (targets[gp.currentMap][i].collisionOn) {
                            entity.collisionOn = true;
                        }                        
                    }
                }

                // Reset entity solid area
                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;

                // Reset object solid area
                targets[gp.currentMap][i].hitbox.x = targets[gp.currentMap][i].hitboxDefaultX;
                targets[gp.currentMap][i].hitbox.y = targets[gp.currentMap][i].hitboxDefaultY;
            }
        }
    }

    /**
     * CONTACT PLAYER
     * Checks if the given entity will collide with the player entity
     * @param entity Entity to check collision for
     */
    public void checkPlayer(Entity entity) {

        entity.hitbox.x = entity.worldX + entity.hitbox.x;
        entity.hitbox.y = entity.worldY + entity.hitbox.y;

        gp.player.hitbox.x = gp.player.worldX + gp.player.hitbox.x;
        gp.player.hitbox.y = gp.player.worldY + gp.player.hitbox.y;

        switch (entity.direction) {
            case UP -> entity.hitbox.y -= entity.speed;
            case DOWN -> entity.hitbox.y += entity.speed;
            case LEFT -> entity.hitbox.x -= entity.speed;
            case RIGHT -> entity.hitbox.x += entity.speed;
            default -> entity.collisionOn = true;
        }

        if (entity.hitbox.intersects(gp.player.hitbox)) {
            entity.collisionOn = true;
        }

        // Reset entity solid area
        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;

        // Reset object solid area
        gp.player.hitbox.x = gp.player.hitboxDefaultX;
        gp.player.hitbox.y = gp.player.hitboxDefaultY;
    }
}