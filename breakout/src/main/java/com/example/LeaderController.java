package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LeaderController {

    public static String databaseUrl = "https://breakout-52014-default-rtdb.europe-west1.firebasedatabase.app/";
    public static String path = "/userData.json";
    @FXML
    private Text name1, name2, name3, name4, name5, name6, name7, name8, name9, name10;
    @FXML   
    private Text score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;
    @FXML
    private Text myscore, myplacement, myname;
    private Text[] nameTexts;
    private Text[] scoreTexts;
    private Text[] userTexts;

    public void initialize() throws Exception {
        nameTexts = new Text[] {name1, name2, name3, name4, name5, name6, name7, name8, name9, name10};
        scoreTexts = new Text[] {score1, score2, score3, score4, score5, score6, score7, score8, score9, score10};
        userTexts = new Text[] {myplacement, myname, myscore};
        populateLeaderBoard(readDatabase());
    }


    public void populateLeaderBoard(String[] leaderboard) {
        // populates leaderboard based on array
        String[] names = new String[leaderboard.length];
        int[] scores = new int[leaderboard.length];
        


        // Populate the lists
        for (int i = 0 ; i < leaderboard.length ; i++) {
            names[i] = leaderboard[i].split("[:\\s]")[0].toUpperCase();
            scores[i] = Integer.parseInt(leaderboard[i].split("[:\\s]")[2]);
        }

        // Sorting
        int temp;
        String temp2;
        boolean swapped;
        for (int i = 0; i < scores.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < scores.length - i - 1; j++) {
                if (scores[j] < scores[j + 1]) {
                    
                    // Swap
                    temp = scores[j];
                    temp2 = names[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = temp;
                    names[j] = names[j+1];
                    names[j + 1] = temp2;
                    swapped = true;
                }
            }
            if (swapped == false) // To save time, we will go out of the loop if there's nothing left to sort
                break;
        }
        
        // Populating the Text arrays
        for (int i = 0 ; i < names.length ; i++) {
            if (i < 10) {
                nameTexts[i].setText(names[i]);
                scoreTexts[i].setText(""+scores[i]);
            }
            
        }

    }

    public String[] readDatabase() throws Exception {
        URL url = new URL(databaseUrl + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String response;
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }
            response = responseBuilder.toString();
            System.out.println("GET Response: " + response);
        }
    
        // Manual parsing of JSON
        List<String> scores = new ArrayList<>();
        response = response.substring(1, response.length() - 1); // Remove outer braces
        String[] entries = response.split("},"); // Split into individual user entries
    
        for (String entry : entries) {
            entry = entry.trim();
            if (!entry.endsWith("}")) {
                entry += "}"; // Add missing closing brace for last entry
            }
    
            // Extract user data
            int colonIndex = entry.indexOf(':');
            if (colonIndex == -1) continue; // Skip if no valid data found
            String userData = entry.substring(colonIndex + 1).trim();
    
            // Parse fields manually
            String[] fields = userData.substring(1, userData.length() - 1).split(","); // Remove outer braces and split
            String name = "";
            int score = 0;
    
            for (String field : fields) {
                String[] pair = field.replace("\"", "").split(":");
                if (pair.length != 2) continue;
    
                String key = pair[0].trim();
                String value = pair[1].trim();
    
                if (key.equals("name")) {
                    name = value;
                } else if (key.equals("score")) {
                    score = Integer.parseInt(value);
                }
            }
    
            scores.add(name + ": " + score);
        }
    
        return scores.toArray(new String[0]);
    }

    public void onBackButtonClicked() throws Exception{
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        App.setRoot(menuPane);
    }
}

// when leaderboard array is made, read the leaderboard and add the first 10 along with the highest score on the current username
