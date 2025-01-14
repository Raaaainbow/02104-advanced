package com.example;

import java.io.IOException;
import java.util.ArrayList;

import GameBoard.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class PrimaryController {
    private double hSpeed = 5;
    @FXML
    private Rectangle paddle; 
    @FXML 
    private Text score, highscore; 
    private Paddle pad;
    private Ball ball;
    private int scoreAmount; 

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
                try {
                    onStep();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }
    
    public void onStep() throws IOException {
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
            App.save("Score", (double) scoreAmount);
            System.exit(0);
        } 

        if (loseCondition()) {
            System.out.println("YOU LOST");
            App.save("Score", (double) scoreAmount);
            System.exit(0);
        }
        score.setText(""+ scoreAmount);
    }

    public boolean winCondition() {
        if (blocks.getBlockGrid().size() == 0) {
            return true;
        }
        return false;
    }

    public boolean loseCondition() {
        return (ball.getPos()[1] >= 972)? true: false;
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

    public void Addscore(int scoreAmount) {
        this.scoreAmount += scoreAmount;
    }

    public void readScores() throws FileNotFoundException {
        File scores = new File(App.saveFilePath);
        Scanner reader = new Scanner(scores);
        HashMap<Integer, String> scoresNamesHash = new HashMap<>();
        ArrayList<Integer> scoresList = new ArrayList<>();
        int counter;

        if (scores.exists() && scores.canRead()) {
            while (reader.hasNextLine()) {
                scoresNamesHash.put(reader.nextInt(), // needs to get the name out;
            }
        } else {
            throw new FileNotFoundException(App.saveFilePath + " could not be found or read");
        }
    }
}
