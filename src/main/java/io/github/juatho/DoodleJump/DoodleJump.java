package io.github.juatho.DoodleJump;

import io.github.juatho.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * This class is PaneOrganizder, the top-level object class of this package.
 * It contains 4 main methods to create each pane of this application.
 * It also contains several accessor methods so that the lower classes
 * like Game or Score can know about the graphical elements
 * and mutate their values as the game is played.
 *
 */
public class DoodleJump implements Game {
	
	private Manager _manager;
	private StackPane _root;
	private VBox _startPane, _gameOverPane;
	private Pane _menu, _gamePane;
	private Label _scoreLabel;
	private Label[][] _scores;
	
	public DoodleJump() {
		//root
		_root = new StackPane();
		_root.setAlignment(Pos.TOP_CENTER);
		//Only start pane is made in the constructor, as the start of the app.
		//Called as an instance variable to be deleted below.
		_startPane = this.createStartPane();
	}
	
	// Start Pane
	public VBox createStartPane() {
		//Used VBox
		VBox startPane = new VBox();
		startPane.setPrefSize(Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
		//Uses an image of the background for the original game
		startPane.setStyle("-fx-background-image: url('"+Constants.BG_URL+"');"
				+ "-fx-background-size:100%;"
				+ "-fx-background-position: center;"
				+ "-fx-background-repeat: no-repeat");
		//The start button
		Button start = new Button("Start");
		start.setOnAction(new StartHandler());
		startPane.setAlignment(Pos.CENTER);
		startPane.getChildren().add(start);
		_root.getChildren().add(startPane);
		return startPane;
	}
	
	// Score Pane
	public GridPane createScorePane() {
		// GridPane is used
		GridPane scorePane = new GridPane();
		scorePane.setMaxSize(Constants.PANE_WIDTH, Constants.SCORE_PANE_HEIGHT);
		scorePane.setStyle("-fx-background-color:grey");
		// Opacity helps us see that platforms are coming down below the score pane.
		scorePane.setOpacity(0.6);
		// Auto-sizing
	    ColumnConstraints col1 = new ColumnConstraints();
	    ColumnConstraints col2 = new ColumnConstraints();
	    col1.setPercentWidth(80);
	    col2.setPercentWidth(20);
	    scorePane.getColumnConstraints().addAll(col1,col2);
	    scorePane.setAlignment(Pos.CENTER);
	    // Elements: score label and the quit button
		_scoreLabel = new Label("0");
		_scoreLabel.setStyle("-fx-font-size:17");
		_scoreLabel.setPadding(new Insets(0,0,0,50));
		Button quit = new Button("Quit");
		quit.setOnAction(new QuitHandler());
		scorePane.add(_scoreLabel, 0, 0, 1, 1);
		scorePane.add(quit, 1, 0, 1, 1);
		_root.getChildren().add(scorePane);
		return scorePane;
	}
	
	// Game Pane
	public Pane createGamePane() {
		Pane gamePane = new Pane();
		gamePane.setMaxSize(Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
		// Uses the same image
		gamePane.setStyle("-fx-background-image: url('"+Constants.BG_URL+"');"
				+ "-fx-background-size:100%;"
				+ "-fx-background-position: center;"
				+ "-fx-background-repeat: no-repeat");
		_root.getChildren().add(gamePane);
		gamePane.toBack();
		return gamePane;
	}
	
	// Game Over Pane
	public VBox createGameOverPane() {
		_gameOverPane = new VBox();
		_gameOverPane.setMaxSize(Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
		// Same Image
		_gameOverPane.setStyle("-fx-background-image: url('"+Constants.BG_URL+"');"
				+ "-fx-background-size:100%;"
				+ "-fx-background-position: center;"
				+ "-fx-background-repeat: no-repeat");
		// Set up game over string
		Label over = new Label("Game Over! \nDo you want to restart?");
		// Set up score list
		GridPane scoreListPane = new GridPane();
		scoreListPane.setMaxSize(Constants.SCORE_LIST_SIZE, Constants.SCORE_LIST_SIZE);
		ColumnConstraints col1 = new ColumnConstraints();
	    ColumnConstraints col2 = new ColumnConstraints();
	    col1.setPercentWidth(70);
	    col2.setPercentWidth(30);
	    scoreListPane.getColumnConstraints().addAll(col1,col2);
		scoreListPane.setAlignment(Pos.CENTER);
		scoreListPane.setStyle("-fx-border-color:black");
		// Array is used as the number of scores is fixed.
		_scores = new Label[5][2];
		// Sets up the names.
		for (int i = 0; i < 5; i++) {
			_scores[i][0] = new Label();
			_scores[i][0].setText("Score #" + String.valueOf(i+1));
			_scores[i][0].setPadding(new Insets(0,0,0,50));
			_scores[i][1] = new Label();
			_scores[i][1].setText("0");
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				_scores[i][j].setTextAlignment(TextAlignment.CENTER);
				scoreListPane.add(_scores[i][j], j, i);
			}
		}
		over.setTextAlignment(TextAlignment.CENTER);
		// set up restart button
		Button restart = new Button("Restart");
		_gameOverPane.setAlignment(Pos.CENTER);
		_gameOverPane.setSpacing(30);
		restart.setOnAction(new RestartHandler());
		// add all
		_gameOverPane.getChildren().addAll(over, scoreListPane, restart);
		return _gameOverPane;
	}

	/*
	 * Accessor methods
	 */
	
	public Pane getGamePane() {
		return _gamePane;
	}
	
	public VBox getGameOverPane() {
		return _gameOverPane;
	}

	public StackPane getRoot() {
		return _root;
	}
	
	public Label getScoreLabel() {
		return _scoreLabel;
	}
	
	public Label[][] getScoreList() {
		return _scores;
	}
	
	// This method is used to always have score pane in front of the
	// Game or Game Over Pane.
	public void setMain(Pane pane) {
		_root.getChildren().add(pane);
		pane.toBack();
	}

	public void setReturn(Pane menu) {
		_menu = menu;
	}
	
	// Start Button setup
	private class StartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			_root.getChildren().remove(_startPane);
			DoodleJump.this.createScorePane();
			_gameOverPane = DoodleJump.this.createGameOverPane();
			_gamePane = DoodleJump.this.createGamePane();
			_manager = new Manager(DoodleJump.this);
			// The Game starts!
			_manager.setupGame();
		}
	}
	
	// Quit Button setup
	private class QuitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {

			_menu.toFront();
		}	
	}
	
	// Restart Button setup
	private class RestartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			// Switches the main pane from Gamve Over to Game
			_root.getChildren().remove(_gameOverPane);
			DoodleJump.this.setMain(_gamePane);
			// Set score back to 0
			_scoreLabel.setText("0");
			// Set up the game again. This method resets all the elements.
			_manager.setupGame();
		}	
	}
}
