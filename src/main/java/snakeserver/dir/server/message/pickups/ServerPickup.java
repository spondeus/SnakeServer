package snakeserver.dir.server.message.pickups;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class ServerPickup {

    private final List<Pickup> items = new ArrayList<>();

    public List<Pickup> getItems() {
        return items;
    }

    private static int pickupId = 0;
    private static int nextPickupId = pickupId++;
    int padding = 60;
    private Set<List<Integer>> pickupPositions = new HashSet<>();

    public ServerPickup() {
    }

    public Pickup newPickup() {

        while (true) {

            final int HEIGHT = 600;
            final int WIDTH = 1200;
            int x,y;

            x = new Random().nextInt(padding, WIDTH - padding);
            y = new Random().nextInt(padding, HEIGHT - padding);

            List<Integer> temp = new ArrayList<>() {{
                add(x);
                add(y);
            }};
            if (!pickupPositions.contains(temp)) {
                pickupPositions.add(temp);
                return PickupFactory.createRandomPickup(x,y);
            }
        }
    }

    private static class PickupFactory {
        public static Pickup createRandomPickup(float x, float y) {
            Type type = Type.getRandomType();
            Vector2 position = new Vector2(x, y);
            Pickup pickup = new Pickup(type, nextPickupId, position);
            return pickup;
        }
    }

    public void removePickupById(int id){
        Pickup temp = null;

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
