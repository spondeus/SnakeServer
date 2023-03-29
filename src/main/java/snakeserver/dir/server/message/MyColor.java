package snakeserver.dir.server.message;

import java.awt.*;

public class MyColor extends Color {
//    public int getR(){return getRed();}
//    public int getG(){return getGreen();}
//    public int getB(){return getBlue();}
//    public int getA(){return getAlpha();}

    public int getValue(){return getRGB();}

    public void setValue(int value){
        ;
    }

    public MyColor(int r, int g, int b) {
        super(r, g, b);
    }
//
//    public MyColor(int r, int g, int b, int a) {
//        super(r, g, b, a);
//    }
}
