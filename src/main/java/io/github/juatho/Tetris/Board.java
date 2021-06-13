package io.github.juatho.Tetris;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This is Board class, representing the board of this game.
 * This class contains methods to setup the data structures
 * as well as graphical settings of the board, with
 * algorithms to clear lines or check game over for
 * this game of Tetris.
 *
 */
public class Board {

	private Block[][] _board;
	
	public Board() {
	}
	
	// Graphically sets up the Board array
	public void setupBoard(Pane gamePane) {
		//initial set up of the elements
		for (int i = 0; i < Constants.NUM_COL; i++) {
			for (int j = 0; j < Constants.NUM_ROW; j++) {
				Rectangle square = new Rectangle(Constants.SQUARE_SIZE,Constants.SQUARE_SIZE,Color.BLACK);
				square.setStrokeWidth(Constants.BORDER);
				square.setStroke(Color.GREY);
				/* Set position of each square.
				 * With index*square_size calculated, we increment a bit to centralize the board.
				 */
				square.setX(i * (Constants.SQUARE_SIZE+Constants.BORDER/2) + Constants.COL_ADJUST);
				square.setY(j * (Constants.SQUARE_SIZE+Constants.BORDER/2) + Constants.ROW_ADJUST);
				gamePane.getChildren().add(square);
			}
		}
	}
	
	// The actual data structure of the board has larger size,
	// with buffers for borders.
	public void clearBoard() {
		_board = new Block[Constants.NUM_COL+2][Constants.NUM_ROW+2];
		// Create Buffers
		for (int j = 0; j < Constants.NUM_ROW; j++) {
			_board[0][j] = new Block();
			_board[Constants.NUM_COL+1][j] = new Block();
		}
		for (int i = 1; i <= Constants.NUM_COL; i++) {
			_board[i][Constants.NUM_ROW+1] = new Block();
		}
	}

	// Line clearing algorithm
	public int clearLines(Pane gamePane) {
		int cleared = 0;
		// Looping from the bottom:
		for (int j = 1; j <= Constants.NUM_ROW; j++) {
			boolean filled = true;
			for (int i = 1; i <= Constants.NUM_COL; i++) {
				// if there is a null space in this row, change boolean to false.
				if (_board[i][j] == null) {
					filled = false;
					break; // saves runtime
				}
			}
			// after running through all the columns, if boolean stays true:
			if (filled) {
				// This line will be cleared, so increment total number of cleared lines.
				cleared += 1;
				// clear both logical and graphical nodes of this row.
				for (int i = 1; i <= Constants.NUM_COL; i++) {
					gamePane.getChildren().remove(_board[i][j]);
					_board[i][j] = null;
					// drag down all the nodes in this column that are above this row 
					for (int k = j; k > 0; k--) {
						// update the node below with the one above.
						_board[i][k] = _board[i][k-1];
						_board[i][k-1] = null;
						// for filled blocks, update the graphical position as well.
						if (_board[i][k] != null) {
							_board[i][k].setRow(k);
						}
					}
				}
			}
		}
		return cleared;
	}
	
	/*
	 * Game Over check
	 */
	public boolean gameOver(Piece current) {
		boolean over = false;
		/* first case: if a newly generated piece cannot move down.
		 * Since we call this method when the piece status is false,
		 * a piece would not have a graphical location: it would be set as 0.
		 */
		if (!current.onBoard()) {
			over = true;
		}
		/* second case: when a piece is on top row of the board:
		 * This includes a case when a piece is settled and 
		 * its location is over the board, which means the buffer zone.
		 * So we consider both the top row and the buffer row
		 * to check if any of the nodes there is filled.
		 */
		for (int i = 1; i <= Constants.NUM_COL; i++) {
			// buffer zone has the index of 0.
			if (_board[i][0] != null || _board[i][1] != null ) {
				over = true;
				break;
			}
		}
		return over;
	}
	
	/*
	 * Accessor methods
	 */
	public Rectangle[][] getBoard(){
		return _board;
	}
	
}
