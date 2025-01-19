/// By Victor & Sebastian
/// Controls the view
package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class App extends Application {

    private static final String SAVE_FILE_PATH = "save.dat";
    private static Pane rootPane;
    private static Scene scene;
    private double width = 672;
    private double height = 970;

    // Starts the application on the menu screen
    @Override
    public void start(Stage stage) throws Exception {
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

    // To set a fxml file as the new root
    static void setRoot(String fxml) throws IOException { 
        scene.setRoot(loadFXML(fxml));
    }

    // Used to add to the scene in other files
    public static Scene getScene() {
        return scene;
    }

    // Set a new root from a Parent and sets the rootpane
    static void setRoot(Parent root) {
        scene.setRoot(root);
        if (root instanceof Pane) {
            rootPane = (Pane) root;
        } else {
            rootPane = null;
        }
    }

    // Important method to add elements to the screen
    public static void addElement(Shape shape) {
        if (rootPane != null) {
            rootPane.getChildren().add(shape); // Add shape to rootPane
        } else {
            System.err.println("Error: Root pane is not initialized!");
        }
    }
    // Important method to remove elements from the screen
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

    // The save functions save a value by its keyword in the save.dat file
    public static void save(String keyword, int value) {
        try {
            Path path = Paths.get(SAVE_FILE_PATH);
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(keyword + ": " + value + "\n");
                System.out.println("Saved " + keyword + ": " + value);
            }
        } catch (IOException e) {
            System.out.println("There was an error writing to the file \"save.dat\"");
        }
    }

    // The save functions save a value by its keyword in the save.dat file
    public static void save(String keyword, String value) {
        try {
            Path path = Paths.get(SAVE_FILE_PATH);
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(keyword + ": " + value + "\n");
            }
        } catch (IOException e) {
            System.out.println("There was an error writing to the file \"save.dat\"");
        }
    }

    // Loads a array of keywords in the save.dat file
    public static String[] loadName() {
        ArrayList<String> list = new ArrayList<>();
        Path path = Paths.get(SAVE_FILE_PATH);
        if (Files.exists(path)) {
            try (Scanner saver = new Scanner(path)) {
                while (saver.hasNextLine()) {
                    list.add(saver.nextLine());
                }
            } catch (IOException e) {
                System.out.println("There was an error reading the file \"save.dat\": " + e.getMessage());
            }
        } else {
            System.out.println("The file \"save.dat\" does not exist.");
        }
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).split("[:\\s]")[0];
        }
        return result;
    }

    // Loads a array of scores in the save.dat file
    public static int[] loadScore() {
        ArrayList<String> list = new ArrayList<>();
        Path path = Paths.get(SAVE_FILE_PATH);
        if (Files.exists(path)) {
            try (Scanner saver = new Scanner(path)) {
                while (saver.hasNextLine()) {
                    list.add(saver.nextLine());
                }
            } catch (IOException e) {
                System.out.println("There was an error reading the file \"save.dat\": " + e.getMessage());
            }
        } else {
            System.out.println("The file \"save.dat\" does not exist.");
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {
                result[i] = Integer.parseInt(list.get(i).split("[:\\s]")[2]);
            } catch (NumberFormatException e) {
                result[i] = 0; // Default to 0 if parsing fails
            }
        }
        return result;
    }
}