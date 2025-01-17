package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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


    public void populateLeaderBoard(String[] leaderboard) throws FileNotFoundException {
        String[] names = new String[leaderboard.length];
        int[] scores = new int[leaderboard.length];

        // Populate the leaderboard arrays
        for (int i = 0; i < leaderboard.length; i++) {
            names[i] = leaderboard[i].split("[:\\s]")[0].toUpperCase();
            scores[i] = Integer.parseInt(leaderboard[i].split("[:\\s]")[2]);
        }

        // Sort the leaderboard by scores (descending)
        sortLeaderboard(names, scores);

        // Populate the Text arrays with top 10 entries
        for (int i = 0; i < names.length; i++) {
            if (i < 10) {
                nameTexts[i].setText(names[i]);
                scoreTexts[i].setText("" + scores[i]);
            }
        }

        // Load local scores and names
        String[] localNames = App.loadName();
        int[] localScores = App.loadScore();

        // Validate local names and scores
        List<String> validNames = new ArrayList<>();
        List<Integer> validScores = new ArrayList<>();
        for (int i = 0; i < localNames.length; i++) {
            if (!localNames[i].isEmpty() && i < localScores.length) {
                validNames.add(localNames[i]);
                validScores.add(localScores[i]);
            }
        }
        localNames = validNames.toArray(new String[0]);
        localScores = validScores.stream().mapToInt(Integer::intValue).toArray();

        // Sort local scores (descending)
        sortLeaderboard(localNames, localScores);

        // Match local scores with leaderboard
        boolean matchFound = false;
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < localNames.length; j++) {
                if (names[i].equalsIgnoreCase(localNames[j]) && scores[i] == localScores[j] && !matchFound) {
                    myname.setText(localNames[j].toUpperCase());
                    myplacement.setText("#" + (i + 1));
                    myscore.setText("" + localScores[j]);
                    matchFound = true;
                }
            }
        }

        // If no match found, display the highest local score
        if (!matchFound && localNames.length > 0) {
            myname.setText(localNames[0].toUpperCase());
            myscore.setText("" + localScores[0]);
        }
    }

    public static void sortLeaderboard(String[] names, int[] scores) {
        boolean swapped;
        int tempScore;
        String tempName;
        for (int i = 0; i < scores.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < scores.length - i - 1; j++) {
                if (scores[j] < scores[j + 1]) {
                    // Swap scores
                    tempScore = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = tempScore;

                    // Swap names
                    tempName = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempName;

                    swapped = true;
                }
            }
            if (!swapped) break;
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

    @FXML
    public void onBackButtonClicked() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        App.setRoot(menuPane);
    }
}
