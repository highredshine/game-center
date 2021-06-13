package io.github.juatho.Tetris;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * This is Control class, managing the Control section of the screen,
 * represented by the controlPane. Control section mainly has
 * three sections: hold, numbers, and next pieces.
 * This class contains methods to effectively manage these sections,
 * both graphically and logically.
 * updateHold, updateNumber, updateNext corresponds to these managements,
 * and updateScores and reset are used during gameOver and Restart,
 * managing the final scores and resetting the elements for a new game.
 */
public class Control {

	private VBox _hold, _next;
	private Label _scoreLabel, _levelLabel, _linesLabel;
	private Manager _manager;
	private int _score, _level, _lines;
	private int[] _scoreList;
	
	public Control (Tetris organizer, Manager manager) {
		VBox controlPane = organizer.getControlPane();
		// Hold Section
		_hold = (VBox) ((VBox) controlPane.getChildren().get(0)).getChildren().get(1);;
		// Numbers Section
		VBox numbers = (VBox) controlPane.getChildren().get(1);
		_scoreLabel = (Label) numbers.getChildren().get(1);
		_levelLabel = (Label) numbers.getChildren().get(3);
		_linesLabel = (Label) numbers.getChildren().get(5);
		_scoreList = new int[5];
		// Next Pieces section
		_next = (VBox) ((VBox) controlPane.getChildren().get(2)).getChildren().get(1);
		// Game association
		_manager = manager;
	}
	
	// Hold section: uses Piece class' addCopy method
	public void updateHold() {
		_hold.getChildren().clear();
		_manager.getHold().addCopy(_hold);
	}

	//Score counting for different act: each line is certain point, Tetris is certain point, and so on.
	public void updateNumber(int lines, Timeline timeline) {
		// update total line count
		_lines += lines;
		// score calculation algorithm
		switch(lines) {
		default:
			break;
		case 1:
			_score += _level*40;
			break;
		case 2:
			_score += _level*100;
			break;
		case 3:
			_score += _level*300;
			break;
		case 4:
			_score += _level*1200;
			break;
		}
		//if # of lines divided by 10 rounded to an integer corresponds to a new level, update level.
		if ((int) _lines/10 > _level-1) {
			_level += 1;
			//if level is updated, change the speed of drop
			timeline.setRate(timeline.getRate()+Constants.INCREMENT);
		}
		//Graphical update of these numbers
		_scoreLabel.setText(String.valueOf(_score));
		_levelLabel.setText(String.valueOf(_level));
		_linesLabel.setText(String.valueOf(_lines));
		
	}
	
	// Next piece section: also uses addCopy method
	public void updateNext() {
		_next.getChildren().clear();
		for (int i = 0; i < _manager.getPieces().length; i++) {
			_manager.getPieces()[i].addCopy(_next);
		}
	}
	
	// update the scores for the gameOverPane
	public void updateScore(Label[][] list) {
		// sorts the numbers in decreasing order
		for (int i = 0; i < 5; i++) {
			if (_score >= _scoreList[i]) {
				int j = 4;
				while (j > i) {
					_scoreList[j] = _scoreList[j-1];
					j--;
				}
				_scoreList[i] = _score;
				break;
			}
		}
		// plug each number orderly into the labels
		int k = 0;
		while (k < 5) {
			list[k][1].setText(String.valueOf(_scoreList[k]));
			k++;
		}
	}
	
	// Reset elements for Restart
	public void reset() {
		_hold.getChildren().clear();
		_next.getChildren().clear();
		_score = 0;
		_level = 1;
		_lines = 0;
		_scoreLabel.setText(String.valueOf(_score));
		_levelLabel.setText(String.valueOf(_level));
		_linesLabel.setText(String.valueOf(_lines));
	}
}
