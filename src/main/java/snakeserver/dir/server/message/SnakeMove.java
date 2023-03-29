package snakeserver.dir.server.message;

public class SnakeMove extends Message{
    private boolean isLeft;

    public SnakeMove( boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    @Override
    public String toString() {
        return "SnakeMove{" +
                "isLeft=" + isLeft +
                '}';
    }
}
