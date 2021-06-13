package io.github.juatho.DoodleJump;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main App class for DoodleJump package.
 * The top-level object PaneOrganizer is called,
 * and its getter method for the main root is called as well.
 * The final scene sets it as the root, and the stage is shown.
 * 
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
    	// Instantiate top-level object, set up the scene, and show the stage.
    	DoodleJump organizer = new DoodleJump();
    	Scene scene = new Scene(organizer.getRoot());
    	stage.setScene(scene);
    	stage.setTitle("DoodleJump");
    	stage.show();
    }

    /*
     * Here is the mainline! No need to change this.
     */
    public static void main(String[] argv) {
        // launch is a static method inherited from Application.
        launch(argv);
    }
}
