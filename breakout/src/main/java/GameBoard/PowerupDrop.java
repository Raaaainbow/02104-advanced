package GameBoard;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

import com.example.App;
import com.example.PrimaryController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class PowerupDrop {
    private double[] pos = new double[2]; 
    private Rectangle rect;
    private Random rand = new Random();
    private double hue = rand.nextInt(256);
    private double velocity = 0, speed = 4;
    private Paddle pad;

    @FXML
    private Timeline timeline;
    LinearGradient linearGradient;
    public PowerupDrop(double x, double y, Powerup powerup, Paddle pad) {
        this.pos[0] = x;
        this.pos[1] = y;
        this.pad = pad;
        rect = new Rectangle(powerup.getRect().getWidth()/2,powerup.getRect().getHeight()/2); 
        rect.setLayoutX(x);
        rect.setLayoutY(y);
        App.addElement(rect);
        startTimeline();
    }
    
    public void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                rect.setLayoutX(pos[0]);
                velocity = PrimaryController.lerp(velocity, speed, 0.005);
                pos[1]+=speed;
                rect.setLayoutY(pos[1]);
                hue = hue > 255 ? hue-255 : hue+1;
                rect.setFill(Color.hsb(hue, 0.7f, 1.0f));
                if (collidesTopPaddle()) {
                    kill();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }

    public boolean collidesTopPaddle() {
        double paddleX = pad.getX();
        double paddleY = pad.getY();
        double paddleWidth = pad.getObject().getWidth();
        double paddleHeight = pad.getObject().getHeight();
    
        boolean collides = pos[0] + rect.getWidth() > paddleX && pos[0] < paddleX + paddleWidth &&
                           pos[1] + rect.getHeight() > paddleY && pos[1] < paddleY + paddleHeight;
        if (collides) {
            pos[1] = paddleY - rect.getHeight();
        }
        return collides;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void kill() {
        pad.powerupEffect(0);
        App.removeElement(rect);
    }
    
    public double[] getPos() {
        return pos;
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

// Større paddle 
// stjerne fra mario 
// Usynlig væg
// Ekstra liv 
// Ekstra bolde 


// Power downs: 
// Paddle bliver mindre
// Mister et liv 
// Langsommere bold
}

