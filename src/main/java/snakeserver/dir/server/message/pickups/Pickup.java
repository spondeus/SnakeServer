package snakeserver.dir.server.message.pickups;

import lombok.Getter;
import lombok.Setter;
import snakeserver.dir.util.Vector2;

public class Pickup {
    @Getter @Setter
    private final String msg = "pickup";
    @Getter @Setter
    private Type type;

    private int id;

    @Getter @Setter
    private Vector2 position;

    public int getId(){
        return id;
    }

    public Pickup(Type type, int id, Vector2 position){
        this.type = type;
        this.id = id;
        this.position = position;
    }

    @Override
    public String toString(){
        return String.format(
                "%s#%s#%d#%d#%d",msg,type,id, (int) position.x(),(int) position.y()
        );
    }
}
