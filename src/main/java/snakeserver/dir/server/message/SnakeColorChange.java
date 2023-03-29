package snakeserver.dir.server.message;

import com.badlogic.gdx.graphics.Color;

public class SnakeColorChange extends Message {

    private Color newColor;

    private Integer first;
    private Integer second;

    public SnakeColorChange( Color newColor, Integer first, Integer second) {
        this.newColor = newColor;
        this.first = first;
        this.second = second;
    }

    public Color getNewColor() {
        return newColor;
    }

    public void setNewColor(Color newColor) {
        this.newColor = newColor;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "SnakeColorChange{" +
                "newColor=" + newColor +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
