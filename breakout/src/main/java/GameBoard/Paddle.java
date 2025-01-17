package GameBoard;

import com.example.PrimaryController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Paddle {
    private Rectangle paddle;
    private double paddleCurrentLength = 109, paddleGoalLength = paddleCurrentLength;
    private Timeline stepTimeline, smallpad, bigpad;
    private PrimaryController controller;

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
            case 0:
                if (this.paddleGoalLength == 54) {
                    this.paddleGoalLength = 109;
                    smallpad.setCycleCount(0);
                } else {
                bigpad = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    this.paddleGoalLength = 209;
                }));
                bigpad.setCycleCount(1000/10*20); 
                bigpad.play();
                bigpad.setOnFinished(e -> {paddleGoalLength = 109;});
            }
                break;

            case 1: // backburner
                
                break;

            case 2:
                controller.Addlivtal(1);
                break;

            case 3: // backburner
                
                break;

            case 4:
                if (this.paddleGoalLength == 209) {
                    this.paddleGoalLength = 109;
                    bigpad.setCycleCount(0);
                } else {
                smallpad = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    this.paddleGoalLength = 54;
                }));
                smallpad.setCycleCount(1000/10*20); 
                smallpad.play();
                smallpad.setOnFinished(e -> {paddleGoalLength = 109;});
            }
                break;

            case 5:
                controller.Addlivtal(-1);
                break;

            case 6:
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
            
            case 8:
                break;

            default:
                break;
        }

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
