package GameBoard;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

import com.example.App;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class Powerup extends Block {
    private double[] pos = new double[2]; 
    private Rectangle rect;
    private int type; // Type of powerup 
    private Random rand = new Random();
    private double hue = rand.nextInt(256);
    private Paddle pad;

    @FXML
    private Timeline timeline;
    LinearGradient linearGradient;
    public Powerup(double x, double y, double width, double height, int type, Paddle pad) {
        super(x, y, width, height, Color.rgb(0, 0, 0, 0)); 
        this.pos[0] = x;
        this.pos[1] = y;
        this.type = type;
        this.pad = pad;
        
        rect = new Rectangle(x, y, width, height); 
        App.addElement(rect);
        startTimeline();
    }

    public void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                hue = hue > 255 ? hue-255 : hue+1;
                rect.setFill(Color.hsb(hue, 0.7f, 1.0f));
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }
    
    public Rectangle getRect() {
        return rect;
    }

    public void kill() {
        new PowerupDrop(rect.getX(), rect.getY(), this, pad);
        App.removeElement(rect);
    }

    public int getType() {
        return type;
    }
    
    public double[] getPos() {
        return pos;
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

}

