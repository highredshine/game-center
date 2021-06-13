package io.github.juatho.Tetris;

/**
 * This is the Constants class. GUI like this uses a lot of static numbers and data.
 * Having this Constants class make the code more readable and clear.
 */
public class Constants {

	/*
	 * Display settings
	 */
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 600;
	public static final int TITLE_SIZE = 500;
	public static final int GAME_PANE_WIDTH = 400;
	public static final int CONTROL_PANE_WIDTH = 200;
	public static final int SCORE_LIST_SIZE = 400;
	public static final String IMAGE_URL = "https://s3.amazonaws.com/tetris-www/assets/editorial/2018/03/tetrislogo-1500.png";
	
	/*
	 * Board settings
	 */
	public static final int NUM_COL = 10;
	public static final int NUM_ROW = 20;
	public static final int SQUARE_SIZE = 38;
	public static final int BORDER = 2;
	// Adjusts are to make the board to be on the middle of the pane, with some spacings
	// on each border.
	public static final int COL_ADJUST = (GAME_PANE_WIDTH -  NUM_COL * (SQUARE_SIZE+BORDER/2)) / 2;
	public static final int ROW_ADJUST = (SCREEN_HEIGHT - NUM_ROW * (SQUARE_SIZE+BORDER/2)) / 2;
	public static final int BOTTOM = SCREEN_HEIGHT-SQUARE_SIZE-BORDER/2-ROW_ADJUST;
	// Buffer adjusts are used to subtract certain amount of distance while calculating
	public static final int BUFFER_ADJUST = (SQUARE_SIZE+BORDER/2);
	
	/*
	 * Piece settings: unique structure of each pieces
	 */
	public static final int[][] TETRIMINO_I = {{0,0},{2,0},{1,0},{3,0}};
	public static final int[][] TETRIMINO_J = {{0,0},{1,1},{0,1},{2,1}};
	public static final int[][] TETRIMINO_L = {{0,1},{1,1},{2,1},{2,0}};
	public static final int[][] TETRIMINO_O = {{0,0},{1,0},{0,1},{1,1}};
	public static final int[][] TETRIMINO_S = {{0,1},{1,1},{1,0},{2,0}};
	public static final int[][] TETRIMINO_T = {{1,0},{1,1},{0,1},{2,1}};
	public static final int[][] TETRIMINO_Z = {{0,0},{1,1},{1,0},{2,1}};
	
	/*
	 * Control Settings
	 */
	public static final int HOLD_SECTION_HEIGHT = 150;
	public static final int HOLD_PANE_HEIGHT = 100;
	public static final int NUMBER_SECTION_HEIGHT = 150;
	public static final int NEXT_SECTION_HEIGHT = 350;
	public static final double INCREMENT = 0.5;
	
}
