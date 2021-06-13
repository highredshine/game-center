package io.github.juatho.DoodleJump;

/**
 * This is your Constants class. It defines some constants you will need
 * in DoodleJump, using the default values from the demo--you shouldn't
 * need to change any of these values unless you want to experiment. Feel
 * free to add more constants to this class!
 *
 * A NOTE ON THE GRAVITY CONSTANT:
 *   Because our y-position is in pixels rather than meters, we'll need our
 *   gravity to be in units of pixels/sec^2 rather than the usual 9.8m/sec^2.
 *   There's not an exact conversion from pixels to meters since different
 *   monitors have varying numbers of pixels per inch, but assuming a fairly
 *   standard 72 pixels per inch, that means that one meter is roughly 2800
 *   pixels. However, a gravity of 2800 pixels/sec2 might feel a bit fast. We
 *   suggest you use a gravity of about 1000 pixels/sec2. Feel free to change
 *   this value, but make sure your game is playable with the value you choose.
 */

public class Constants {

	// Physics
    public static final int GRAVITY = 980; // acceleration constant (UNITS: pixels/s^2)
    public static final int REBOUND_VELOCITY = -980; // initial jump velocity (UNITS: pixels/s)
    public static final double DURATION = 0.0016; // KeyFrame duration (UNITS: s)
    
    // Node constants
    public static final int PLATFORM_WIDTH = 50; // (UNITS: pixels)
    public static final int PLATFORM_HEIGHT = 15; // (UNITS: pixels)
    public static final int DOODLE_WIDTH = 60; // (UNITS: pixels)
    public static final int DOODLE_HEIGHT = 60; // (UNITS: pixels)
    
    // Display
    public static final int PANE_WIDTH = 400;
    public static final int PANE_HEIGHT = 600;
    public static final int SCORE_PANE_HEIGHT = 50;
    public static final int SCORE_LIST_SIZE = 200;
    public static final int MIDDLE = PANE_HEIGHT/2-DOODLE_HEIGHT/2;
    
    // Difficulty level related
    public static final double MIN_X = 0;
    public static final double MAX_X = PANE_WIDTH/3;
    public static final double MIN_Y = 2*PLATFORM_HEIGHT;
    public static final double MAX_Y = PANE_HEIGHT/3;
    public static final double SCALE = 2.15;
    
    // Game initial positions
    public static final double DOODLE_START_X = PANE_WIDTH/2-DOODLE_WIDTH/2;
    public static final double DOODLE_START_Y = PANE_HEIGHT - 100;
    public static final double PLATFORM_INITIAL_X = PANE_WIDTH/2-PLATFORM_WIDTH/2;
    public static final double PLATFORM_INITIAL_Y = DOODLE_START_Y + DOODLE_HEIGHT + 10;
    
    // Constants during the game
    public static final double X_VELOCITY = 0.5;
    public static final double MOVE_PLATFORM_VELOCITY = 0.1;
    
    // Scalar value of multiplication for the distnace
    public static final double SCORE = 3;
    
    // Image urls for the background and the Doodler
    public static final String BG_URL = "https://d14nx13ylsx7x8.cloudfront.net/lesson_images/images/000/001/443/original/temp1406589445.png";
    public static final String DOODLE_URL = "https://vignette.wikia.nocookie.net/doodle-jump/images/5/5c/Doodler.png";
}

