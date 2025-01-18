package GameBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import com.example.App;
import com.example.PrimaryController;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Paddle {
    private Rectangle paddle;
    private double paddleCurrentLength = 109, paddleGoalLength = paddleCurrentLength;
    private Timeline stepTimeline = new Timeline(), smallpad = new Timeline(), bigpad = new Timeline();
    private PrimaryController controller;
    private ShieldPaddle shield;

    public Paddle(Rectangle paddle, PrimaryController controller) {
        this.paddle = paddle;
        this.controller = controller;
        startTimeline();
    }

    public void startTimeline() {
        stepTimeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                    double lastLength = paddleCurrentLength;
                    paddleCurrentLength = PrimaryController.lerp(paddleCurrentLength, paddleGoalLength,0.05);
                    paddle.setWidth(paddleCurrentLength);
                    paddle.setLayoutX(paddle.getLayoutX()-(paddleCurrentLength-lastLength)/2);
                    if (paddle.getLayoutX()+paddle.getWidth() > 672-9) {
                        paddle.setLayoutX(paddle.getLayoutX()-1);
                    }
                    if (paddle.getLayoutX() < 11) {
                        paddle.setLayoutX(12);
                    }
                    
                })
            );
        stepTimeline.setCycleCount(Timeline.INDEFINITE); 
        stepTimeline.play();
    }

    public void powerupEffect(int type) {
        switch (type) {
            case 0: // Bigger Paddle
                bigpad = new Timeline(
                    new KeyFrame(Duration.millis(10), event -> {
                        this.paddleGoalLength = 209; 
                    })
                );
                if (smallpad.getStatus() != Animation.Status.RUNNING) {
                    bigpad.setCycleCount(1000 / 10 * 10);
                } else {
                    bigpad.setCycleCount(0);
                }
                    bigpad.play();
                    bigpad.setOnFinished(e -> this.paddleGoalLength = 109);
                break;

            case 1: // Shield
                if (shield == null) {
                    shield = new ShieldPaddle(paddle.getLayoutX(), paddle.getLayoutY(), this);
                }
                break;

            case 2: // Small Paddle
                this.paddleGoalLength = 109; 
                smallpad = new Timeline(
                    new KeyFrame(Duration.millis(10), event -> {
                        this.paddleGoalLength = 54;   
                    })
                );
                if (bigpad.getStatus() != Animation.Status.RUNNING) {
                    smallpad.setCycleCount(1000 / 10 * 10);
                } else {
                    smallpad.setCycleCount(0);
                }
                smallpad.play();
                smallpad.setOnFinished(e -> this.paddleGoalLength = 109); 
                break;

            case 3: // laser pad
                double firerate = 1000; //ms
                Timeline laser = new Timeline(
                new KeyFrame(Duration.millis(firerate), event -> {
                    new Laser(controller,this);
                }));
                laser.setCycleCount((int)(1000/firerate*5)); 
                laser.play();
                break;
            
            case 4: // Speedy paddle
                Timeline speedy = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    controller.setVelocityInterpolation(1);
                    controller.sethSpeed(7.5);
                }));
                speedy.setCycleCount(1000/10*10); 
                speedy.play();
                speedy.setOnFinished(e -> {controller.setVelocityInterpolation(0.25);controller.sethSpeed(5);});
                break;

            case 5: // Slippery pad
                Timeline slippery = new Timeline(
                    new KeyFrame(Duration.millis(10), event -> {
                        controller.setVelocityInterpolation(0.025);
                    }));
                    slippery.setCycleCount(1000/10*10); 
                    slippery.play();
                    slippery.setOnFinished(e -> {controller.setVelocityInterpolation(0.25);});
                break;

            case 6: // Mario Star
                Timeline star = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    controller.getBall().setStar(true);
                }));
                star.setCycleCount(1000/10*10); 
                star.play();
                star.setOnFinished(e -> {controller.getBall().setStar(false);});
                
                break;
            

            default:
                break;
        }

    }

    public void killShield() {
        App.removeElement(shield.getRect());
        shield = null;
    }

    public ShieldPaddle getShield() {
        return shield;
    }

    public void move(double speed) { // moving and collision with wall
        paddle.setLayoutX(paddle.getLayoutX() + speed);
    }

    public double getX() {
        return paddle.getLayoutX();
    }

    public double getY() {
        return paddle.getLayoutY();
    }

    public double getLength() {
        return paddle.getWidth();
    }

    public double getHeight() {
        return paddle.getHeight();
    }

    public Rectangle getObject() {
        return paddle;
    }

    @Override
    public String toString() {
        return "" + paddle;
    }
}
