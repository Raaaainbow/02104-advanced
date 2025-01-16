package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class LeaderController {

    /*
    public void populateLeaderBoard() {
        // populates leaderboard based on array
    }
    */

    public void onBackButtonClicked() throws IOException{
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        App.setRoot(menuPane);
    }
}

// when leaderboard array is made, read the leaderboard and add the first 10 along with the highest score on the current username
