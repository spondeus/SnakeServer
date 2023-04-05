package snakeserver.dir.server.message;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.SnapshotArray;

public class WallPattern {

    public SnapshotArray<WallPart> getParts() {
        return parts;
    }
    private final SnapshotArray<WallPart> parts;

    public WallPattern(SnapshotArray<WallPart> parts) {
        this.parts = parts;
    }
    public static SnapshotArray<WallPattern> createWallPatterns() {

        SnapshotArray<WallPattern> wallPatterns = new SnapshotArray<>();

        SnapshotArray<WallPart> bottomLeftCorner = new SnapshotArray<>();
        WallPart bottomLeft1 = new WallPart(100, 100, 200, 50, Color.FIREBRICK);
        WallPart bottomLeft2 = new WallPart(100, 150, 50, 150, Color.FIREBRICK);
        bottomLeftCorner.add(bottomLeft1);
        bottomLeftCorner.add(bottomLeft2);
        wallPatterns.add(new WallPattern(bottomLeftCorner));

        SnapshotArray<WallPart> bottomRightCorner = new SnapshotArray<>();
        WallPart bottomRight1 = new WallPart(900, 100, 200, 50, Color.FIREBRICK);
        WallPart bottomRight2 = new WallPart(1050, 150, 50, 150, Color.FIREBRICK);
        bottomRightCorner.add(bottomRight1);
        bottomRightCorner.add(bottomRight2);
        wallPatterns.add(new WallPattern(bottomRightCorner));

        SnapshotArray<WallPart> upperRightCorner = new SnapshotArray<>();
        WallPart upperRight1 = new WallPart(900, 650, 200, 50, Color.FIREBRICK);
        WallPart upperRight2 = new WallPart(1050, 500, 50, 150, Color.FIREBRICK);
        upperRightCorner.add(upperRight1);
        upperRightCorner.add(upperRight2);
        wallPatterns.add(new WallPattern(upperRightCorner));

        SnapshotArray<WallPart> upperLeftCorner = new SnapshotArray<>();
        WallPart upperLeft1 = new WallPart(100, 650, 200, 50, Color.FIREBRICK);
        WallPart upperLeft2 = new WallPart(100, 500, 50, 150, Color.FIREBRICK);
        upperLeftCorner.add(upperLeft1);
        upperLeftCorner.add(upperLeft2);
        wallPatterns.add(new WallPattern(upperLeftCorner));

        SnapshotArray<WallPart> straightLong = new SnapshotArray<>();
        WallPart straight = new WallPart(450, 500, 350, 50, Color.FIREBRICK);
        straightLong.add(straight);
        wallPatterns.add(new WallPattern(straightLong));

        SnapshotArray<WallPart> uShaped = new SnapshotArray<>();
        WallPart uLeft = new WallPart(500, 700, 50, 30, Color.FIREBRICK);
        WallPart uMiddle = new WallPart(500, 650, 250, 50, Color.FIREBRICK);
        WallPart uRight = new WallPart(700, 700, 50, 30, Color.FIREBRICK);
        uShaped.add(uLeft);
        uShaped.add(uMiddle);
        uShaped.add(uRight);
        wallPatterns.add(new WallPattern(uShaped));

        SnapshotArray<WallPart> diagonalPieces = new SnapshotArray<>();
        WallPart diagonal1 = new WallPart(220, 450, 50, 50, Color.FIREBRICK);
        WallPart diagonal2 = new WallPart(270, 400, 50, 50, Color.FIREBRICK);
        WallPart diagonal3 = new WallPart(320, 350, 50, 50, Color.FIREBRICK);
        WallPart diagonal4 = new WallPart(370, 300, 50, 50, Color.FIREBRICK);
        diagonalPieces.add(diagonal1);
        diagonalPieces.add(diagonal2);
        diagonalPieces.add(diagonal3);
        diagonalPieces.add(diagonal4);
        wallPatterns.add(new WallPattern(diagonalPieces));

        SnapshotArray<WallPart> landingPad = new SnapshotArray<>();
        WallPart landingPad1 = new WallPart(600, 300, 150, 50, Color.FIREBRICK);
        WallPart landingPad2 = new WallPart(650, 150, 50, 200, Color.FIREBRICK);
        WallPart landingPad3 = new WallPart(600, 100, 150, 50, Color.FIREBRICK);
        landingPad.add(landingPad1);
        landingPad.add(landingPad2);
        landingPad.add(landingPad3);
        wallPatterns.add(new WallPattern(landingPad));

        return wallPatterns;
    }
}
