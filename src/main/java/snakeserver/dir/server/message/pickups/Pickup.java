package snakeserver.dir.server.message.pickups;

import lombok.Getter;
import lombok.Setter;
import snakeserver.dir.server.message.Message;
import snakeserver.dir.util.Vector2;

public class Pickup extends Message {
    @Getter @Setter
    private Type type;

    private final int pickUpId;

    @Getter @Setter
    private Vector2 position;

    public int getPickUpId() {
        return pickUpId;
    }

    public Pickup(Type type, int id, Vector2 position){
        this.type = type;
        this.pickUpId = id;
        this.position = position;
    }

    /*
    @Override
    public String toString(){
        return String.format(
                "%s#%s#%d#%d#%d",msg,type,id, (int) position.x(),(int) position.y()
        );
    }

     */

    @Override
    public String toString() {
        return super.toString();
    }
}
