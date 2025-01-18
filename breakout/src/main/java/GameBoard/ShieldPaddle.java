package GameBoard;

import java.util.Random;

import com.example.App;
import com.example.PrimaryController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ShieldPaddle {
    private Rectangle rect;
    private double[] pos = new double[2]; 
    private Paddle pad;
    private Timeline timeline;
    private Random rand = new Random();
    private double speed = 3, velocity = rand.nextInt(2) == 0 ? speed : -speed;

    public ShieldPaddle(double x, double y, Paddle pad) {
        pos[0] = x;
        pos[1] = y;
        this.pad = pad;
        rect = new Rectangle(pad.getLength(), pad.getHeight());
        rect.setFill(Color.rgb(107, 255, 222, 0.75));
        rect.setLayoutX(x);
        rect.setLayoutY(y+pad.getHeight()*2.5);
        App.addElement(rect);
        startTimeline();
    }
    
    public Timeline getTimeline() {
        return timeline;
    }

    private void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                    if (pos[0]+rect.getWidth() >= 672-11 || pos[0] <= 11) {
                        velocity *= -1;
                    }

                    rect.setLayoutX(pos[0]);
                    pos[0]+= velocity;
                })
            );
            timeline.setCycleCount(1000/10*15); 
            timeline.play();
            timeline.setOnFinished(e -> {pad.killShield();});
    }

    public Rectangle getRect() {
        return rect;
    }

    public void kill() {
        timeline.stop();
        pad.killShield();
    }


}
