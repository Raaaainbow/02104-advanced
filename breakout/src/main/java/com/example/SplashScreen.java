package com.example;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class SplashScreen {
    private static HashMap<Integer, String> splashHash = new HashMap<>();
    private static int counter = 0;
    private static Scanner reader;

    public static String splashGiver() {
        Random rand = new Random();
        return splashHash.get(rand.nextInt(counter));
    }

    public static void splashReader() {
        String fileURL = "Breakout/sec/main/resources/splash.dat";
        File file = new File(fileURL);

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                splashHash.put(counter, reader.nextLine());
                counter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader.close();
    }
}
