package snakeserver.dir.server.message;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WallPart extends Rectangle {

    private Color wallColor;
    public WallPart() {
    }
    public WallPart(float x, float y, float width, float height, Color color){
        super(x,y, width, height);
        this.wallColor = color;
    }

    public Color getWallColor(){
        return this.wallColor;
    }

    public float getRadius() {
        return 50;
    }
}
