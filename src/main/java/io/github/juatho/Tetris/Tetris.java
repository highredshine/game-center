package io.github.juatho.Tetris;

import io.github.juatho.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * This is the PaneOrganizer class, the top-level object of this package.
 * The root of this program is a StackPane, and there are total
 * 4 stacks to be put on: start screen, main screen, pause screen, and gameOver screen.
 * The start screen contains teh title and the start button.
 * The main screen contains the gamePane and the controlPane, each managed
 * by the corresponding class: Game and Control.
 * The pause screen and game over screen comes up for certain situations,
 * when pressing the pause button or when the game is over.
 * Except the main screen, all other screens utilize blurEffect,
 * an illusion created by setting opacity lower for other screens so that
 * they are stacking up on the main screen.
 * The class mainly has methods to set up these screens individually,
 * some accessor methods for the lower classes associated with PaneOrganizer
 * to access the screens, and some ButtonHandlers for the buttons existent
 * in the screen.
 * 
 */
public class Tetris implements Game {

	private BorderPane _main;
	private StackPane _root, _gameOverPane, _pausePane;
	private Pane _menu, _gamePane;
	private VBox _controlPane;
	private Manager _manager;
	private Label[][] _scores;
	
	public Tetris() {
		// root
		_root = new StackPane();
		_root.setMaxSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		_main = new BorderPane();
		// set up gameOverPane and pausePane to be stacked first.
		this.createPausePane();
		this.createGameOverPane();
		// set up main pane
		this.createGamePane();
		this.createControlPane();
		_manager = new Manager(this);
		// add main as last.
		_root.getChildren().add(_main);
		// Start!
		this.createStartPane();
	}
	
	// Start Pane
	public StackPane createStartPane() {
		StackPane startPane = new StackPane();
		// blur effect
		Pane blurEffect = new Pane();
		blurEffect.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		blurEffect.setStyle("-fx-background-color:black;-fx-opacity:0.3");
		VBox contents = new VBox();
		contents.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		contents.setAlignment(Pos.CENTER);
		Pane title = new Pane();
		title.setPrefSize(Constants.TITLE_SIZE, Constants.TITLE_SIZE);
		//title image
		title.setStyle("-fx-background-image: url('"+Constants.IMAGE_URL+"');"
				+ "-fx-background-size:80%;"
				+ "-fx-background-position: center;"
				+ "-fx-background-repeat: no-repeat");
		//The start button
		Button start = new Button("Start");
		start.setOnAction(new StartHandler());
		contents.getChildren().addAll(title,start);
		startPane.getChildren().addAll(blurEffect, contents);
		_root.getChildren().add(startPane);
		return startPane;
	}
	
	// Game Pane
	public void createGamePane() {
		_gamePane = new Pane();
		_gamePane.setPrefSize(Constants.GAME_PANE_WIDTH, Constants.SCREEN_HEIGHT);
		_gamePane.setStyle("-fx-background-color:grey");
		_main.setCenter(_gamePane);
	} 
	
	// Control Pane
	public void createControlPane() {
		_controlPane = new VBox();
		_controlPane.setPrefSize(Constants.CONTROL_PANE_WIDTH, Constants.SCREEN_HEIGHT);
		_controlPane.setStyle("-fx-background-color:black");
		_controlPane.setAlignment(Pos.CENTER);
		_controlPane.setPadding(new Insets(10));
		_controlPane.setSpacing(20);
		String paneStyle = "-fx-background-color:black;-fx-border-color:white;-fx-border-radius: 15;";
		String textStyle = "-fx-font-size:15;-fx-text-fill:white";
		//Hold
		VBox holdPane = new VBox();
		holdPane.setMinHeight(Constants.HOLD_SECTION_HEIGHT);
		holdPane.setPrefHeight(Constants.HOLD_SECTION_HEIGHT);
		holdPane.setStyle(paneStyle);
		holdPane.setAlignment(Pos.CENTER);
		Label hold = new Label("HOLD");
		hold.setStyle(textStyle);
		VBox holdPiece = new VBox();
		holdPiece.setMinHeight(Constants.HOLD_PANE_HEIGHT);
		holdPiece.setAlignment(Pos.CENTER);
		holdPane.getChildren().addAll(hold,holdPiece);
		//Score
		VBox numberPane = new VBox();
		numberPane.setPrefHeight(Constants.NUMBER_SECTION_HEIGHT);
		numberPane.setStyle(paneStyle);
		numberPane.setAlignment(Pos.CENTER);
		Label score = new Label("SCORE");
		Label scoreLabel = new Label("0");
		scoreLabel.setPadding(new Insets(0,0,3,0));
		Label level = new Label("LEVEL");
		Label levelLabel = new Label("1");
		levelLabel.setPadding(new Insets(0,0,3,0));
		Label lines = new Label("LINES");
		Label linesLabel = new Label("0");
		linesLabel.setPadding(new Insets(0,0,3,0));
		numberPane.getChildren().addAll(score,scoreLabel,level,levelLabel,lines,linesLabel);
		for (int i = 0; i < numberPane.getChildren().size(); i++) {
			numberPane.getChildren().get(i).setStyle(textStyle);
		}
		//Next
		VBox nextPane = new VBox();
		nextPane.setPrefHeight(Constants.NEXT_SECTION_HEIGHT);
		nextPane.setMinHeight(Constants.NEXT_SECTION_HEIGHT);
		nextPane.setStyle(paneStyle);
		nextPane.setAlignment(Pos.CENTER);
		nextPane.setSpacing(10);
		Label next = new Label("NEXT");
		next.setStyle(textStyle);
		VBox nextPieces = new VBox();
		nextPieces.setAlignment(Pos.CENTER);
		nextPieces.setPadding(new Insets(10));
		nextPieces.setSpacing(23);
		nextPane.getChildren().addAll(next, nextPieces);
		//Quit
		Button quit = new Button("Quit");
		quit.setOnAction(new QuitHandler());
		//Decorate the control section
		_controlPane.getChildren().addAll(holdPane, numberPane, nextPane, quit);
		_main.setRight(_controlPane);
	}
	
	// Pause Pane
	public void createPausePane() {
		_pausePane = new StackPane();
		_pausePane.setAlignment(Pos.CENTER);
		// Blur Effect Pane
		Pane blurEffect = new Pane();
		blurEffect.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		blurEffect.setStyle("-fx-background-color:black;-fx-opacity:0.3");
		// Actual Content
		VBox contents = new VBox();
		contents.setAlignment(Pos.CENTER);
		contents.setSpacing(30);
		// gameOver Label
		Label pause = new Label("Pause");
		pause.setStyle("-fx-font-size:40;-fx-text-fill:white");
		pause.setTextAlignment(TextAlignment.CENTER);
		// set up restart button
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(100);
		Button resume = new Button("Resume");
		resume.setOnAction(new ResumeHandler());
		Button quit = new Button("Quit");
		quit.setOnAction(new QuitHandler());
		buttons.getChildren().addAll(resume, quit);
		// add all
		contents.getChildren().addAll(pause, buttons);
		// setup the pane
		_pausePane.getChildren().addAll(blurEffect, contents);
		_root.getChildren().add(_pausePane);
	}
	
	// Game Over Pane
	public void createGameOverPane() {
		_gameOverPane = new StackPane();
		_gameOverPane.setAlignment(Pos.CENTER);
		// Blur Effect Pane
		Pane blurEffect = new Pane();
		blurEffect.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		blurEffect.setStyle("-fx-background-color:black;-fx-opacity:0.3");
		// Actual Content
		VBox contents = new VBox();
		contents.setAlignment(Pos.CENTER);
		contents.setSpacing(30);
		// gameOver Label
		Label gameOver = new Label("Game Over!");
		gameOver.setStyle("-fx-font-size:40;-fx-text-fill:white");
		gameOver.setTextAlignment(TextAlignment.CENTER);
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
				_scores[i][j].setStyle("-fx-font-size:30;-fx-text-fill:white");
				_scores[i][j].setTextAlignment(TextAlignment.CENTER);
				scoreListPane.add(_scores[i][j], j, i);
			}
		}
		// set up restart button
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(100);
		Button restart = new Button("Restart");
		restart.setOnAction(new RestartHandler());
		Button quit = new Button("Quit");
		quit.setOnAction(new QuitHandler());
		buttons.getChildren().addAll(restart, quit);
		// add all
		contents.getChildren().addAll(gameOver, scoreListPane, buttons);
		// setup the pane
		_gameOverPane.getChildren().addAll(blurEffect, contents);
		_root.getChildren().add(_gameOverPane);
	}
	
	/**
	 * Accessor methods
	 */
	public Pane getRoot() {
		return _root;
	}

	@Override
	public void setReturn(Pane menu) {
		_menu = menu;
	}

	public Pane getGamePane() {
		return _gamePane;
	}
	
	public VBox getControlPane() {
		return _controlPane;
	}
	
	public StackPane getPausePane() {
		return _pausePane;
	}
	
	public StackPane getGameOverPane() {
		return _gameOverPane;
	}
	
	public Label[][] getScoreList() {
		return _scores;
	}
	
	// Start Button setup
	private class StartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			// remove start Pane
			_root.getChildren().remove(3);
			// The Game starts!
			_manager.setupGame();
		}
	}
	
	// Resume Button setup
	private class ResumeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			// uses status mutator method
			_manager.toggleGameStatus();
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
			_gameOverPane.toBack();
			// Set up the game again. This method resets all the elements.
			_manager.setupGame();
		}	
	}
}
