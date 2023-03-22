package snakeserver.dir.util;

//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.math.Circle;
import java.awt.Color;

public class SnakePart{

    float x,y;
    float radius;
    private Color color;

    private Snake.SnakeDirection direction;

    public SnakePart(float x, float y, float radius, Color color, Snake.SnakeDirection direction) {
        this.x=x;
        this.y=y;
        this.radius=radius;
        this.color = color;
        this.direction = direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Snake.SnakeDirection getDirection() {
        return direction;
    }

    public void setDirection(Snake.SnakeDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format("_%f:%f:%f:%s:%s",
                x,y,radius,color.toString(),direction.toString());
    }
}
