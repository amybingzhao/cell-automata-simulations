package cellsociety_team21;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Todos:
 * Load UI basics
 * Wait for xml press
 * On XML Load press-> Load xml, create grid object, return to simulation
 * Simulation calls grid.render(), gets rectangles to for UI
 * rerenders grid every second in step
 * illustrate it in the UI 
 * @author austinwu
 *
 *
 *TODOS:
 *Implement states- values and associated colors
 *Fill grid with cells full of states in String form
 *Display grid by pulling colors from rates
 */

public class Simulation {
	public String TITLE;
	
	private Group boardGroup;
	private Rectangle[][] myBoard;
	private Grid myGrid;
	
	private int boardPixelSize;
	private int borderPixelSize = 1;
	private int cellPixelSize;
	private static final int BOARDWIDTHOFFSET = 50;
	private static final int BOARDHEIGHTOFFSET = 50;
	
	private int gridwidth;
	private int gridheight;
	
	private Rules rules;

	private boolean running;
	private int speed;
	

	public Simulation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns name of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	/**
	 * Create the game's scene
	 */
	public Scene init(int ps, int ww, int wh) {
		Group root = buildUI();
		boardPixelSize = ps;
		Scene myScene = new Scene(root, ww, wh, Color.WHITE);
		return myScene;
	}

	/**
	 * Displays game, animates blocks
	 */
	public void step(double elapsedTime) {
		if(running){
			myGrid.updateEachState();
			displayGridToBoard();	
		}
	}
	
	/**
	 * Sets up 
	 * @return returns a new group that represents the root node for the entire UI, with buttons and board attached
	 */
	public Group buildUI(){
		Group group = new Group();
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.BLACK);
		boardBackground.setX(50);
		boardBackground.setY(50);
		boardBackground.setWidth(400);
		boardBackground.setHeight(400);
		group.getChildren().add(boardBackground);
		boardGroup = new Group();
		boardGroup.setLayoutX(BOARDWIDTHOFFSET);
		boardGroup.setLayoutY(BOARDHEIGHTOFFSET);
		group.getChildren().add(boardGroup);
		attachButtonsToUI(group);
		return group;
	}
	
	public void attachButtonsToUI(Group group){
		String[] buttons = {"Start", "Stop", "Step", "Speed Up", "Slow Down", "Load XML"}; 
		HBox hbox = new HBox();
		for(int i = 0; i < buttons.length; i++){
			Button button = new Button(buttons[i]);
			button.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			        respondToButton(e.toString().split("'")[1]);
			    }
			});
			hbox.getChildren().add(button);
		}
		hbox.setLayoutX(50);
		hbox.setLayoutY(500);
		group.getChildren().add(hbox);
	}
	
	public void displayGridToBoard(){
		for(int r = 0; r < myGrid.getNumRows(); r++){
			for(int c = 0; c < myGrid.getNumCols(); c++){
				Color color = rules.getFill(myGrid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(color);
			}
		}
	}
	
	private void respondToButton(String s){
		switch(s) {
		case "Start":
			break;
		case "Stop":
			break;
		case "Step":
			break;
		case "Speed Up":
			break;
		case "Slow Down":
			break;
		case "Load XML":
			gridwidth = 10;
			gridheight = 10;

			cellPixelSize = (boardPixelSize / Math.max(gridwidth, gridheight)) - 2 * borderPixelSize;
			
			String[][] tempArray = new String[gridwidth][gridheight];
			for(int r = 0; r < tempArray.length; r++){
				for(int c = 0; c < tempArray[r].length; c++){
					tempArray[r][c] = "RED";
				}
			}
			
			myGrid = new Grid(gridwidth, gridheight, tempArray);
			rules = new SegregationRules(.5);
			/*
			 * Here is where you would load in the XML, which would generate a grid and set myGrid equal to it, 
			 * as well as set the grid width and grid height (in this case the same number, but in the future
			 * will be different numbers).
			 * 
			 * Things to set:
			 * gridwidth
			 * gridheight
			 * myGrid
			 * rules
			 * 
			 */
			fillBoardGroup();
			displayGridToBoard();	
			break;
		}		
	}
	
	public void fillBoardGroup(){
		myBoard = new Rectangle[gridwidth][gridheight];
		for (int r = 0; r < myGrid.getNumRows(); r++) {
			for (int c = 0; c < myGrid.getNumCols(); c++) {
				Rectangle rect = new Rectangle();
				//put black background and then make cells have an offset of a pixel on each side
				rect.setY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				myBoard[r][c] = rect;
				boardGroup.getChildren().add(rect);
			}
		}
	}
}
//	
//	/**
//	 * Displays the welcome screen
//	 */
//	public void displayWelcome(){
//		root.getChildren().clear();
//		Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/welcome.png"));
//		ImageView imgv = new ImageView(image);
//		root.getChildren().add(imgv);
//	}
//	
//	/**
//	 * Creates maze, generates game board
//	 */
//	public void displayGame(String m){
//		root.getChildren().clear();
//		maze = createMaze(m);
//		// Generate maze nodes
//		int cellsize = windowsize / gridsize;
//		for (int r = 0; r < maze.getNumRows(); r++) {
//			for (int c = 0; c < maze.getNumCols(); c++) {
//				Cell cell = maze.get(r, c);
//				Rectangle rect = new Rectangle();
//				rect.setY(r * cellsize);
//				rect.setX(c * cellsize);
//				rect.setWidth(cellsize);
//				rect.setHeight(cellsize);
//				rect.setFill(cell.getColor());
//				board[r][c] = rect;
//				root.getChildren().add(rect);
//			}
//		}
//	}
//	
//	/**
//	 * generates maze by filling grid, opening path, adding avatar
//	 * @param gsize the size of the maze
//	 * @return returns a new maze
//	 */
//	private Maze createMaze(String mazeinput) {
//		Maze tempmaze = new Maze(gridsize, gridsize);
//		// fills maze with blocked cells
//		for (int r = 0; r < tempmaze.getNumRows(); r++) {
//			for (int c = 0; c < tempmaze.getNumCols(); c++) {
//				tempmaze.putNew(r, c, 'X');
//			}
//		}
//		
//		austin = null;
//		player = null;
//		lookalike = null;
//		
//		// adds other cells
//		String[] fill = mazeinput.split(",");
//		
//		for(String s: fill)
//			fillMaze(s, tempmaze);
//		
//		return tempmaze;
//	}
//	
//	/**
//	 * Takes a three char string that is used as a "maze fill" element
//	 * @param input the maze fill element
//	 * @param m the maze to insert the elements into
//	 */
//	public void fillMaze(String input, Maze m){
//		if(!hardmode && input.charAt(2) == 'L'){
//			return;
//		}
//		Cell temp = null;
//		if(input.charAt(0) == 'R'){
//			for (int r = 0; r < m.getNumRows(); r++) {
//				m.putNew(r, Character.getNumericValue(input.charAt(1)), input.charAt(2));
//			}
//		} else if (input.charAt(1) == 'C'){
//			for (int c = 0; c < m.getNumRows(); c++) {
//				m.putNew(Character.getNumericValue(input.charAt(0)), c, input.charAt(2));
//			}
//		} else {
//			temp = m.putNew(Character.getNumericValue(input.charAt(0)), Character.getNumericValue(input.charAt(1)), input.charAt(2));
//		}
//		switch(input.charAt(2)) {
//		case 'A':
//			austin = temp;
//			break;
//		case 'P':
//			player = temp;
//			break;
//		case 'L':
//			lookalike = temp;
//			break;
//		default:
//			//do nothing
//		}
//	}
//	
//	/**
//	 * Sets the display to the "win" scenario.
//	 */
//	public void displayWin(){
//		root.getChildren().clear();
//		Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/you-win.jpg"));
//		ImageView imgv = new ImageView(image);
//		root.getChildren().add(imgv);
//	}
//	
//	/**
//	 * Function to automove a cell
//	 * @param cell
//	 */
//	
//	public void drawMaze(){
//		for (int r = 0; r < maze.getNumRows(); r++) {
//			for (int c = 0; c < maze.getNumCols(); c++) {
//				Cell cell = maze.get(r, c);
//				board[r][c].setFill(cell.getColor());
//			}
//		}
//	}
//	 
//	
//}
