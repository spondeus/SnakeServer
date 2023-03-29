package snakeserver.dir.server.message;

public class SnakePointChange extends Message{

    private int change;

    public SnakePointChange(int change) {
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
        return "SnakePointChange{" +
                "change=" + change +
                '}';
    }
}

