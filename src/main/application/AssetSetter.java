package application;

import entity.enemy.EMY_Keese;
import entity.npc.NPC_OldMan;

public record AssetSetter(GamePanel gp) {

    public void setup() {
        setNPCs();
        setEnemies();
    }

    private void setNPCs() {

        int mapNum = 0;
        int i = 0;

        gp.npc[mapNum][i] = new NPC_OldMan(gp, 30, 26);
    }

    private void setEnemies() {

        int mapNum = 0;
        int i = 0;

        gp.enemy[mapNum][i] = new EMY_Keese(gp, 26, 21);
    }
}