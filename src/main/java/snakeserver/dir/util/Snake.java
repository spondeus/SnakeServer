package snakeserver.dir.util;

//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Circle;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.utils.SnapshotArray;
import java.awt.Color;

import java.util.ArrayList;

//import static pentasnake.client.entities.Snake.SnakeDirection.*;
import static snakeserver.dir.util.Snake.SnakeDirection.*;

public class Snake {

    private int id;
    public ArrayList<SnakePart> getParts() {
        return parts;
    }

    ArrayList<SnakePart> parts;

    private final SnakePart head;

    private int speed = 120;

    private int points;

    private final int initialParts = 4;

//    private SnapshotArray<SnakePart> colliders = new SnapshotArray<SnakePart>();

    private static final float sqrt2 = (float) Math.pow(2, 0.5);


    public Snake(int id,int x, int y, int radius, Color bodyColor) {
        head = new SnakePart(x, y, radius, Color.ORANGE, E);
        this.parts = new ArrayList<>();
        this.parts.add(head);
        for (int i = 1; i <= initialParts; i++) {
            x -= 2 * radius;
            y = y;
            SnakePart body = new SnakePart(x, y, radius, bodyColor, E);
            this.parts.add(body);
        }
        x -= 1.5 * radius;
        y = y;
        SnakePart tail = new SnakePart(x, y, radius / 2.0f, bodyColor, E);
        this.parts.add(tail);
//        eye1 = new Circle();
//        eye2 = new Circle();
//        eye1.radius = eye2.radius = head.radius / 4;
//        innerEye1 = new Circle();
//        innerEye2 = new Circle();
//        innerEye1.radius = innerEye2.radius = eye1.radius / 2;
        points = 0;
    }

//
//    public void draw(Batch batch, float parentAlpha) {
//        batch.end();
//        sr.setAutoShapeType(true);
//        for (SnakePart part : this.parts) {
//            sr.begin(ShapeRenderer.ShapeType.Filled);
//            sr.setColor(part.getColor());
//            if (colliders.contains(part, true)) sr.setColor(Color.RED);
//            sr.circle(part.x, part.y, part.radius, 100);
//            if (part == head) {
//                switch (head.getDirection()) {
//                    case N:
//                    case S:
//                        eye1.x = head.x - eye1.radius - 2;
//                        eye1.y = eye2.y = head.y + (head.getDirection() == N ? 3 : -3);
//                        eye2.x = head.x + eye2.radius + 2;
//                        innerEye1.x = eye1.x;
//                        innerEye2.x = eye2.x;
//                        innerEye1.y = innerEye2.y = eye1.y;
//                        break;
//                    case NE:
//                    case SW:
//                        eye1.x = head.x - eye1.radius / sqrt2 -1;
//                        eye1.y = head.y + eye1.radius / sqrt2 + 1;
//                        eye2.x = head.x + eye2.radius / sqrt2 + 1;
//                        eye2.y = head.y - eye2.radius / sqrt2 -1;
//                        innerEye1.x = eye1.x;
//                        innerEye2.x = eye2.x;
//                        innerEye1.y = eye1.y;
//                        innerEye2.y = eye2.y;
//                        break;
//                    case E:
//                    case W:
//                        eye1.x = eye2.x = head.x + (head.getDirection() == E ? -3 : 3);
//                        eye1.y = head.y - eye1.radius - 2;
//                        eye2.y = head.y + eye2.radius + 2;
//                        innerEye1.x = innerEye2.x = eye1.x;
//                        innerEye1.y = eye1.y;
//                        innerEye2.y = eye2.y;
//                        break;
//                    case NW:
//                    case SE:
//                        eye1.x = head.x + eye1.radius / sqrt2 +1;
//                        eye1.y = head.y + eye1.radius / sqrt2 + 1;
//                        eye2.x = head.x - eye2.radius / sqrt2 - 1;
//                        eye2.y = head.y - eye2.radius / sqrt2 -1;
//                        innerEye1.x = eye1.x;
//                        innerEye2.x = eye2.x;
//                        innerEye1.y = eye1.y;
//                        innerEye2.y = eye2.y;
//                        break;
//
//                }
//                sr.setColor(eyeColor);
//                sr.circle(eye1.x, eye1.y, eye1.radius);
//                sr.circle(eye2.x, eye2.y, eye2.radius);
//                sr.setColor(innerEyeColor);
//                sr.circle(innerEye1.x, innerEye1.y, innerEye1.radius);
//                sr.circle(innerEye2.x, innerEye2.y, innerEye2.radius);
//            }
//            sr.end();
//        }
//        batch.begin();
//    }

//    private boolean selfCollision() {
//        for (int i = 0; i < this.parts.size; i++) {
//            for (int j = 0; j < this.parts.size; j++) {
//                if (Math.abs(i - j) < 2) continue;
//                if (this.parts.get(i).overlaps(this.parts.get(j))) {
//                    colliders.add(parts.get(i));
//                    colliders.add(parts.get(j));
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void act(float delta) {
//        if (selfCollision()) return;
        float movement = 1 / 60f * speed;
        for (int i = 0; i < this.parts.size(); i++) {
            SnakePart part = this.parts.get(i);
            SnakePart prev = (i == 0) ? null : this.parts.get(i - 1);
            if (prev != null) changeDirection(part, prev);
            float diagonal = movement / sqrt2;
            switch (part.getDirection()) {
                case N:
                    part.y += movement;
                    break;
                case NE:
                    part.x += diagonal;
                    part.y += diagonal;
                    break;
                case E:
                    part.x += movement;
                    break;
                case SE:
                    part.x += diagonal;
                    part.y -= diagonal;
                    break;
                case S:
                    part.y -= movement;
                    break;
                case SW:
                    part.x -= diagonal;
                    part.y -= diagonal;
                    break;
                case W:
                    part.x -= movement;
                    break;
                case NW:
                    part.x -= diagonal;
                    part.y += diagonal;
                    break;
            }
//            if (part.x < 0) part.x = Gdx.graphics.getWidth();
//            if (part.x > Gdx.graphics.getWidth()) part.x = 0;
//            if (part.y < 0) part.y = Gdx.graphics.getHeight();
//            if (part.y > Gdx.graphics.getHeight()) part.y = 0;

        }
    }

    public void turnRight() {
        switch (head.getDirection()) {
            case N:
                head.setDirection(NE);
                break;
            case NE:
                head.setDirection(E);
                break;
            case E:
                head.setDirection(SE);
                break;
            case SE:
                head.setDirection(S);
                break;
            case S:
                head.setDirection(SW);
                break;
            case SW:
                head.setDirection(W);
                break;
            case W:
                head.setDirection(NW);
                break;
            case NW:
                head.setDirection(N);
                break;
        }
    }

    public void turnLeft() {
        switch (head.getDirection()) {
            case N:
                head.setDirection(NW);
                break;
            case NW:
                head.setDirection(W);
                break;
            case W:
                head.setDirection(SW);
                break;
            case SW:
                head.setDirection(S);
                break;
            case S:
                head.setDirection(SE);
                break;
            case SE:
                head.setDirection(E);
                break;
            case E:
                head.setDirection(NE);
                break;
            case NE:
                head.setDirection(N);
                break;
        }
    }

    public void slowDown() {
        if (speed > 20) speed -= 20;
    }

    public void speedUp() {
        speed += 20;
    }

//    public void grow() {
//        parts.begin();
//        SnakePart beforeTail = parts.get(parts.size - 2);
//        SnakePart tail = parts.get(parts.size - 1);
//        SnakePart newBeforeTail = new SnakePart(
//                parts.get(parts.size - 2).x,
//                parts.get(parts.size - 2).y,
//                parts.get(parts.size - 2).radius,
//                parts.get(parts.size - 2).getColor(),
//                parts.get(parts.size - 2).getDirection());
//        float radius2 = 2 * beforeTail.radius;
//        float radiusSqrt = sqrt2 * beforeTail.radius;
//        switch (newBeforeTail.getDirection()) {
//            case N:
//                beforeTail.y -= radius2;
//                tail.y -= radius2;
//                break;
//            case NE:
//                beforeTail.y -= radiusSqrt;
//                beforeTail.x -= radiusSqrt;
//                tail.y -= radiusSqrt;
//                tail.x -= radiusSqrt;
//                break;
//            case E:
//                beforeTail.x -= radius2;
//                tail.x -= radius2;
//                break;
//            case SE:
//                beforeTail.y += radiusSqrt;
//                beforeTail.x -= radiusSqrt;
//                tail.y += radiusSqrt;
//                tail.x -= radiusSqrt;
//                break;
//            case S:
//                beforeTail.y += radius2;
//                tail.y += radius2;
//                break;
//            case SW:
//                beforeTail.x += radiusSqrt;
//                beforeTail.y -= radiusSqrt;
//                tail.x += radiusSqrt;
//                tail.y -= radiusSqrt;
//                break;
//            case W:
//                beforeTail.x += radius2;
//                tail.x += radius2;
//                break;
//            case NW:
//                beforeTail.x += radiusSqrt;
//                beforeTail.y += radiusSqrt;
//                tail.x += radius2;
//                tail.y += radiusSqrt;
//                break;
//        }
//        this.parts.insert(parts.size - 2, newBeforeTail);
//        parts.end();
//    }

//    public void shrink() {
//        if (parts.size < 3) return;
//        parts.begin();
//        SnakePart beforeTail = parts.get(parts.size - 2);
//        SnakePart tail = parts.get(parts.size - 1);
//        beforeTail.radius = tail.radius;
//        float radiusSqrt = sqrt2 * beforeTail.radius;
//        switch (beforeTail.getDirection()) {
//            case N:
//                beforeTail.y += beforeTail.radius;
//                break;
//            case NE:
//                beforeTail.y += beforeTail.radius;
//                beforeTail.x += beforeTail.radius;
//            case S:
//                beforeTail.y -= beforeTail.radius;
//                break;
//            case SE:
//                beforeTail.y -= beforeTail.radius;
//                beforeTail.x += beforeTail.radius;
//            case E:
//                beforeTail.x -= beforeTail.radius;
//                break;
//            case SW:
//                beforeTail.x -= beforeTail.radius;
//                beforeTail.y -= beforeTail.radius;
//            case W:
//                beforeTail.x += beforeTail.radius;
//                break;
//            case NW:
//                beforeTail.x -= beforeTail.radius;
//                beforeTail.y += beforeTail.radius;
//        }
//        parts.removeValue(tail, true);
//        parts.end();
//    }

    enum SnakeDirection {N, NE, E, SE, S, SW, W, NW}

    public SnakePart getHead() {
        return head;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    private void changeDirection(SnakePart part, SnakePart prev) {
        float delta;
        SnakeDirection prevDir = prev.getDirection();
        float deltaX = Math.abs(part.x - prev.x);
        float deltaY = Math.abs(part.y - prev.y);
        float radius2 = part.radius + prev.radius;
        float side = (radius2) / sqrt2;
        delta = (part.getDirection() == N || part.getDirection() == S) ? deltaY : deltaX;
        switch (prevDir) {
            case N:
            case S:                                     // ha elkanyarodott felfele
                if (deltaX < 1) {                       // ha egy vonalba kerülnek
                    part.x = prev.x;                    // legyenek teljesen egy vonalban
                    part.setDirection(prevDir);         // és váltson irányt a hátsó tag is
                }
                break;
            case E:
            case W:
                if (deltaY < 1) {
                    part.y = prev.y;
                    part.setDirection(prevDir);
                }
                break;
            case NE:
                if (radius2 / delta > sqrt2) {
                    part.y = prev.y - side;
                    part.x = prev.x - side;
                    part.setDirection(prevDir);
                }
                break;
            case SW:
                if (radius2 / delta > sqrt2) {
                    part.y = prev.y + side;
                    part.x = prev.x + side;
                    part.setDirection(prevDir);
                }
                break;
            case NW:
                if (radius2 / delta > sqrt2) {
                    part.y = prev.y - side;
                    part.x = prev.x + side;
                    part.setDirection(prevDir);
                }
                break;
            case SE:
                if (radius2 / delta > sqrt2) {
                    part.y = prev.y + side;
                    part.x = prev.x - side;
                    part.setDirection(prevDir);
                }
                break;
        }
    }

    @Override
    public String toString() {
        String str= String.format("%d#%d#%d#",id,speed,points);
        for (SnakePart part:parts) {
            str+= part.toString();
        }
        return str;
    }
}


