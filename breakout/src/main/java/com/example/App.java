package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class App extends Application {

    private static Scene scene;
    Parent root;
    private static Pane rootPane;
    private double width = 672;
    private double height = 970;
    

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        rootPane = (Pane) fxmlLoader.load();  // Initialize rootPane here
        stage.setResizable(false);
        PrimaryController controller = fxmlLoader.getController();
        scene = new Scene(rootPane, width, height);

        scene.setFill(Color.web("#000000")); // Background
        
        // Handle events 
        startTimeline(controller);
        scene.setOnKeyReleased(event -> { // Key release event
            controller.stopHandling(event);
        });
        scene.setOnKeyPressed(event -> { // Key press event
            controller.inputHandling(event);
        });
        stage.setScene(scene);
        stage.show();
    }
    
    public void startTimeline(PrimaryController controller) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(10), event -> {
                controller.onStep();  // Calls the step event stored in our controller
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();
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

    public Parent getRoot() {
        return root;
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

    private static void makeSaveFile() {
        String fileName = "save.dat";
        File file = new File(fileName);
        try {
            // Check if the file already exists
            if (file.exists()) {
                System.out.println(fileName + " already exists.");
            } else {
                // Create a new file
                if (file.createNewFile()) {
                    System.out.println(fileName + " has been created.");
                } else {
                    System.out.println("Failed to create " + fileName);
                }
            }
        } catch (IOException e) {
            // Handle potential IOException
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // The save functions save a value by its keyword in the save.dat file
    public static void save(String keyword, Number value) throws IOException {
        makeSaveFile();
    }

    public static void save(String keyword, String value) throws FileNotFoundException {
        makeSaveFile();
        Scanner saver = new Scanner(new File("save.dat"));
        saver.close();
    }

    // loads a value by its keyword
    public static int loadNumber(String keyword) throws FileNotFoundException {
        makeSaveFile();
        String line = "";
        Scanner saver = new Scanner(new File("save.dat"));
        while (saver.hasNextLine()) {
            line = saver.nextLine();
            if (line.contains(keyword)) {
                line.replace(keyword+": ", "");
                break;
            }
        }
        saver.close();
        return Integer.parseInt(line);
    }

    public static String loadString(String keyword) throws FileNotFoundException {
        makeSaveFile();
        String line = "";
        Scanner saver = new Scanner(new File("save.dat"));
        while (saver.hasNextLine()) {
            line = saver.nextLine();
            if (line.contains(keyword)) {
                line.replace(keyword+": ", "");
                break;
            }
        }
        saver.close();
        return line;
    }

    public static void main(String[] args) { // Launching the game
        launch(args);
    }
}
