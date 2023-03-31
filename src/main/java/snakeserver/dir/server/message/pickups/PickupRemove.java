package snakeserver.dir.server.message.pickups;

import lombok.Getter;
import snakeserver.dir.server.message.Message;

public class PickupRemove extends Message {
    @Getter
    private int pickupId;
    @Getter
    private int snakeId;

}
