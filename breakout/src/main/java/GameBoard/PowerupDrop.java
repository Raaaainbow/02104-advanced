/// By Victor
/// Model for the power up droplet
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
    private boolean dead = false; 
    private int type = rand.nextInt(7);

    @FXML
    private Timeline timeline;
    public PowerupDrop(double x, double y, Powerup powerup, Paddle pad) {
        pos[0] = x + powerup.getRect().getWidth()/2;
        pos[1] = y;
        this.pad = pad;
        rect = new Rectangle(powerup.getRect().getWidth()/2,powerup.getRect().getHeight()/2); 
        rect.setLayoutX(pos[0]);
        rect.setLayoutY(y);
        App.addElement(rect);
        switch (type) {
            // power ups
            case 0:
            case 1:
            case 3:
            case 4:
            
                rect.setFill(Color.LIME);
                break;

                // power downs
            case 2:
            case 5:
                rect.setFill(Color.RED);
                break;
            
        }
        startTimeline();
    }
    
    public void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                rect.setLayoutX(pos[0]);
                velocity = PrimaryController.lerp(velocity, speed, 0.005);
                pos[1]+=speed;
                rect.setLayoutY(pos[1]);
                if (type == 6) {
                    hue = hue > 255 ? hue-255 : hue+1;
                    rect.setFill(Color.hsb(hue, 0.7f, 1.0f));
                } 
                if (collidesTopPaddle() || collidesTopShield()) {
                    kill();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }

    // collision with paddle to get picked up
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

    // collision with shield to get picked up
    public boolean collidesTopShield() {
        ShieldPaddle shield = pad.getShield();
        if (shield != null) {
            double shieldX = shield.getRect().getLayoutX();
            double shieldY = shield.getRect().getLayoutY();
            double shieldWidth = shield.getRect().getWidth();
            double shieldHeight = shield.getRect().getHeight();
        
            boolean collides = pos[0] + rect.getWidth() > shieldX && pos[0] < shieldX + shieldWidth &&
                            pos[1] + rect.getHeight() > shieldY && pos[1] < shieldY + shieldHeight;
            if (collides) {
                pos[1] = shieldY - rect.getHeight();
            }
            return collides;
        } else {
            return false;
        }
    }

    public Rectangle getRect() {
        return rect;
    }

    // Remove the power up droplet from the game
    public void kill() {
        if (!dead) {
            App.removeElement(rect);
            pad.powerupEffect(type); // insert type here
            dead = true;
        }
    }
    
    public double[] getPos() {
        return pos;
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

}

