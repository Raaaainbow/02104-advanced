package GameBoard;

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
                    if (paddle.getLayoutX()+paddle.getWidth() > 672-10) {
                        paddle.setLayoutX(661-paddle.getLayoutX()+paddle.getWidth());
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
                    System.out.println(" IT HAS SPAWNED YES");
                }
                break;

            case 2: // 1Up
                controller.Addlivtal(1);
                break;

            case 3: // Ball spam
                    
                break;

            case 4: // Small Paddle
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

            case 5: // 1Down
                controller.Addlivtal(-1);
                break;

            case 6: // Slowball
                Timeline slowball = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    controller.getBall().setSpeed(controller.getBall().getNormalSpeed()/2);
                }));
                slowball.setCycleCount(1000/10*10); 
                slowball.play();
                slowball.setOnFinished(e -> {paddleGoalLength = 109;});
                break;

            case 7: // laser pad

                break;
            
            case 8: // Speedy paddle
                Timeline speedy = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    controller.setVelocityInterpolation(1);
                    controller.sethSpeed(7.5);
                }));
                speedy.setCycleCount(1000/10*10); 
                speedy.play();
                speedy.setOnFinished(e -> {controller.setVelocityInterpolation(0.25);controller.sethSpeed(5);});
                break;

            case 9: // Slippery pad
                Timeline slippery = new Timeline(
                    new KeyFrame(Duration.millis(10), event -> {
                        controller.setVelocityInterpolation(0.025);
                    }));
                    slippery.setCycleCount(1000/10*10); 
                    slippery.play();
                    slippery.setOnFinished(e -> {controller.setVelocityInterpolation(0.25);});
                break;

            case 10: // Heat seek
                break;

            case 11: // More Power Ups
                break;

            case 12: // Shuffle Block Posititons
                break;

            case 13: // Mario Star
                break;
            
            case 14: // Explosive Ball!
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
