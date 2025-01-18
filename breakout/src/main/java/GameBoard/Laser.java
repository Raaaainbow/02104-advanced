package GameBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.example.App;
import com.example.PrimaryController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Laser {
    private double[] pos = new double[2]; 
    private Rectangle rect;
    private Paddle pad;
    private PrimaryController controller;
    @FXML
    private Timeline timeline;
    public Laser(PrimaryController controller, Paddle pad) {
        double height = 25, width = 5;
        pos[0] = pad.getX() + pad.getLength()/2;
        pos[1] = pad.getY()-pad.getHeight()/2-height;
        this.pad = pad;
        this.controller = controller;
        rect = new Rectangle(width, height); 
        rect.setLayoutX(pos[0]);
        rect.setLayoutY(pos[1]);
        rect.setFill(Color.RED);
        App.addElement(rect);
        startTimeline();
    }
    
    public void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                pos[1]-= 3.5;
                rect.setLayoutY(pos[1]);
                if (collidesBlockVertical()) {
                    kill();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }

    public boolean collidesBlockVertical() {
        HashSet<Block> blocks = controller.getBlockGrid().getBlockGrid();
        for (Block b : blocks) {
            if (b.getRect().intersects(pos[0],pos[1]-3.5,rect.getWidth(),rect.getHeight())) {
                blocks.remove(b);
                controller.getBlockGrid().removeBlock(b);
                controller.Addscore(b.getScoren());
                return true;
            }
        }
        return false;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void kill() {
        timeline.stop();
        App.removeElement(rect);
    }
    
    public double[] getPos() {
        return pos;
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

}
