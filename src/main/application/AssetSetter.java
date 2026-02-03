package application;

import entity.npc.NPC_OldMan;

public record AssetSetter(GamePanel gp) {

    public void setup() {
        setNPCs();
    }

    private void setNPCs() {

        int mapNum = 0;
        int i = 0;

        gp.npc[mapNum][i] = new NPC_OldMan(gp, 30, 26);
    }
}