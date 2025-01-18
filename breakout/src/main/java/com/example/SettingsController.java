package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class SettingsController {

    @FXML
    private Text goBack;

    @FXML
    public void onBackButtonClicked() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        App.setRoot(menuPane);
    }

    @FXML
    public void handleMouseOver() {
        System.out.println("MOUSE OVER");
        goBack.setStyle("-fx-fill: white;");
    }

    @FXML
    public void handleMouseExit() {
        System.out.println("MOUSE LEFT");
        goBack.setStyle("");
    }
}
