package io.github.juatho.DoodleJump;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This is the Game class. The Game class knows about the top-level object,
 * as most of the elements in this game are graphical as well.
 * So the panes in the organizer are accessed, and used to add the children.
 * Besides its main method of setting up the game, the game class
 * is consisted of private classes of event handlers, mainly
 * the timeline handler and the keyboard handler, the two main
 * pillars of this game. The specific design of the algorithms
 * are explained in detail below.
 *
 */
public class Manager {

	private DoodleJump _organizer;
	private Pane _gamePane;
	private VBox _gameOverPane;
	private Timeline _timeline;
	private Score _score;
	private Label _scoreLabel;
	private Doodle _doodle;
	private ArrayList<Platform> _platforms;
	/*
	 *  Important: keyhandler is created as a variable because
	 *  it is not called only once. For each restart, the keyhandler
	 *  is added back to the pane.
	 */
	private KeyHandler _keyHandler;
	
	public Manager(DoodleJump organizer) {
		// Association
		_organizer = organizer;
		_gamePane = _organizer.getGamePane();
		_gameOverPane = _organizer.getGameOverPane();
		//Set up the elements: We just instantiate the main elements of this game.
		_doodle = new Doodle();
		_platforms = new ArrayList<Platform>();
		_keyHandler = new KeyHandler();
		_score = new Score();
		_scoreLabel = _organizer.getScoreLabel();
	}
	
	/*
	 *  After we set up the elements in the constructor, 
	 *  we decorate them with specific characteristics. 
	 */
	public void setupGame() {
		//Doodler
		_doodle.getNode().setX(Constants.DOODLE_START_X);
		_doodle.getNode().setY(Constants.DOODLE_START_Y);
		_gamePane.getChildren().add(_doodle.getNode());
		//Initial platform
		Platform initial = new Platform();
		initial.setColor(Color.LIMEGREEN);
		initial.setXPos(Constants.PLATFORM_INITIAL_X);
		initial.setYPos(Constants.PLATFORM_INITIAL_Y);
		_platforms.add(initial);
		_gamePane.getChildren().add(initial.getNode());
		//Set KeyBoard Interaction
		_gamePane.addEventHandler(KeyEvent.ANY, _keyHandler);
		_gamePane.requestFocus();
		//Set up time line
		KeyFrame keyframe = new KeyFrame(Duration.seconds(Constants.DURATION),new TimeHandler());
		_timeline = new Timeline(keyframe);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}

	// Timeline Handler
	private class TimeHandler implements EventHandler<ActionEvent> {

		private double _velocity;
		private int _sign;
		
		public TimeHandler() {
			// Velocity is used as instance variable to be constantly updated.
			_velocity = 0;
			// This variable is used to control move platforms' direction of movement
			_sign = 1;
		}

		@Override
		public void handle(ActionEvent event) {
			// update
			_velocity = _velocity + Constants.GRAVITY*Constants.DURATION;
			double newYPos = _doodle.getYPos() + _velocity*Constants.DURATION;
			// scroll platforms down
			if (_doodle.getYPos() < Constants.MIDDLE) {
				// first calculate the distance
				double dist = Constants.MIDDLE - newYPos;
				// Set up doodle in a constant position
				_doodle.setYPos(Constants.MIDDLE);
				// update platform positions (scrolling)
				for (int i = 0; i < _platforms.size(); i++) {
					_platforms.get(i).setYPos(_platforms.get(i).getYPos()+dist);
				}
				// update score with the distance as the variable
				_score.setScore(_score.getScore() + (int)(Constants.SCORE*dist));
				_scoreLabel.setText(String.valueOf(_score.getScore()));
			} else { //if the doodle is still below the midpoint
				_doodle.setYPos(newYPos);
			}
			// bounce
			if (_velocity > 0) { // only when the platform is going down
				for (int i = 0; i < _platforms.size(); i++) {
					// if any of the platforms intersect with the doodle
					if (_platforms.get(i).getNode().intersects(
							_doodle.getXPos(), _doodle.getYPos()+Constants.DOODLE_HEIGHT, 
							Constants.DOODLE_WIDTH, 0)) {
						// switch method used to differentiate the behavior of doodle
						// for different type of platforms
						switch (_platforms.get(i).getID()) {
							// for normal and move platforms
							case 1: case 3: default:
								_velocity = Constants.REBOUND_VELOCITY;
								break;
							// for break platforms
							case 2:
								// slow down the velocity
								_velocity /= 2;
								// remove the platform
								_gamePane.getChildren().remove(_platforms.get(i).getNode());
								_platforms.remove(_platforms.get(i));
								break;
							// for high-jump platform
							case 4:
								_velocity = 2*Constants.REBOUND_VELOCITY;
								break;
						}
						break;
					}
				}
			}
			// This is to check certain behaviors for the platforms
			for (int i = 0; i < _platforms.size(); i++) {
				// for MovePlatform's behavior of changing direction
				if (_platforms.get(i).getID() == 3) {
					if (_platforms.get(i).getXPos() <= 1) {
						_sign = 1;
					}
					if (_platforms.get(i).getXPos() >= Constants.PANE_WIDTH - Constants.PLATFORM_WIDTH ) {
						_sign = -1;
					}
					_platforms.get(i).setXPos( _platforms.get(i).getXPos() + _sign*Constants.MOVE_PLATFORM_VELOCITY);
				}
				// remove platforms if they are below the screen now
				if (_platforms.get(i).getYPos() > Constants.PANE_HEIGHT) {
					_gamePane.getChildren().remove(_platforms.get(i).getNode());
					_platforms.remove(i);
				}
			}
			// generate more platforms 
			Platform top = _platforms.get(_platforms.size()-1);
			while (top.getYPos() >= Constants.SCORE_PANE_HEIGHT) {
				//ArrayList is used to consider the break platform case.
				//Array is avoided to avoid numeorus null spaces that will be created.
				ArrayList<Platform> newPlatforms = _score.getPlatforms();
				// semi-random positioning of the new platform
				// sign and rand methods are used to determine xPos
				double xPosDist = this.sign() * this.rand(Constants.MIN_X, Constants.MAX_X);
				double xPos = top.getXPos() + xPosDist;
				// this condition is when part of the platform is out of bounds from the screen
				if (xPos > Constants.PANE_WIDTH-Constants.PLATFORM_WIDTH || xPos < 0) {
					xPos = top.getXPos() - xPosDist;
				}
				// the minimum range for the y position changes as levels increase
				double yPos = top.getYPos() - this.rand(_score.getMinY(), Constants.MAX_Y);
				_platforms.addAll(newPlatforms);
				// add it graphically
				for (int i = 0; i < newPlatforms.size(); i++) {
					_gamePane.getChildren().add(newPlatforms.get(i).getNode());
				}
				// for BreakPlatform: it is important to add the second support platform as well
				if (newPlatforms.size() > 1) {
					newPlatforms.get(1).setXPos(xPos + this.sign() * this.rand(0, Constants.PLATFORM_WIDTH));
					//maxY is the distance between the break platform and the support platform.
					//It changes as the difficulty level increases.
					newPlatforms.get(1).setYPos(yPos + _score.getMaxY());
				}
				// Final set up and new top update
				Platform p = newPlatforms.get(0);
				p.setXPos(xPos);
				p.setYPos(yPos);
				top = p;
			}
			// update difficulty level, and make the game harder by changing variables
			if (_score.checkUpdated()) {
				_score.updateMinY();
				_score.updateMaxY();
			}
			// check game over
			if (_doodle.getYPos() > Constants.PANE_HEIGHT) {
				//Clear up and reset all the elements
				_timeline.stop();
				_gamePane.getChildren().clear();
				_keyHandler.getDoodleTimeline().stop();
				_gamePane.removeEventHandler(KeyEvent.ANY, _keyHandler);
				_platforms.clear();
				_organizer.getRoot().getChildren().remove(_gamePane);
				//Set the game over pane
				_organizer.setMain(_gameOverPane);
				//Save the score
				_score.updateScore(_organizer.getScoreList());
				_score.reset();
			}
		}
		
		// This generates a random number in a range of two numbers
		public double rand(double min, double max) {
			return (Math.random()*((max-min)+1) + min);
		}
		
		// This creates a random sign of either positive or negative
		public double sign() {
			int sign = (int) (Math.random()*2);
			if (sign == 0) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	// Keyboard Handler
	private class KeyHandler implements EventHandler<KeyEvent> {
		
		private Timeline _doodleTimeline;
		private KeyCode _key;
		
		public KeyHandler() {
			//As the components of each keyframe and the timeline will not be 
			//altered even after game over we set it up in the constructor.
			KeyFrame keyframe = new KeyFrame(Duration.seconds(Constants.DURATION),new DoodleHandler());
			_doodleTimeline = new Timeline(keyframe);
			_doodleTimeline.setCycleCount(Animation.INDEFINITE);
		}
		
		@Override
		public void handle(KeyEvent e) {
			_key = e.getCode();
			// As we set up the KeyEvent type for the handler as ANY, we differentiate
			// for cases when it is pressed or released.
			if (e.getEventType() == KeyEvent.KEY_PRESSED) {
				switch(_key) {
					// When pressed, the doodle moves, so x_velocity is nonzero
					case LEFT: case RIGHT:
							_doodle.setXVelocity(Constants.X_VELOCITY);
							_doodleTimeline.play();
						break;
					/*
					 * aside: this started out as a debugging tool to
					 * stop the game for a second and check the eclipse console,
					 * but it kind of became something like a hidden cheat when
					 * I let my friends play my game! So only those who know about this
					 * will use. :)
					 * You can simply pause the time, so gravity will not be effective.
					 * But moving x is still effective, so you can stop the doodle for a second
					 * and move it to where you want it to be (just for horizontal).
					 */
					case SPACE:
						_timeline.pause();
						break;
					default:
						break;
				}
			// When the key is released, it stops
			} else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
				switch(_key) {
					case LEFT: case RIGHT:
						_doodle.setXVelocity(0);
						_doodleTimeline.pause();
						break;
					case SPACE:
						_timeline.play();
						break;
					default:
						break;
				}
			}
			e.consume();
		}
		
		public Timeline getDoodleTimeline() {
			return _doodleTimeline;
		}
		
		private class DoodleHandler extends TimeHandler {
			
			@Override
			public void handle(ActionEvent e) {
				// move the doodle back to the screen if it moves beyond the borders.
				if (_doodle.getXPos()+Constants.DOODLE_WIDTH <= 0) {
					_doodle.setXPos(Constants.PANE_WIDTH - 1);
				} else if (_doodle.getXPos() >= Constants.PANE_WIDTH) {
					_doodle.setXPos(-Constants.DOODLE_WIDTH + 1);
				} else {
					// set up the new position with smooth animation
					double newXPos = 0;
					if (_key.toString() == "LEFT") {
						newXPos = _doodle.getXPos() - _doodle.getXVelocity();
					} else if (_key.toString() == "RIGHT") {
						newXPos = _doodle.getXPos() + _doodle.getXVelocity();
					}
					_doodle.setXPos(newXPos);
				}
			}
		}
	}
}
