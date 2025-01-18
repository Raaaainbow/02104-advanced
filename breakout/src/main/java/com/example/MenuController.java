package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class MenuController {
    @FXML
    private Text start, settings, leaderboard, splash;
    private static HashMap<Integer, String> splashHash = new HashMap<>();
    private static int counter;

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
        App.setRoot(leaderboardPane);
    }

    @FXML
    public void onSettingsButtonClick() throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(App.class.getResource("settings.fxml"));
        Parent settingsPane = settingsLoader.load();
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

    @FXML
    public void setSplashText() throws FileNotFoundException {
        splash.setText(splashGiver());
    }

    public static String splashGiver() throws FileNotFoundException {
        splashReader();
        Random rand = new Random();
        return splashHash.get(rand.nextInt(counter));
    }

    public static void splashReader() throws FileNotFoundException {
        String fileURL = "breakout/src/main/resources/splash.dat";
        File file = new File(fileURL);
        counter = 0;
        if (file.exists() && file.canRead()) {
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    splashHash.put(counter, line);
                    counter++;
                }
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("File was not found");
            }
        } else {
            throw new FileNotFoundException("File was not found");
        }
    }
}
