package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class App extends Application {

    private static Scene scene;
    private static Pane rootPane;
    private double width = 672;
    private double height = 970;
    public static String saveFilePath = "breakout/src/main/resources/save.dat";

    @Override
    public void start(Stage stage) throws IOException {
        // Load menu.fxml and get its controller
        FXMLLoader menuLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent menuPane = menuLoader.load();
        MenuController menuController = menuLoader.getController();

        // Set the initial scene to menuPane
        scene = new Scene(menuPane, width, height);
        if (menuPane instanceof Pane) {
            rootPane = (Pane) menuPane;
        }
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.getRoot().requestFocus();

        menuController.setSplashText();
    }

    static void setRoot(String fxml) throws IOException { // Loads root from fxml
        scene.setRoot(loadFXML(fxml));
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public static Scene getScene() {
        return scene;
    }

    static void setRoot(Parent root) {
        scene.setRoot(root);
        if (root instanceof Pane) {
            rootPane = (Pane) root;
        } else {
            rootPane = null;
        }
    }

    public static void addElement(Shape shape) {
        if (rootPane != null) {
            rootPane.getChildren().add(shape); // Add shape to rootPane
        } else {
            System.err.println("Error: Root pane is not initialized!");
        }
    }

    public static void removeElement(Shape shape) {
        rootPane.getChildren().remove(shape);
    }

    private static Parent loadFXML(String fxml) throws IOException { // FXML loader from the vscode javafx project template
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) { // Launching the game
        launch(args);
    }

    public static void makeSaveFile() {
        File file = new File(saveFilePath);
        try {
            // Check if the file already exists
            if (file.exists()) {
                System.out.println(saveFilePath + " already exists.");
            } else {
                // Create a new file
                if (file.createNewFile()) {
                    System.out.println(saveFilePath + " has been created.");
                } else {
                    System.out.println("Failed to create " + saveFilePath);
                }
            }
        } catch (IOException e) {
            // Handle potential IOException
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // The save functions save a value by its keyword in the save.dat file
    // check again
    public static void save(String keyword, int value) throws IOException {
        makeSaveFile();
        try {
            FileWriter writer = new FileWriter(saveFilePath, true);
            writer.append(keyword + ": " + value + "\n");
            writer.close();
        } catch (IOException e){
            System.out.println("there was an error writing the the file \"save.dat\"");
        }

    }

    public static void save(String keyword, String value) throws FileNotFoundException {
        makeSaveFile();
        try {
            FileWriter writer = new FileWriter(saveFilePath);
            writer.write(keyword + ": " + value);
            writer.close();
        } catch (IOException e){
            System.out.println("there was an error writing the the file \"save.dat\"");
        }
    }

    public static String[] loadName() throws FileNotFoundException {
        makeSaveFile();
        ArrayList<String> list = new ArrayList<String>();
        Scanner saver = new Scanner(new File(saveFilePath));
        while (saver.hasNextLine()) {
            list.add(saver.nextLine());
        }
        saver.close();
        String[] result = new String[list.size()];
        for (int i = 0 ; i < list.size() ; i++) {
            result[i] = list.get(i).split("[:\\s]")[0];
        }
        return result;
    }

    public static int[] loadScore() throws FileNotFoundException {
        makeSaveFile();
        ArrayList<String> list = new ArrayList<String>();
        Scanner saver = new Scanner(new File(saveFilePath));
        while (saver.hasNextLine()) {
            list.add(saver.nextLine());
        }
        saver.close();
        int[] result = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++) {
            try {
                result[i] = Integer.parseInt(list.get(i).split("[:\\s]")[2]);
            } catch (NumberFormatException e) {
                result[i] = 0; // Default to 0 if parsing fails
            }
        }
        return result;
    }
}
