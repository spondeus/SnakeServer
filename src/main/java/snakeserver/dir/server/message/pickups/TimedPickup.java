package snakeserver.dir.server.message.pickups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import snakeserver.dir.server.message.Message;

@Getter @AllArgsConstructor
public class TimedPickup extends Message {
    private boolean isGhost;

    private boolean effect;
}
