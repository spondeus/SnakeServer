package snakeserver.dir.server.message.pickups;

import java.util.Random;

public enum Type {
    FOOD(6, 0.5f),
    POISON(4, 0.25f),
    DRINK(4, 0.2f),
    WEB(3, 0.2f),
    ICE(3, 0.15f),
    GHOST(2, 0.1f);

    private final int maxAmount;
    private final float spawnRate;

    Type(int maxAmount, float spawnRate) {
        this.maxAmount = maxAmount;
        this.spawnRate = spawnRate;
    }
    public int getMaxAmount() {
        return maxAmount;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public static Type getRandomType() {
        int random = new Random().nextInt(1,101);

        if (random >= 1 && random <= 31){
            return Type.FOOD;
        } else if (random > 31 && random <= 46) {
            return Type.POISON;
        } else if (random > 46 && random <= 61) {
            return Type.DRINK;
        } else if (random > 61 && random <= 76) {
            return Type.WEB;
        } else if (random > 76 && random <= 91) {
            return Type.ICE;
        } else {
            return Type.GHOST;

        }
    }
}
