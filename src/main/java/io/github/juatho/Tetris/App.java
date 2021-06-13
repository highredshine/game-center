package io.github.juatho.Tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is App class for this package Tetris.
  * This is the  main class to get things started.
  * The main method of this application calls the start method.
  * 
  * PaneOrganizer is the top-level object of this package.
  * It contains the root layout to be used for the scene.
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
    	Tetris organizer = new Tetris();
    	Scene scene = new Scene(organizer.getRoot());
    	stage.setScene(scene);
    	stage.setTitle("Tetris");
    	stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
