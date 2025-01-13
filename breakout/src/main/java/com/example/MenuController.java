package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    @FXML
    private Button startButton, LeaderBoardButton, Settings; 

    @FXML
    public void onStartButtonClicked() throws IOException {
        App.setRoot("primary");
    }
}
