package snakeserver.dir.server.message;

public class SnakeSpeedChange extends Message {
    private int change;

    public SnakeSpeedChange(int change) {
        this.change = change;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "SnakeSpeedChange{" +
                "change=" + change +
                '}';
    }
}
