package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

class MenuController {
    
    @FXML
    public void startGame(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }

    public void leaderboard() {

    }

    public void settings() {

    }
}
