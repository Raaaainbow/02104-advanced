package com.example;

import GameBoard.*;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import java.util.Random;


public class PrimaryController {
    private double hSpeed = 7;
    @FXML
    private Rectangle paddle; 
    private Paddle pad;
    private Ball ball;

    private BlockGrid blocks;

    private boolean create = false;
    double velocity, velocityGoal;

    public void initialize() {
        pad = new Paddle(paddle); 
    }
    
    public void onStep() {
        if (create == false) { // Run once
            blocks = new BlockGrid();
            ball = new Ball(pad.getX()+pad.getLength()/2-13/2, pad.getY()-30,pad,blocks);
            // Alt der skal køres en gang, skal tilføjes her
            create = true;
        }
        velocity = lerp(velocity, velocityGoal, 0.25);
        if (pad.getX()+velocity < 672-10-pad.getLength() && pad.getX()+velocity > 10) {
            pad.move(velocity);
        } else {
            velocity = 0;
        }

        ball.nextPos();
        if (ball.isMoving() == false) {
            ball.setPos( pad.getX()+pad.getLength()/2-13/2+velocity,ball.getPos()[1]);
        }
        // System.exit(0);     // for debugging
    }
        
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
        }
    }

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
        }
    }

    public double lerp(double startValue, double endValue, double interpolationAmount) {
        return (1 - interpolationAmount) * startValue + interpolationAmount * endValue;
    }

    public boolean looseCondition() {
        return (ball.getPos()[1] >= 972)? true: false;
    }
}
