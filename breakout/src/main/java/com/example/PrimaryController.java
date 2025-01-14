package com.example;

import GameBoard.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class PrimaryController {
    private double hSpeed = 5;
    @FXML
    private Rectangle paddle; 
    @FXML 
    private Text score, highscore; 
    private Paddle pad;
    private Ball ball;
    private int scoren; 
    // change amount of lives user has here
    private int liv = 3; 

    private BlockGrid blocks;

    private boolean create = false;
    double velocity, velocityGoal;

    public void initialize() {
        pad = new Paddle(paddle);
        startTimeline(); 
    }

    public void startTimeline() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                onStep();  // Calls the step event stored in our controller
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }
    
    public void onStep() {
        if (create == false) { // Run once
            blocks = new BlockGrid();
            ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            // Everything that needs to be ran once (and not run in initialize()), you can add here
            create = true;
        }
        velocity = lerp(velocity, velocityGoal, 0.25);
        if (pad.getX() + velocity < 672 - 10 - pad.getLength() && pad.getX() + velocity > 10) {
            pad.move(velocity);
        } else {
            velocity = 0;
        }

        ball.nextPos();
        if (ball.isMoving() == false) {
            ball.setPos(pad.getX() + pad.getLength()/2 - 13/2 + velocity,ball.getPos()[1]);
        }

        if (winCondition()) {
            System.out.println("YOU WON");
            System.exit(0);
        } 
        System.out.println(liv);
        if (loseCondition()) {
            App.removeElement(ball.getShape());
            liv--;
            if (liv <= 0) {
                System.out.println("YOU LOST");
                System.exit(0);
            } else {
                ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            }
        }
        score.setText(""+scoren);
    }

    public boolean winCondition() {
        if (blocks.getBlockGrid().size() == 0) {
            return true;
        }
        return false;
    }

    public boolean loseCondition() {
        return (ball.getPos()[1] <= 972)? false: true;
    }

    // Called on key pressed
    public void inputHandling(KeyEvent event) {
        switch (event.getCode()) {
            case L:
                velocityGoal = hSpeed;
                break;
                
            case H:
                velocityGoal = -hSpeed;
                break;
            
            case SPACE:
                ball.setMoving(true);
                break;

            default:
                break;
        }
    }

    // Called on key release
    public void stopHandling(KeyEvent event) {
        switch (event.getCode()) {
            case L:
                if (velocityGoal > 0) {
                    velocityGoal = 0;
                }
                break;
            
            case H:
            if (velocityGoal < 0) {
                velocityGoal = 0;
            }
                break;

            default:
                break;
        }
    }

    // Linear interpolation
    private double lerp(double startValue, double endValue, double interpolationAmount) {
        return (1 - interpolationAmount) * startValue + interpolationAmount * endValue;
    }
    public void Addscore(int scoren) {
        this.scoren += scoren;
    }

}
