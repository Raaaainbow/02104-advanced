/// By Victor, Sebastian & Sophia
/// Model for each Block
package GameBoard;

import java.util.Arrays;

import com.example.App;
import com.example.PrimaryController;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Block {
    private double[] pos; // Initialize position
    private double[] scale; // Initialize scale
    private Rectangle rect;// Create a visual representation of the block
    private int scoren; 


    public Block(double x, double y, double width, double height, Color color) {
        pos = new double[] {x,y};
        scale = new double[] {width,height};
        rect = new Rectangle(x, y, width, height);
        rect.setFill(color);
        App.addElement(rect);
        // Assign score based on the block's color
        if (color.equals(Color.rgb(231, 100, 154))) {
            scoren = 10;
        } else if (color.equals(Color.rgb(252, 79, 81))) {
            scoren = 8;
        } else if (color.equals(Color.rgb(248, 123, 65))) {
            scoren = 5;
        } else if (color.equals(Color.rgb(243, 211, 42))) {
            scoren = 4;
        } else if (color.equals(Color.rgb(82, 189, 85))) {
            scoren = 1;
        } else if (color.equals(Color.rgb(69, 69, 229))) {
            scoren = 0;
        } else if (color.equals(Color.rgb(140, 77, 243))) {
            scoren = 0;
        } else if (color.equals(Color.rgb(44, 240, 239))) {
            scoren = 0;
        } else {
            scoren = 0;
        }
    }

    //The Rectangle object representing the block in the GUI.
    public Rectangle getRect() {
        return rect;
    }
    
    // Delete the object from the gui
    public void kill() {
        App.removeElement(rect);
    }

    //Updates the position of the block.
    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
        rect.setLayoutX(x);
        rect.setLayoutY(y);
    }

    // The current position of the block as a double array [x, y].
    public double[] getPos() {
        return new double[] {rect.getLayoutX(), rect.getLayoutY()};
    }

    public String toString() {
        return " " + Arrays.toString(getPos());
    }

    //The score value associated with the block's color.
    public int getScoren() {
        return scoren;
    }
}
