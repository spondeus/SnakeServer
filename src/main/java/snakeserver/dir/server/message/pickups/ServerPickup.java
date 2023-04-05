package snakeserver.dir.server.message.pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import lombok.val;
import snakeserver.dir.server.message.WallPart;
import snakeserver.dir.server.message.WallPattern;

import java.util.*;

public class ServerPickup {

    private final List<Pickup> items = new ArrayList<>();

    public List<Pickup> getItems() {
        return items;
    }
    private Set<Integer> ids = new HashSet<>();

    int padding = 60;
    final int HEIGHT = 720;
    final int WIDTH = 1200;

    private Set<List<Integer>> pickupPositions = new HashSet<>();

    public ServerPickup() {
    }

    public Pickup newPickup(SnapshotArray<WallPattern> walls) {

        Random random = new Random();

        while (true) {
            int x,y;

            x = random.nextInt(padding, WIDTH - padding);
            y = random.nextInt(padding, HEIGHT - padding);

            boolean insideWall = false;
            for (WallPattern wall : walls) {
                for (WallPart part : wall.getParts()) {
                    if (part.contains(x, y)) {
                        insideWall = true;
                        break;
                    }
                }
                if (insideWall) {
                    break;
                }
            }


            if (!insideWall) {
                List<Integer> temp = new ArrayList<>() {{
                    add(x);
                    add(y);
                }};
                if (!pickupPositions.contains(temp)) {
                    pickupPositions.add(temp);
                    return createRandomPickup(x, y, walls);
                }
            }
        }
    }

    public Pickup createRandomPickup(float x, float y, SnapshotArray<WallPattern> walls) {
        var id = -1;
        while(true){
            val rand = new Random().nextInt(1,101);
            if(!ids.contains(rand)){
                ids.add(rand);
                id = rand;
                break;
            }
        }
        Type type = Type.getRandomType();
        Vector2 position = new Vector2(x, y);
        Pickup pickup = new Pickup(type, id, position);

        var typenum = 0;
        for(var t: items)
            if(t.getType() == type)
                typenum++;
        if(typenum >= type.getMaxAmount()){
            System.out.println("Too many of these items: "+type);
            return newPickup(walls);
        }
        return pickup;
    }

    public void removePickupById(int id){
        Pickup temp = null;
        ids.remove(id);

        for (Pickup item : items) {
            if (item.getPickUpId() == id) {
                temp = item;
            }
        }
        items.remove(temp);
    }

    public void reset(){
        for (int i = 0; i < items.size(); i++) {
            items.remove(items.get(i));
        }
    }

    public void addPickup(Pickup pickup){
        items.add(pickup);
    }
}
