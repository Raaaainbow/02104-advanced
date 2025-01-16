package com.example;

import java.io.IOException;
import java.util.ArrayList;

import GameBoard.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Button;
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
    // change amount of lives user has here
    private int lives = 3; 

    private BlockGrid blocks;

    @FXML
    private Text gamePause;
    @FXML
    private Text pressEscape;
    @FXML
    private Rectangle gamePauseBackground;
    @FXML
    private Timeline timeline;
    @FXML
    private Text gameOver;
    @FXML 
    private Text returnTo;
    @FXML 
    private Text mainMenu;
    @FXML 
    private Button backButton;
    @FXML
    private Text winLoseScore; 

    private boolean create = false;
    double velocity, velocityGoal;

    public void initialize() {
        pad = new Paddle(paddle);
        startTimeline(); 
    }

    public void startTimeline() {
        timeline = new Timeline(
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

        // remove winCondition and replace you died with GAME OVER
        if (winCondition()) {
            System.out.println("YOU WON");
            App.save("Score", (double) scoreAmount);
            System.exit(0);
        } 

        if (loseCondition()) {
            App.removeElement(ball.getShape());
            lives--;
            if (lives <= 0) {
                App.save("Score",(double) scoreAmount);
                toggleGameOverScreen();
                /*
                System.out.println("YOU LOST");
                App.save("Score", (double) scoreAmount);
                System.exit(0);
                */
            } else {
                ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            }
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
        return (ball.getPos()[1] <= 972)? false: true;
    }

    // Called on key pressed
    public void inputHandling(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE:
                if (timeline.getStatus() == Timeline.Status.RUNNING) {
                    timeline.pause();  
                    togglePauseScreen();
                    break;
                } else {
                    timeline.play();
                    togglePauseScreen();
                    break;
                }

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

    private void togglePauseScreen () {
        if (gamePauseBackground.isVisible() && pressEscape.isVisible() && gamePause.isVisible()) {
            gamePauseBackground.setVisible(false);
            gamePauseBackground.setManaged(false);
            pressEscape.setVisible(false);
            pressEscape.setManaged(false);
            gamePause.setVisible(false);
            gamePause.setManaged(false); 
        } else {
            gamePauseBackground.setVisible(true);
            gamePauseBackground.setManaged(true);
            pressEscape.setVisible(true);
            pressEscape.setManaged(true);
            gamePause.setVisible(true);
            gamePause.setManaged(true); 

            gamePauseBackground.toFront();
            pressEscape.toFront();
            gamePause.toFront();
        }
    }

    private void toggleGameOverScreen() {
        if (gameOver.isVisible() && returnTo.isVisible() && mainMenu.isVisible() && gamePauseBackground.isVisible() && winLoseScore.isVisible() && backButton.isVisible()) {
            gamePauseBackground.setVisible(false);
            gamePauseBackground.setManaged(false);
            gameOver.setVisible(false);
            gameOver.setManaged(false);
            returnTo.setVisible(false);
            returnTo.setManaged(false);
            mainMenu.setVisible(false);
            mainMenu.setManaged(false);
            winLoseScore.setVisible(false);
            winLoseScore.setManaged(false);
            backButton.setDisable(false);
    } else {
            gamePauseBackground.setVisible(true);
            gamePauseBackground.setManaged(true);
            gameOver.setVisible(true);
            gameOver.setManaged(true);
            returnTo.setVisible(true);
            returnTo.setManaged(true);
            mainMenu.setVisible(true);
            mainMenu.setManaged(true);
            winLoseScore.setVisible(true);
            winLoseScore.setManaged(true);
            backButton.setDisable(true);
        }
    }
}

