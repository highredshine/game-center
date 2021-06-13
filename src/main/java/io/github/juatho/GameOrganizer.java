package io.github.juatho;

import io.github.juatho.DoodleJump.DoodleJump;
import io.github.juatho.Tetris.Tetris;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 
 */
public class GameOrganizer {

	// Games
	Tetris _tetris;
	DoodleJump _doodleJump;

	private final TilePane _main;
	private final StackPane _root;
	
	public GameOrganizer() {
		_tetris = new Tetris();
		_doodleJump = new DoodleJump();


		_root = new StackPane();
		_root.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

		_main = new TilePane();
		_root.getChildren().addAll(_main, _tetris.getRoot(), _doodleJump.getRoot());
		_main.toFront();
		_tetris.setReturn(_main);
		_doodleJump.setReturn(_main);

		this.setup();
	}

	private void setup() {
		Button tetrisButton = this.createGameButton("Tetris",
				"src/main/resources/images/tetris_logo.png",
				new GameStarter(_tetris));
		Button doodleJumpButton = this.createGameButton("DoodleJump",
				"src/main/resources/images/doodlejump_logo.jpg",
				new GameStarter(_doodleJump));

		_main.getChildren().addAll(tetrisButton, doodleJumpButton);
	}

	private Button createGameButton(String text, String path, EventHandler<ActionEvent> handler) {
		Button button = new Button();
		try {
			ImageView imageView = new ImageView(new Image(new FileInputStream(path)));
			imageView.setFitWidth(150);
			imageView.setFitHeight(100);
			button.setGraphic(imageView);
		} catch (FileNotFoundException e) {
			button.setText(text);
		}
		button.setPrefWidth(150);
		button.setPrefHeight(100);
		button.setOnAction(handler);
		return button;
	}

	public Pane getRoot() {
		return _root;
	}
	
	//
	private static class GameStarter implements EventHandler<ActionEvent> {

		Game _game;

		public GameStarter(Game game) {
			_game = game;
		}

		@Override
		public void handle(ActionEvent e) {
			_game.getRoot().toFront();
		}
	}

	private static class QuitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			System.exit(0);
		}	
	}

}
