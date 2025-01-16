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
    private int lives = 3; 
    @FXML
    private Text livesnumber;
    @FXML
    private Text livesdisplay;

    private BlockGrid blocks;

    private boolean create = false;
    double velocity, velocityGoal;

    public void initialize() {
        pad = new Paddle(paddle);
        livesnumber.setText(""+lives);
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

    public boolean loseCondition() { // You have 3 lives and each time one life is taken the ball is reset
        if (ball.getPos()[1] >= 972) { 
            lives--; 

            livesnumber.setText("" + lives);

            if (lives <= 0) {
                lives = 0; 
                return true; 
            }
    
            ball.resetPosition(); 
            return false;  
        }
    
        return false; 
    }
    

    // Called on key pressed
    public void inputHandling(KeyEvent event) {
        switch (event.getCode()) {
            case L:
                velocityGoal = hSpeed;
                break;

            case D:
                velocityGoal = hSpeed;
                break;

            case RIGHT:
                velocityGoal = hSpeed;
                break;
                
            case H:
                velocityGoal = -hSpeed;
                break;

            case A:
                velocityGoal =-hSpeed;
                break;
            
            case LEFT:
                velocityGoal =-hSpeed;
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
            
            case D:
                if (velocityGoal > 0) {
                    velocityGoal = 0;
                }
                break;

            case RIGHT:
                if (velocityGoal > 0) {
                    velocityGoal = 0;
                }
                break;

            case H:
            if (velocityGoal < 0) {
                velocityGoal = 0;
            }
                break;

            case A:
            if (velocityGoal < 0) {
                velocityGoal = 0;
            }
                break;

            case LEFT:
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

    public void Addlivtal(int lives) {
        this.lives += lives;
    }


    public void readScores() throws FileNotFoundException {
        File scores = new File(App.saveFilePath);
        if (!scores.exists() || !scores.canRead()) {
            throw new FileNotFoundException(App.saveFilePath + " could not be found or read");
        }

        HashMap<Integer, String> scoresNamesHash = new HashMap<>();
        ArrayList<Integer> scoresList = new ArrayList<>();

        Scanner reader = new Scanner(scores);
        while(reader.hasNextLine()) {
            String line = reader.nextLine();
            processLine(line, scoresNamesHash, scoresList);
        }
        reader.close();

        scoresList.sort(null);
    }

    private void processLine(String line, HashMap<Integer, String> scoresNamesHash, ArrayList<Integer> scoresList) {
        String[] parts = line.split(":");
        int score = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();

        scoresNamesHash.put(score, name);
        scoresList.add(score);
    }
}

