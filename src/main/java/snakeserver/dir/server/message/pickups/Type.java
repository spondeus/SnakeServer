package snakeserver.dir.server.message.pickups;

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
        return values()[(int) (Math.random() * values().length)];
    }
}
