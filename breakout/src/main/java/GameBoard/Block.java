package GameBoard;

import java.util.Arrays;

import com.example.App;
import com.example.PrimaryController;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Block {
    private double[] pos;
    private double[] scale; 
    private Rectangle rect;
    private int score; 


    public Block(double x, double y, double width, double height, Color color) {
        pos = new double[] {x,y};
        scale = new double[] {width,height};
        rect = new Rectangle(x, y, width, height);
        rect.setFill(color);
        App.addElement(rect);
        if (color.equals(Color.rgb(231, 100, 154))) {
            score = 500;
        } else if (color.equals(Color.rgb(252, 79, 81))) {
            score = 300;
        } else if (color.equals(Color.rgb(248, 123, 65))) {
            score = 200;
        } else if (color.equals(Color.rgb(243, 211, 42))) {
            score = 100;
        } else if (color.equals(Color.rgb(82, 189, 85))) {
            score = 50;
        } else if (color.equals(Color.rgb(69, 69, 229))) {
            score = 0;
        } else if (color.equals(Color.rgb(140, 77, 243))) {
            score = 0;
        } else if (color.equals(Color.rgb(44, 240, 239))) {
            score = 0;
        } else {
            score = 0;
        }
        
    }

    public Rectangle getRect() {
        return rect;
    }
    
    // Delete the object from the gui
    public void kill() {
        App.removeElement(rect);
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

    public double[] getPos() {
        return pos;
    }

    public String toString() {
        return " " + Arrays.toString(getPos());
    }

    public int getScore() {
        return score;
    }
}
