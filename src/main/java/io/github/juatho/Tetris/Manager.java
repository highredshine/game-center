package io.github.juatho.Tetris;

import java.util.Arrays;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This is Game class, the main area of this program.
 * It organizes the low-level classes and their corresponding data and objects
 * into coherent "game:" Piece, Board, and Control, with higher-level object
 * PaneOrganizer associated to transfer these data into graphics.
 * The Game class primarily contains two private classes within it:
 * TimeHandler manages the timeline of this game, which goes inifinite until game over.
 * KeyHandler manages the keyboard input the user will make to play the game.
 * The main methods of this class also include setting up the game components,
 * random generation of the pieces, and accessor and mutator methods to control
 * inputs and outputs.
 * 
 */
public class Manager {

	private Tetris _organizer;
	private Pane _gamePane;
	private Control _control;
	private Board _board;
	private Piece _current, _hold;
	private Timeline _timeline;
	private KeyHandler _keyHandler;
	private Piece[] _pieces;
	
	public Manager(Tetris organizer) {
		// Association
		_organizer = organizer;
		_gamePane = _organizer.getGamePane();
		_control = new Control(_organizer,this);
		// Elements
		_board = new Board();
		_board.setupBoard(_gamePane);
		_pieces = new Piece[3];
		_keyHandler = new KeyHandler();
	}
	
	public void setupGame(){
		// set up initial elements (setupBoard called again as this method is used for restart)
		_board.setupBoard(_gamePane);
		_board.clearBoard();
		_control.reset();
		_current = this.generatePiece();
		_current.setInitialLocation(_gamePane, _board);
		// Generate different pieces for the Pieces Array
		_pieces[0] = this.generateDifferentPiece(_pieces[0], _current);
		for (int i = 1; i < _pieces.length; i++) {
			_pieces[i] = this.generateDifferentPiece(_pieces[i], _pieces[i-1]);
		}
		_control.updateNext();
		//add key handler to gamePane
		_gamePane.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
		_gamePane.requestFocus();
		//Set up time line
		KeyFrame keyframe = new KeyFrame(Duration.seconds(1),new TimeHandler());
		_timeline = new Timeline(keyframe);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}
	
	public void setupGameOver() {
		// Clear up and reset the elements.
		_timeline.stop();
		_gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
		//piece list clear
		_pieces = new Piece[3];
		// Pop up the Game Over message.
		 _organizer.getGameOverPane().toFront();
		//scoring
		_control.updateScore(_organizer.getScoreList());
	}
	
	//Random generation of the piece
	public Piece generatePiece() {
		Piece piece = new Piece();
		// random method is used to generate different kind
		int type = (int) (Math.random()*7);
		switch(type) {
			case 0:
				piece.configure(Constants.TETRIMINO_I, Color.SKYBLUE, Color.DEEPSKYBLUE);
				break;
			case 1:
				piece.configure(Constants.TETRIMINO_J, Color.VIOLET, Color.BLUEVIOLET);
				break;
			case 2:
				piece.configure(Constants.TETRIMINO_L, Color.ORANGE, Color.DARKORANGE);
				break;
			case 3:
				piece.configure(Constants.TETRIMINO_O, Color.YELLOW, Color.SANDYBROWN);
				break;
			case 4:
				piece.configure(Constants.TETRIMINO_S, Color.RED, Color.DARKRED);
				break;
			case 5:
				piece.configure(Constants.TETRIMINO_T, Color.DARKVIOLET, Color.PURPLE);
				break;
			case 6:
				piece.configure(Constants.TETRIMINO_Z, Color.GREEN, Color.DARKGREEN);
				break;
			default:
				break;
		}
		// Prevent generation of a same piece in a row
		if (_pieces[2] != null) {
			piece = this.generateDifferentPiece(piece, _pieces[2]);
		}
		return piece;
	}
	
	// An advanced version of generatePiece, for countereffect of bad-quality randomization
	public Piece generateDifferentPiece(Piece piece1, Piece piece2) {
		if (piece1 == null) {
			piece1 = this.generatePiece();
		}
		if (piece2 == null) {
			piece2 = this.generatePiece();
		}
		while (piece1.getShape() == piece2.getShape()) {
			piece1 = this.generatePiece();
		}
		return piece1;
	}
	
	// next piece: updates the _pieces array every time a piece is settled.
	public void next() {
		_current = _pieces[0];
		_current.setInitialLocation(_gamePane, _board);
		_pieces[0] = _pieces[1];
		_pieces[1] = _pieces[2];
		_pieces[2] = this.generatePiece();
		_control.updateNext();
	}
	
	// update: setting a new Piece when a move is not valid anymore.
	public void update() {
		//line clearing
		int lines = _board.clearLines(_gamePane);
		_control.updateNumber(lines,_timeline);
		// next piece
		if (!_current.getStatus()) {
			// update the new piece first
			Manager.this.next();
			// check for game over
			if (_board.gameOver(_current)) {
				Manager.this.setupGameOver();
			}
		}
	}
	
	// toggle the game status for pausing and resuming the game
	public void toggleGameStatus() {
		if (_timeline.getStatus() == Status.PAUSED) {
			_timeline.play();
			_organizer.getPausePane().toBack();
			_gamePane.requestFocus();
			
		} else {
			_timeline.pause();
			_organizer.getPausePane().toFront();
		}
	}
	
	/*
	 * Accessor Methods
	 */
	public Piece[] getPieces() {
		return _pieces;
	}
	
	public Piece getHold() {
		return _hold;
	}
	
	//Time Handler
	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//move the current piece down by 1
			_current.softDrop(_board);
			//update the new piece
			Manager.this.update();
		}
	}
	
	//KeyBoard Handler
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {
			KeyCode keyPressed = e.getCode();
			// Differentiates when the game is paused or not:
			if (_timeline.getStatus() == Status.RUNNING) {
				switch(keyPressed) {
					/*
					 * Up,Down,Left,Right: movements: transitional and rotational
					 */
					case UP:
						_current.rotate(_board);
						break;
					case DOWN:
						_current.softDrop(_board);
						//Start over the timeline to make soft drop steady
						_timeline.playFromStart();
						break;
					case LEFT:
						_current.move(_board, -1);
						break;
					case RIGHT:
						_current.move(_board, 1);
						break;
					// Space: hard drop
					case SPACE:
						_current.hardDrop(_board);
						Manager.this.update();
						break;
					//C is to hold a piece.
					case C:
						_gamePane.getChildren().removeAll(Arrays.asList(_current.getBlocks()));
						if (_hold == null) {
							_hold = _current;
							Manager.this.next();
						// If hold is already there, swap.
						} else {
							Piece temp = _hold;
							_hold = _current;
							_current = temp;
							_current.setInitialLocation(_gamePane, _board);
						}
						_control.updateHold();
						break;
					//P: stop and resume the timeline, disable keyboard handler
					case P:
						Manager.this.toggleGameStatus();
						break;
					default:
						break;
				}
			// if game is paused, only P should work.
			} else {
				switch(keyPressed) {
					case P:
						Manager.this.toggleGameStatus();
						break;
					default:
						break;
				}
			}
			e.consume();
		}
	}
	
}
