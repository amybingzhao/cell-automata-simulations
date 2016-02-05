package cellsociety_team21;

import java.io.File;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author austinwu
 * This class takes care of overall logic and UI elements
 */

public class Simulation {
	public static final String TITLE = "Cell Society Simulation";
	
	// UI variables
	private Stage myStage;
	private Group myBoardGroup;
	private Text myTitleDisplay;
	private Text mySpeedDisplay;
	private int boardPixelSize;
	private int borderPixelSize = 1;
	private int cellPixelSize;
	private static final int BOARD_WIDTH_OFFSET = 50;
	private static final int BOARD_HEIGHT_OFFSET = 50;

	private boolean running;
	private int speed;
	private int tick;
	
	//xml determined variables
	private int myGridWidth;
	private int myGridHeight;
	private Rectangle[][] myBoard;
	private Grid myGrid;
	private Rules myRules;
	private String myCurrentSimulation; 
	
	/**
	 * Returns name of the game.
	 */
	public String getTitle() {
		return TITLE;
	}

	/**
	 * Create the game's scene
	 */
	public Scene init(int pixelSize, int width, int height, Stage stage) {
		myStage = stage;
		speed = 10;
		tick = 0;
		boardPixelSize = pixelSize;
		myCurrentSimulation = "NONE";
		Group root = buildUI();
		Scene myScene = new Scene(root, width, height, Color.WHITE);
		return myScene;
	}

	/**
	 * Sets up 
	 * @return returns a new group that represents the root node for the entire UI, with buttons and board background attached
	 */
	private Group buildUI(){
		Group group = new Group();
		
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.BLACK);
		boardBackground.setX(50);
		boardBackground.setY(50);
		boardBackground.setWidth(400);
		boardBackground.setHeight(400);
		group.getChildren().add(boardBackground);
		
		//set boardGroup instance variable
		myBoardGroup = new Group();
		myBoardGroup.setLayoutX(BOARD_WIDTH_OFFSET);
		myBoardGroup.setLayoutY(BOARD_HEIGHT_OFFSET);
		group.getChildren().add(myBoardGroup);
		attachButtonsToUI(group);
		attachFieldsToUI(group);

		return group;
	}
	
	/**
	 * Attach buttons to the UI display.
	 */
	private void attachButtonsToUI(Group group){
		String[] buttons = {"Start", "Stop", "Step", "Speed Up", "Slow Down", "Load XML"}; 
		HBox hbox = new HBox(2);
		for(int i = 0; i < buttons.length; i++){
			Button button = new Button(buttons[i]);
			button.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			        respondToButton(e.toString().split("'")[1]);
			    }
			});
			hbox.getChildren().add(button);
		}
		hbox.setMaxWidth(400);
		hbox.setLayoutX(50);
		hbox.setLayoutY(475);
		group.getChildren().add(hbox);
	}
	
	/**
	 * Attach title and speed display to UI.
	 * @param group
	 */
	private void attachFieldsToUI(Group group){
		HBox hbox = new HBox(2);
		myTitleDisplay = new Text();
		myTitleDisplay.setFont(new Font(15));
		myTitleDisplay.setText("Current Simulation: None");
		hbox.getChildren().add(myTitleDisplay);
		mySpeedDisplay = new Text();
		mySpeedDisplay.setFont(new Font(15));
		mySpeedDisplay.setText("    Current Speed: " + speed);
		hbox.getChildren().add(mySpeedDisplay);
		hbox.setLayoutX(50);
		hbox.setLayoutY(525);
		group.getChildren().add(hbox);
	}
	
	/**
	 * Determines response to button press based on the button.
	 */
	private void respondToButton(String button){
		switch(button) {
		case "Start":
			if(!myCurrentSimulation.equals("NONE")){
				running = true;
			}
			break;
		case "Stop":
			running = false;
			break;
		case "Step":
			if(!myCurrentSimulation.equals("NONE"))
				step(0.0, true);
			break;
		case "Speed Up":
			if(speed < 20)
				mySpeedDisplay.setText("    Current Speed: " + ++speed);
			break;
		case "Slow Down":
			if(speed > 1)
				mySpeedDisplay.setText("    Current Speed: " + --speed);
			break;
		case "Load XML":
			running = false;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Load XML File");
			File file = fileChooser.showOpenDialog(myStage);
			if(file != null){
				loadXML(file);
				buildBoard();
				displayGridToBoard();	
			}
			break;
		}	
	}
	
	
	/**
	 * Helper method that loads XML with the parser and then sets instance variables
	 */
	private void loadXML(File file){
		XMLParser parser = new XMLParser();
		parser.parse(file);
		String[][] inputgrid = parser.getGrid();
		

		for(int i = 0; i < inputgrid.length; i++){
			for(int k = 0; k < inputgrid[i].length; k++){
				if(inputgrid[i][k] == null)
					inputgrid[i][k] = "EMPTY";
			}
		}
		
		myGridWidth = inputgrid.length;
		myGridHeight = inputgrid[1].length;
		cellPixelSize = (boardPixelSize / Math.max(myGridWidth, myGridHeight)) - 2 * borderPixelSize;
		myGrid = new Grid(myGridWidth, myGridHeight, inputgrid);
		myRules = parser.getRules(); 
		myRules.initGrid(myGrid, inputgrid);
		myCurrentSimulation = myRules.toString();
		myTitleDisplay.setText("Current Simulation: " + myCurrentSimulation);
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 */
	private void buildBoard(){
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridWidth][myGridHeight];
		for (int r = 0; r < myGrid.getNumRows(); r++) {
			for (int c = 0; c < myGrid.getNumCols(); c++) {
				Rectangle rect = new Rectangle();
				//put black background and then make cells have an offset of a pixel on each side
				rect.setY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				myBoard[r][c] = rect;
				myBoardGroup.getChildren().add(rect);
			}
		}
	}
	
	/**
	 * Updates the fill of every cell in myGrid
	 */
	private void displayGridToBoard(){
		for(int r = 0; r < myGrid.getNumRows(); r++){
			for(int c = 0; c < myGrid.getNumCols(); c++){
				Color color = myRules.getFill(myGrid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(color);
			}
		}
	}
	
	/**
	 * Applyies rules to cells, updates their states, displays new states
	 */
	public void step(double elapsedTime, boolean stepping) {
		if(tick % (21 - speed) != 0 && !stepping){
			tick++;
			return;
		}
		if(running || stepping){
			applyRulesToGrid();
			updateEachState();
			displayGridToBoard();	
		}
		tick++;
	}
	
	/**
	 * Helper method that applies the specified rules to each method in the grid
	 */
	private void applyRulesToGrid(){
		for(int r = 0; r < myGrid.getNumRows(); r++){
			for(int c = 0; c < myGrid.getNumCols(); c++){
				myRules.applyRulesToCell(myGrid.getCell(r,c), myGrid);
			}
		}
	}
	
	/**
	 * Helper method that updates each state that needs to be updated
	 * Then clears the 
	 */
	private void updateEachState(){
		for(Cell c: myRules.getToBeUpdatedList()){
			c.updateState();
		}
		myRules.clearToBeUpdatedList();
	}
}