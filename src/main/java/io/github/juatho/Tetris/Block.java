package io.github.juatho.Tetris;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/** 
 * This is Block class. It is the building "block"
 * of this whole game: the pieces, and the board.
 * It is a subclass of the built-in JavaFX Rectangle class.
 * This means that it can use methods from Rectangle,
 * but it actually conetains uqnie methods of setCol and setRow
 * that uses built-in methods of setX and setY.
 */
public class Block extends Rectangle{

	public Block() {
		super();
	}
	
	public Block(double width, double height, Paint fill) {
		super(width, height, fill);
	}
 	
	/*
	 * setCol and setRow uses mathematical calculation to 
	 * get the exact pixel location of each block.
	 */
	
	public void setCol(int col) {
		this.setX(col * (Constants.SQUARE_SIZE+Constants.BORDER/2) 
				+ Constants.COL_ADJUST - Constants.BUFFER_ADJUST);
	}
	
	public void setRow(int row) {
		this.setY(row * (Constants.SQUARE_SIZE+Constants.BORDER/2) 
				+ Constants.ROW_ADJUST - Constants.BUFFER_ADJUST);
	}
}
