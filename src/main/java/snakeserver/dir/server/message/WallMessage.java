package snakeserver.dir.server.message;

import com.badlogic.gdx.utils.SnapshotArray;

public class WallMessage extends Message {

    private SnapshotArray<WallPattern> wallList;

    public WallMessage(SnapshotArray<WallPattern> wallList) {
        this.wallList = wallList;
    }

    public SnapshotArray<WallPattern> getWallList() {
        return wallList;
    }

    public void setWallList(SnapshotArray<WallPattern> wallList) {
        this.wallList = wallList;
    }
}
