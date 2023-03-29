package snakeserver.dir.server.message;

public class SnakeCollision extends Message{
    private boolean isPickup;

    private int pickupId;

    public SnakeCollision( boolean isPickup, int pickupId) {
        this.isPickup = isPickup;
        this.pickupId = pickupId;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    public int getPickupId() {
        return pickupId;
    }

    public void setPickupId(int pickupId) {
        this.pickupId = pickupId;
    }

    @Override
    public String toString() {
        return "SnakeCollision{" +
                "isPickup=" + isPickup +
                ", pickupId=" + pickupId +
                '}';
    }
}
