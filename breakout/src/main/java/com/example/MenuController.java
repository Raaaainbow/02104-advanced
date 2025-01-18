package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class MenuController {
    @FXML
    private Text start, settings, leaderboard;

    @FXML
    public void onStartButtonClicked() throws IOException {
        FXMLLoader levelSelectLoader = new FXMLLoader(App.class.getResource("levelselect.fxml"));
        Parent levelselectPane = levelSelectLoader.load();
        App.setRoot(levelselectPane);
    }

    @FXML
    public void onLeaderboardButtonClick() throws IOException {
        FXMLLoader leaderLoader = new FXMLLoader(App.class.getResource("leaderboard.fxml"));
        Parent leaderboardPane = leaderLoader.load();
        LeaderController leaderController = leaderLoader.getController();
        
        App.setRoot(leaderboardPane);
   }

   @FXML
   public void onSettingsButtonClick() throws IOException {
       FXMLLoader settingsLoader = new FXMLLoader(App.class.getResource("settings.fxml"));
       Parent settingsPane = settingsLoader.load();
       SettingsController settingsController = settingsLoader.getController();

       App.setRoot(settingsPane);
   }

   @FXML
   public void handleMouseOver() {
        if (start.isHover()) {
            start.setStyle("-fx-fill: white;");
        } else if (settings.isHover()) {
            settings.setStyle("-fx-fill: white;");
        } else if (leaderboard.isHover()) {
            leaderboard.setStyle("-fx-fill: white;");
        }
   }

   @FXML
   public void handleMouseExit() {
       start.setStyle("");
       settings.setStyle("");
       leaderboard.setStyle("");
   }
}
