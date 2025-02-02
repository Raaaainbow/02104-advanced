/// By Sebastian & Victor
/// Controls the main menu buttons and text
package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class MenuController {
    @FXML
    private Text start, settings, leaderboard, quit, splash;
    private static HashMap<Integer, String> splashHash = new HashMap<>();
    private static int counter;

    // Go to difficulty & username selection
    @FXML
    public void onStartButtonClicked() throws IOException {
        FXMLLoader levelSelectLoader = new FXMLLoader(App.class.getResource("levelselect.fxml"));
        Parent levelselectPane = levelSelectLoader.load();
        App.setRoot(levelselectPane);
    }

    // Go to leaderboard
    @FXML
    public void onLeaderboardButtonClick() throws IOException {
        FXMLLoader leaderLoader = new FXMLLoader(App.class.getResource("leaderboard.fxml"));
        Parent leaderboardPane = leaderLoader.load();
        App.setRoot(leaderboardPane);
    }

    // Go to keybinds tab
    @FXML
    public void onSettingsButtonClick() throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(App.class.getResource("settings.fxml"));
        Parent settingsPane = settingsLoader.load();
        App.setRoot(settingsPane);
    }

    // Quit the game
    @FXML
    public void onQuitButtonClick() throws IOException {
        System.exit(0);
    }

    // cool mouse over effect
    @FXML
    public void handleMouseOver() {
        if (start.isHover()) {
            start.setStyle("-fx-fill: white;");
        } else if (settings.isHover()) {
            settings.setStyle("-fx-fill: white;");
        } else if (leaderboard.isHover()) {
            leaderboard.setStyle("-fx-fill: white;");
        } else if (quit.isHover()) {
            quit.setStyle("-fx-fill: white;");
        }
    }

    @FXML
    public void handleMouseExit() {
        start.setStyle("");
        settings.setStyle("");
        leaderboard.setStyle("");
        quit.setStyle("");
    }


    // Funny splash screen text
    @FXML
    public void setSplashText() throws FileNotFoundException {
        splash.setText(splashGiver());
    }

    public static String splashGiver() throws FileNotFoundException {
        splashReader();
        Random rand = new Random();
        return splashHash.get(rand.nextInt(counter));
    }

    // Read the splash screen data from file
    public static void splashReader() throws FileNotFoundException {
        InputStream inputStream = MenuController.class.getResourceAsStream("/splash.dat");
        counter = 0;
        if (inputStream != null) {
            try (Scanner reader = new Scanner(inputStream)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    splashHash.put(counter, line);
                    counter++;
                }
            }
        } else {
            throw new FileNotFoundException("File was not found");
        }
    }
}
