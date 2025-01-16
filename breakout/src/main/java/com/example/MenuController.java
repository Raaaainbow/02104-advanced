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
        FXMLLoader levelSelectLoader = new FXMLLoader(App.class.getResource("levelselect.fxml"));
        Parent levelselectPane = levelSelectLoader.load();
        App.setRoot(levelselectPane);
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
