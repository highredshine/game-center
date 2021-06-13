package io.github.juatho.DoodleJump;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Doodle class is to model the main doodle of the game.
 * It mainly has the positioning related methods.
 */
public class Doodle {

	private Rectangle _doodle;
	private double _xVelocity;
	
	public Doodle() {
		_doodle = new Rectangle();
		_doodle.setWidth(Constants.DOODLE_WIDTH);
		_doodle.setHeight(Constants.DOODLE_HEIGHT);
		// Sets the image of the Doodler as the doodle!
		Image doodler = new Image(Constants.DOODLE_URL);
		_doodle.setFill(new ImagePattern(doodler));
		
	}
	
	// Accessor method
	public Rectangle getNode() {
		return _doodle;
	}
	
	/*
	 * Positioning methods
	 */
	public void setXPos(double x) {
		_doodle.setX(x);
	}
	
	public double getXPos() {
		return _doodle.getX();
	}
	
	public void setYPos(double y) {
		_doodle.setY(y);
	}
	
	public double getYPos() {
		return _doodle.getY();
	}
	
	/*
	 * Velocity methods: used for smooth transition of x position.
	 */
	public void setXVelocity(double velocity) {
		_xVelocity = velocity;
	}
	
	public double getXVelocity() {
		return _xVelocity;
	}
	
	
}
