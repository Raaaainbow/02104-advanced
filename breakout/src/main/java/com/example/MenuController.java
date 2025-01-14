package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class MenuController {
    @FXML
    private Button startButton, LeaderBoardButton, Settings; 

    @FXML
    public void onStartButtonClicked() throws IOException {
        FXMLLoader primaryLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent primaryPane = primaryLoader.load();
        PrimaryController primaryController = primaryLoader.getController();

        // Set key event handlers for the primary controller
        App.getScene().setOnKeyPressed(primaryController::inputHandling);
        App.getScene().setOnKeyReleased(primaryController::stopHandling);

        App.setRoot(primaryPane);
    }

    public void onLeaderboardButtonClick() throws IOException {
        FXMLLoader leaderLoader = new FXMLLoader(App.class.getResource("leaderboard.fxml"));
        Parent leaderboardPane = leaderLoader.load();
        LeaderController leaderController = leaderLoader.getController();
        
        App.setRoot(leaderboardPane);
   }

   public void onSettingsButtonClick() throws IOException {
       FXMLLoader settingsLoader = new FXMLLoader(App.class.getResource("settings.fxml"));
       Parent settingsPane = settingsLoader.load();
       SettingsController settingsController = settingsLoader.getController();

       App.setRoot(settingsPane);
   }
}
