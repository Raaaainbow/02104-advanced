package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class LevelSelectController {

    @FXML
    private Text ez, mid, hardcore, goBack;
    @FXML
    private Button backButton, playButton, ezButton, hardcoreButton, midButton;
    private int difficulty = 0;
    private String username;
    @FXML
    private TextField userInput;
    @FXML
    public void initialize() throws Exception {
        
    }


    @FXML
    public void onEzButtonClicked() {
        difficulty = 0;
        ez.setFill(Color.WHITE);
        hardcore.setFill(Color.rgb(204, 204, 204));
        mid.setFill(Color.rgb(204, 204, 204));
    }

    @FXML
    public void onMidButtonClicked() {
        difficulty = 1;
        mid.setFill(Color.WHITE);
        ez.setFill(Color.rgb(204, 204, 204));
        hardcore.setFill(Color.rgb(204, 204, 204));
    }

    @FXML
    public void onHardcoreButtonClicked() {
        difficulty = 2;
        hardcore.setFill(Color.WHITE);
        ez.setFill(Color.rgb(204, 204, 204));
        mid.setFill(Color.rgb(204, 204, 204));
    }

    @FXML
public void onPlayButtonClicked() throws IOException {
    if (userInput.getText().length() > 0 && userInput.getText().length() <= 10) {
        FXMLLoader primaryLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent primaryPane = primaryLoader.load();
        PrimaryController primaryController = primaryLoader.getController();
        // Set key event handlers for the primary controller
        App.getScene().setOnKeyPressed(primaryController::inputHandling);
        App.getScene().setOnKeyReleased(primaryController::stopHandling);
        primaryController.setUsername(userInput.getText());
        primaryController.setDifficulty(difficulty);

        App.setRoot(primaryPane);
    } else if (userInput.getText().length() == 0) {
        userInput.setText("");
        userInput.setPromptText("least 1 char");
    } else {
        userInput.setText("");
        userInput.setPromptText("max 10 chars");
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
        goBack.setStyle("-fx-fill: white;");
    }

    @FXML
    public void handleMouseExit() {
        goBack.setStyle("");
    }
}

