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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;


public class App extends Application {

    private static Scene scene;
    private static Pane rootPane;
    private double width = 672;
    private double height = 970;
    public static String saveFilePath = "breakout/src/main/resources/save.dat";
    private static InputStream inputStream = App.class.getResourceAsStream("/save.dat");
    private static final String SAVE_FILE_PATH = "/save.dat";


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

    // The save functions save a value by its keyword in the save.dat file
    // check again
      public static void save(String keyword, int value) {
        try (FileWriter writer = new FileWriter(new File(App.class.getResource(SAVE_FILE_PATH).toURI()), true)) {
            writer.write(keyword + ": " + value + "\n");
            System.out.println("Saved " + keyword + ": " + value);
            System.out.println(App.class.getResource(SAVE_FILE_PATH));
        } catch (IOException | URISyntaxException e) {
            System.out.println("There was an error writing to the file \"save.dat\"");
        }
    }


    public static void save(String keyword, String value) {
        try (FileWriter writer = new FileWriter(new File(App.class.getResource(SAVE_FILE_PATH).toURI()))) {
            writer.write(keyword + ": " + value + "\n");
        } catch (IOException | URISyntaxException e) {
            System.out.println("There was an error writing to the file \"save.dat\"");
        }
    }

    public static String[] loadName() {
        ArrayList<String> list = new ArrayList<>();
        try (InputStream inputStream = App.class.getResourceAsStream(SAVE_FILE_PATH);
             Scanner saver = new Scanner(inputStream)) {
            while (saver.hasNextLine()) {
                list.add(saver.nextLine());
            }
        } catch (IOException e) {
            System.out.println("There was an error reading the file \"save.dat\"");
        }
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).split("[:\\s]")[0];
        }
        return result;
    }

    public static int[] loadScore() {
        ArrayList<String> list = new ArrayList<>();
        try (InputStream inputStream = App.class.getResourceAsStream(SAVE_FILE_PATH);
             Scanner saver = new Scanner(inputStream)) {
            while (saver.hasNextLine()) {
                list.add(saver.nextLine());
            }
        } catch (IOException e) {
            System.out.println("There was an error reading the file \"save.dat\"");
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
