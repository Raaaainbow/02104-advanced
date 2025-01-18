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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PrimaryController {
    private double hSpeed = 5, velocity, velocityGoal, velocityInterpolation =.25;
    private int scoreAmount, lives = 3, difficulty;
    private boolean create = false;
    private Paddle pad;
    private Ball ball;
    @FXML
    private Text combocounter;
    private String username;
    private BlockGrid blocks;
    @FXML
    private Text gamePause, pressEscape, gameOver, returnTo, mainMenu, winLoseScore, score, highscore, livesnumber, livesdisplay;
    @FXML
    private Timeline timeline;
    @FXML
    private Rectangle paddle, gamePauseBackground; 
    @FXML
    private Rectangle backgroundpress;
    @FXML 
    private Text pressspacetext; 


    public void initialize() throws Exception {
        pad = new Paddle(paddle,this);
        livesnumber.setText(lives + " lives");
        startTimeline(); 

        backgroundpress.setVisible(true);
        pressspacetext.setVisible(true);

        App.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::inputHandling);
        App.getScene().addEventFilter(KeyEvent.KEY_RELEASED, this::stopHandling);
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
            blocks = new BlockGrid(difficulty,pad);
            ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            // Load local scores and names
            String[] localNames = App.loadName();
            int[] localScores = App.loadScore();

            // Validate local names and scores
            List<String> validNames = new ArrayList<>();
            List<Integer> validScores = new ArrayList<>();
            for (int i = 0; i < localNames.length; i++) {
                if (!localNames[i].isEmpty() && i < localScores.length) {
                    validNames.add(localNames[i]);
                    validScores.add(localScores[i]);
                }
            }
            localNames = validNames.toArray(new String[0]);
            localScores = validScores.stream().mapToInt(Integer::intValue).toArray();

            // Sort local scores (descending)
            LeaderController.sortLeaderboard(localNames, localScores);

            // Match local scores with username
            boolean matchFound = false;
            for (int i = 0; i < localNames.length; i++) {
                if (username.equalsIgnoreCase(localNames[i]) && !matchFound) {
                    highscore.setText((""+localScores[i]).toUpperCase());
                    matchFound = true;
                }
            }

            // If no match found, display the highest local score
            if (!matchFound && localNames.length > 0) {
                highscore.setText("" + localScores[0]);
            }
            // Everything that needs to be ran once (and not run in initialize()), you can add here
            create = true;
        }

        combocounter.setText(""+(int)ball.getCombo()+ " Combo");

        velocity = lerp(velocity, velocityGoal, velocityInterpolation);
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
            blocks = new BlockGrid(difficulty,pad);
            App.removeElement(ball.getShape());
            ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
        } 

        if (loseCondition()) {
            if (lives <= 0) {
                System.out.println("YOU LOST");
                App.save(username, scoreAmount);
                toggleGameOverScreen();
                winLoseScore.setText("Score: " + scoreAmount);
                timeline.pause();
                writeToDatabase(username, scoreAmount);
                toggleGameOverScreen();
            } else {
                ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            }
        }
        score.setText(""+ scoreAmount);
    }

    public Ball getBall() {
        return ball;
    }

    public void setVelocityInterpolation(double velocityInterpolation) {
        this.velocityInterpolation = velocityInterpolation;
    }

    public BlockGrid getBlockGrid() {
        return blocks;
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
            App.removeElement(ball.getShape());
            lives--; 
            livesnumber.setText(lives + " lives");
            ball = new Ball(pad.getX() + pad.getLength()/2-13/2, pad.getY() - 30,pad,blocks, this);
            backgroundpress.setVisible(true);
            pressspacetext.setVisible(true);
        }

        if (lives <= 0) {
            return true; 
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
                System.out.println("ESCAPE has been pressed");
                if (timeline.getStatus() == Timeline.Status.RUNNING) {
                    System.out.println("TIMELINE IS RUNNING");
                    timeline.pause();  
                    togglePauseScreen();
                } else {
                    System.out.println("TIMELINE IS NOT RUNNING");
                    timeline.play();
                    togglePauseScreen();
                }
                event.consume();
                break;

            case L:
            case D:
            case RIGHT:
                velocityGoal = hSpeed;
                break;
                
            case H:
            case A:
            case LEFT:
                velocityGoal =-hSpeed;
                break;

            case SPACE:
                ball.setMoving(true);
                backgroundpress.setVisible(false);
                pressspacetext.setVisible(false);
                break;

            default:
                break;
        }
    }

    // Called on key release
    public void stopHandling(KeyEvent event) {
        switch (event.getCode()) {
            case L:
            case D:
            case RIGHT:
                if (velocityGoal > 0) {
                    velocityGoal = 0;
                }
                break;

            case H:
            case A:
            case LEFT:
                if (velocityGoal < 0) {
                    velocityGoal = 0;
                }
                break;
            case ESCAPE:
                break;

            default:
                break;
        }
    }

    // Linear interpolation
    public static double lerp(double startValue, double endValue, double interpolationAmount) {
        return (1 - interpolationAmount) * startValue + interpolationAmount * endValue;
    }

    public void Addscore(int scoreAmount) {
        this.scoreAmount += scoreAmount;
    }

    public void Addlivtal(int lives) {
        this.lives += lives;
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
    
    public void sethSpeed(double hSpeed) {
        this.hSpeed = hSpeed;
    }

    private void processLine(String line, HashMap<Integer, String> scoresNamesHash, ArrayList<Integer> scoresList) {
        String[] parts = line.split(":");
        int score = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();

        scoresNamesHash.put(score, name);
        scoresList.add(score);
    }

    private void togglePauseScreen () {
        System.out.println("TOGGLE PAUSE SCREEN CALLED");
        if (gamePauseBackground.isVisible() || pressEscape.isVisible() || gamePause.isVisible()) {
            System.out.println("HIDING PAUSE SCREEN");
            gamePauseBackground.setVisible(false);
            gamePauseBackground.setManaged(false);
            pressEscape.setVisible(false);
            pressEscape.setManaged(false);
            gamePause.setVisible(false);
            gamePause.setManaged(false); 
        } else {
            System.out.println("SHOWING PAUSE SCREEN");
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
        if (!(gameOver.isVisible() && returnTo.isVisible() && mainMenu.isVisible() && gamePauseBackground.isVisible() && winLoseScore.isVisible())) {
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

            gamePauseBackground.toFront();
            gameOver.toFront();
            winLoseScore.toFront();
            returnTo.toFront();
            mainMenu.toFront();

        }
    }
    
    @FXML
    public void onBackButtonClicked() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        App.setRoot(menuPane);
        MenuController menuController = menuLoader.getController();
        menuController.setSplashText();
    }

    @FXML
    public void handleMouseOver() {
        returnTo.setStyle("-fx-fill: white;");
        mainMenu.setStyle("-fx-fill: white;");
    }

    @FXML
    public void handleMouseExit() {
        returnTo.setStyle("");
        mainMenu.setStyle("");
    }
}
