package snakeserver.dir.server.message;

import com.badlogic.gdx.graphics.Color;

public class SnakeConstruct extends Message {
    private int x;
    private int y;
    private String pos;
    private int radius;
    private Color color;

    public SnakeConstruct(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "SnakeConstruct{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", color=" + color +
                '}';
    }
}

