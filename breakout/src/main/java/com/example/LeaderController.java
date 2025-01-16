package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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

    private String databaseUrl = "https://breakout-52014-default-rtdb.europe-west1.firebasedatabase.app/";
    private String path = "/userData.json";
    
    public void initialize() throws Exception {
    System.out.println(Arrays.toString(readDatabaseScore()));
    }


    public void writeToDatabase(String user, int score) throws Exception {
        URL url = new URL(databaseUrl + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);
        String jsonInput = "{\"name\":\""+user+"\", \"score\": "+score+"}"; // Data to send
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    public String[] readDatabaseScore() throws Exception {
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
