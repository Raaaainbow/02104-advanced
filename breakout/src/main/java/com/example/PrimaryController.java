package com.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private int lives = 3; 
    @FXML
    private Text livesnumber;
    @FXML
    private Text livesdisplay;

    private String username;
    private int difficulty;
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

    public void initialize() throws Exception {
        pad = new Paddle(paddle);
        livesnumber.setText(""+lives);
        startTimeline(); 
    }

    public void startTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                try {
                    onStep();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
    }
    
    public void onStep() throws Exception {
        if (create == false) { // Run once
            blocks = new BlockGrid(difficulty);
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
            App.save(username, (double) scoreAmount);
            System.exit(0);
        } 

        if (loseCondition()) {
            App.removeElement(ball.getShape());
            lives--;
            if (lives <= 0) {
                App.save("Score",(double) scoreAmount);
                toggleGameOverScreen();

                writeToDatabase(username, scoreAmount);

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


    public void setUsername(String username) {
        this.username = username;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
    
    public double getVelocity() {
        return velocity;
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

    public void writeToDatabase(String user, int score) {
        int maxRetries = 15; // Set a limit for retries
        int attempt = 0;
        boolean success = false;
    
        while (attempt < maxRetries && !success) {
            try {
                attempt++;
                String databaseUrl = "https://breakout-52014-default-rtdb.europe-west1.firebasedatabase.app/";
                String path = "/userData.json";
    
                // JSON data to send
                String jsonInput = "{\"name\":\"" + user + "\", \"score\": " + score + "}";
    
                // Create a URL object
                URL url = new URL(databaseUrl + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setDoOutput(true);
    
                // Write JSON data to the output stream
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInput.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
    
                // Get the response code to verify success
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    success = true;
                    System.out.println("Data posted successfully. Response Code: " + responseCode);
                } else {
                    System.out.println("Failed to post data. Response Code: " + responseCode);
                }
    
                connection.disconnect();
    
            } catch (Exception e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
            }
    
            if (!success && attempt < maxRetries) {
                System.out.println("Retrying...");
            }
        }
    
        if (!success) {
            System.err.println("Failed to write to database after " + maxRetries + " attempts.");
        }
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

