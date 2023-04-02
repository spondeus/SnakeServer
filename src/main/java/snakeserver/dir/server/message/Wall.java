package snakeserver.dir.server.message;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static snakeserver.dir.server.message.WallPattern.createWallPatterns;

public class Wall extends Actor {
    SnapshotArray<WallPattern> patterns;
    public SnapshotArray<WallPattern> getParts() {
        return patterns;
    }
    private final ShapeRenderer sr = new ShapeRenderer();

    public Wall(SnapshotArray<WallPattern> patterns){
        this.patterns = patterns;
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        sr.setAutoShapeType(true);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (WallPattern pattern : patterns) {
            for (WallPart part : pattern.getParts()) {
                sr.setColor(part.getWallColor());
                sr.rect(part.getX(), part.getY(), part.getWidth(), part.getHeight());
            }
        }
        sr.end();
        batch.begin();
    }

    public static SnapshotArray<WallPattern> spawnWalls(){

        final int MAX_WALLS_TO_SPAWN = 4;
        final int NUM_WALL_PATTERNS = 8;

        SnapshotArray<WallPattern> wallPatterns = createWallPatterns();
        SnapshotArray<WallPattern> randomlySelectedWalls = new SnapshotArray<>();

        List<Integer> selectedIndices = new ArrayList<>();
        Random random = new Random();
        while (selectedIndices.size() < MAX_WALLS_TO_SPAWN) {
            int i = random.nextInt(NUM_WALL_PATTERNS);
            if (!selectedIndices.contains(i)){
                selectedIndices.add(i);
            }
        }
        randomlySelectedWalls.add(wallPatterns.get(selectedIndices.get(0)));
        randomlySelectedWalls.add(wallPatterns.get(selectedIndices.get(1)));
        randomlySelectedWalls.add(wallPatterns.get(selectedIndices.get(2)));
        randomlySelectedWalls.add(wallPatterns.get(selectedIndices.get(3)));

        return randomlySelectedWalls;
    }

}
