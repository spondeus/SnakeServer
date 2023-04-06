package snakeserver.dir.server.message.pickups;

import com.badlogic.gdx.math.Rectangle;
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

    int padding = 120;
    final int HEIGHT = 800;
    final int WIDTH = 1200;
    private Set<List<Integer>> pickupPositions = new HashSet<>();

    public ServerPickup() {
    }

    public Pickup newPickup(SnapshotArray<WallPattern> walls) {

        final int PICKUP_RADIUS = 10;

        Random random = new Random();
        outer:
        while (true) {
            int x = random.nextInt(padding, WIDTH - padding);
            int y = random.nextInt(padding, HEIGHT - padding);

            Rectangle rectangle=new Rectangle(x,y,60,60);

            final int MIN_DISTANCE = 50;

            boolean insideWall = false;
            for (WallPattern wallPatterns : walls) {
                for (WallPart wallPart : wallPatterns.getParts()) {
                    if(rectangle.overlaps(wallPart)) continue outer;
//                    float dx = Math.abs(wallPart.getX() - x);
//                    float dy = Math.abs(wallPart.getY() - y);
//                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
//                    float radius = PICKUP_RADIUS + 50;
//                    if (distance < radius) {
//                        insideWall = true;
//                        break;
//                    }
                }
                if (insideWall) {
                    break;
                }
            }
            boolean tooClose = false;
            for (List<Integer> position : pickupPositions) {
                double distance = Vector2.dst2(x, y, position.get(0), position.get(1));
                if (distance < MIN_DISTANCE * MIN_DISTANCE) {
                    tooClose = true;
                    break;
                }
            }
            if (!insideWall && !tooClose) {
                List<Integer> temp = Arrays.asList(x, y);
                pickupPositions.add(temp);
                return createRandomPickup(x, y, walls);
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
