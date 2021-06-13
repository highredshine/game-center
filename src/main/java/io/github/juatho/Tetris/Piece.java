package io.github.juatho.Tetris;

import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This is Piece class, where important algorithms for this course is implemented.
 * Piece class represents each piece of Tetris game, or called Tetrimino.
 * THere are several variables and data that stores valuable information to
 * manipulate its conditions.
 * We primarily use 4x2 array of int, to represent the shape of the Piece.
 * 4 for 4 Blocks, 2 for x and y coordinate. 
 * Each Tetrimino also has unique color coordinations as well.
 * Another 4x2 array is used to store the actual location of the piece on the screen.
 * loc and shape is thus different, as loc saves the actual pixel number.
 * prev and cur are used to check if each drop is valid or not.
 * status boolean returns whether this piece can go further down or not.
 * With these variables, the class has several algorithms 
 * to check if a move is valid, and methods to move appropriately
 * for various movements with this valid move checking method.
 * Additionally Piece class has copying method to be used for Control.
 */
public class Piece {
	
	private int[][] _shape;
	private Color _color, _borderColor;
	private int[][] _loc;
	private int[] _prev, _cur;
	private Block[] _blocks;
	private boolean _status;
	
	public Piece() {
		// Stores the current columns and rows of the blocks.
		_loc = new int[4][2];
		// Stores the actual Rectangle nodes of this piece.
		_blocks = new Block[4];
		// Status whether it is settled or not.
		_status = true;
		/*
		 * data structures used for checking valid moves.
		 * instance variables used to minimize space complexity.
		 */
		// prev stores the previous location before softDrop.
		_prev = new int[4];
		// cur stores the current location after softDrop.
		_cur = new int[4];
	}
	
	// Configuration of the colors and shapes into this Piece class
	public void configure(int[][] shape, Color color, Color borderColor) {
		//instance variable associated with arguments later used for copy
		_shape = shape;
		_color = color;
		_borderColor = borderColor;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				_loc[i][j] = _shape[i][j];
			}
			_blocks[i] = new Block(Constants.SQUARE_SIZE,Constants.SQUARE_SIZE,_color);
			_blocks[i].setStrokeWidth(Constants.BORDER);
			_blocks[i].setStroke(_borderColor);
		}
	}
	
	// Copy creation method: used for Hold and Next section in Control
	public void addCopy(Pane nextPane) {
		Piece copy = new Piece();
		copy.configure(_shape, _color, _borderColor);
		// Utilized Group class to make a composite shape
		Group piece = new Group();
		for (int i = 0; i < _shape.length; i++) {
			Block block = copy.getBlocks()[i];
			block.setCol(_shape[i][0]);
			block.setRow(_shape[i][1]);
			piece.getChildren().add(block);
		}
		nextPane.getChildren().add(piece);
	}

	/*
	 * a boolean returning method to check if the potential move is valid or not.
	 * input: board, increments of column and row to move
	 */
	public boolean moveValid(Board board, boolean type, int col, int row) {
		boolean valid = false;
		// create temporary data structure to store potential location
		int[][] temp = new int[4][2];
		for (int i = 0; i < 4; i++) {
			/* store the potential location of column and row
			 * if type is true, we calculate the transitional movement.
			 * if type is false, we calculate the rotational movement.
			 */ 
			if (type) { 
				temp[i][0] = _loc[i][0] + col;
				temp[i][1] = _loc[i][1] + row;
			} else { // Calculate using rotation formula
				/* Center is always the second block, so we use _loc[1].
				* newX = center.x - center.y + oldY
				* newY = center.y + center.x - oldX
				*/
				temp[i][0] = _loc[1][0] - _loc[1][1] + _loc[i][1];
				temp[i][1] = _loc[1][1] + _loc[1][0] - _loc[i][0];
			}
			boolean empty = false;
			// if the temporary coordinate is within the board
			if (temp[i][1] > 0) {
				// check if the board has null space for the potential location
				empty = board.getBoard()[temp[i][0]][temp[i][1]] == null;
			}
			// if so, update the boolean
			if (empty) {
				valid = true;
			// if not, break out of loop; not necessary to check more
			} else {
				valid = false;
				break;
			}
		}
		// If all four squares are valid to move, update the location.
		if (valid) {
			for (int i = 0; i < 4; i++) {
				_loc[i][0] = temp[i][0];
				_loc[i][1] = temp[i][1];
			}
		}
		return valid;
	}
	
	// Foundational method to be used in various types of movements
	public void setLocation(Board board, boolean type, int col, int row) {
		// Piece can only move when the move is valid and its status is not stationary.
		if (this.moveValid(board, type, col, row) && _status) {
			// If this boolean is passed, then new location is updated.
			// With the new location of column and row, calculate pixel.
			for (int i = 0; i < 4; i++) {
				_blocks[i].setCol(_loc[i][0]);
				_blocks[i].setRow(_loc[i][1]);
			}
		}
	}
	
	public void setInitialLocation(Pane gamePane, Board board) {
		// Reset the loc data into default: setLocation does not update going up.
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				_loc[i][j] = _shape[i][j];
			}
		}
		// Initial location of the node: if statements used 
		// so that we can rotate as soon as the piece is created
		if (_shape == Constants.TETRIMINO_I) {
			this.setLocation(board, true, 4, 3);
		} else {
			this.setLocation(board, true, 5, 1);
		}
		// Graphical addition of the nodes
		if (this.onBoard()) {
			gamePane.getChildren().addAll(Arrays.asList(_blocks));
		}
	}
	
	//Move left or right
	public void move(Board board, int dir) {
		//type is true(transitional) for move left or right
		this.setLocation(board, true, dir*1, 0);
	}
	
	// Drop of the piece one row at a time. Used in Timeline and down arrow key.
	public boolean softDrop(Board board) {
		// update _cur with the current location
		for (int i = 0; i < 4; i++) {
			_cur[i] = _loc[i][1];
		}
		//we update prev as equal to_loc before softDropping.
		_prev = Arrays.copyOf(_cur, _cur.length);
		//softDrop (transitional, so type is true)
		this.setLocation(board, true, 0, 1);
		// if softDrop succeeded, _loc is updated, so we update cur as well.
		for (int i = 0; i < 4; i++) {
			_cur[i] = _loc[i][1];
		}
		// if prev and cur is same, that means it cannot go further. so we set status as false.
		if (Arrays.equals(_prev,_cur)) {
			_status = false;
			// We also attach this piece to the board.
			for (int i = 0; i < 4; i++) {
				board.getBoard()[_loc[i][0]][_loc[i][1]] = _blocks[i];
			}
		}
		// we return the status after the softDrop.
		return _status;
	}
	
	//Hard drop
	public void hardDrop(Board board) {
		/* while loop only runs when softDrop returns true; only when softDrop is possible.
		 * if softDrop is not possible anymore, while loop breaks.
		 */
		while (this.softDrop(board)) {
		}
	}
	
	//Rotate algorithms (input Board)
	public void rotate(Board board) {
		// Since square Tetrimino doesn't have to be rotated, we only rotate other ones
		if (_blocks[0].getFill() != Color.YELLOW) {
			// Call setLocation method for rotation, with coordinate parameters set as null.
			this.setLocation(board, false, 0, 0);
		}
	}
	
	/* 
	 * Accessor methods
	 */
	
	public Block[] getBlocks() {
		return _blocks;
	}
	
	public boolean getStatus() {
		return _status;
	}
	
	public boolean onBoard() {
		return _blocks[0].getX() != 0;
	}
	
	public int[][] getShape() {
		return _shape;
	}
}
