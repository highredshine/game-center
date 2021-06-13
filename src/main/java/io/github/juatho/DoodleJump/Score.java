package io.github.juatho.DoodleJump;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * This class is essentially a calculator that 
 * achieves various kinds of action based on the score.
 * Score class sets and gets the score, updates the minY and maxY
 * to reflect difficulty levels, level checking and label changing
 * methods, and mainly, generates different kind of platform
 * for different level.
 */
public class Score {

	private int _score, _difficulty;
	private double _minY, _maxY;
	private int[] _scoreList;
	
	public Score() {
		_score = 0;
		_difficulty = 1;
		_minY = Constants.MIN_Y;
		_maxY = Constants.PLATFORM_HEIGHT;
		_scoreList = new int[5];
	}
	
	/*
	 * Various mutator and accessor methods
	 */
	public void setScore(int score) {
		_score = score;
	}
	
	public int getScore() {
		return _score;
	}
	
	public void updateMinY() {
		_minY *= Constants.SCALE;
	}
	
	public double getMinY() {
		return _minY;
	}
	
	public void updateMaxY() {
		_maxY += 2*Constants.PLATFORM_HEIGHT;
	}
	
	public double getMaxY() {
		return _maxY;
	}
	
	//helper method used for updating the difficulty level.
	public boolean between(int lower, int upper) {
		if (lower <= _score & _score <= upper) {
			return true;
		} else {
			return false;
		}
	}
	
	// returns the corresponding level for the current score
	public boolean checkUpdated() {
		int before = _difficulty;
		if (between(0,5000)) {
			_difficulty = 1; // stationary platform
		} else if (between(10000,20000)) {
			_difficulty = 2; // breaking platform
		} else if (between(20000,30000)) {
			_difficulty = 3; // movable platform
		} else if (between(30000,40000)) {
			_difficulty = 4; // high-jump platform
		}
		if (before != _difficulty) {
			return true;
		} else {
			return false;
		}
	}
	
	// generate different kind of platforms using the difficulty level
	public ArrayList<Platform> getPlatforms() {
		ArrayList<Platform> newPlatforms = new ArrayList<Platform>();
		Platform p = new Platform();
		// random method is used to generate different kind
		int type = (int) (Math.random()*(_difficulty));
		switch(type) {
		// original
		case 0: default:
			// for each case, we set up the corresponding id and the color
			p.setID(1);
			p.setColor(Color.LIMEGREEN);
			break;
		// break platform
		case 1:
			p.setColor(Color.SADDLEBROWN);
			p.setID(2);
			// for break platform, we also create a support platform
			Platform p1 = new Platform();
			p1.setColor(Color.LIMEGREEN);
			newPlatforms.add(p1);
			break;
		// move platform
		case 2:
			p.setColor(Color.SKYBLUE);
			p.setID(3);
			break;
		// high-jump platform
		case 3:
			p.setColor(Color.INDIANRED);
			p.setID(4);
			break;
		}
		newPlatforms.add(0, p);;
		return newPlatforms;
	}
	
	// update the scores for the label
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

	// reset all values to the default
	public void reset() {
		_score = 0;
		_difficulty = 1;
		_minY = Constants.MIN_Y;
		_maxY = Constants.PLATFORM_HEIGHT;
	}
}
