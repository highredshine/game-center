package io.github.juatho.DoodleJump;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This is Platform class to model each platform rectangle. 
 * The ID method is used to differentiate different types.
 * Instead of using inheritance and create different classes,
 * as their different behaviors are mostly dependent on timeline,
 * it was wiser to use one class with different IDs and implement
 * these behaviors inside the TimeHandler.
 * All positioning methods are made too.
 */
public class Platform {

	private Rectangle _platform;
	private int _id;
	
	public Platform() {
		_platform = new Rectangle();
		_platform.setWidth(Constants.PLATFORM_WIDTH);
		_platform.setHeight(Constants.PLATFORM_HEIGHT);
	}
	
	// setColor
	public void setColor(Color color) {
		_platform.setFill(color);
	}
	
	/*
	 * ID methods
	 */
	public void setID(int id) {
		_id = id;
	}
	
	public int getID() {
		return _id;
	}
	
	// Accessor Method
	public Rectangle getNode() {
		return _platform;
	}
	
	/*
	 * Positioning methods
	 */
	public void setXPos(double x) {
		_platform.setX(x);
	}
	
	public double getXPos() {
		return _platform.getX();
	}
	
	public void setYPos(double y) {
		_platform.setY(y);
	}
	
	public double getYPos() {
		return _platform.getY();
	}
}
