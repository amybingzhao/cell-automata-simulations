/**
 * @author Autin Wu
 * This class takes care of overall logic and UI elements
 */

package cellsociety_team21;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private File xmlFile;
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
		String[] firstrow = {"Start", "Stop", "Step", "Load XML"};
		String[] secondrow= {"Speed Up", "Slow Down", "Restart"};
		HBox hbox1 = new HBox(firstrow.length);
		HBox hbox2 = new HBox(secondrow.length);
		for(int i = 0; i < firstrow.length; i++){
			addButtonToHbox(firstrow[i], hbox1);
		}
		for(int i = 0; i < secondrow.length; i++){
			addButtonToHbox(secondrow[i], hbox2);
		}
		hbox1.setMaxWidth(400);
		hbox1.setLayoutX(50);
		hbox1.setLayoutY(460);
		hbox2.setMaxWidth(400);
		hbox2.setLayoutX(50);
		hbox2.setLayoutY(490);
		
		group.getChildren().add(hbox1);
		group.getChildren().add(hbox2);
	}
	
	private Button addButtonToHbox(String name, HBox hbox){
		Button button = new Button(name);
		button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        respondToButton(e.toString().split("'")[1]);
		    }
		});
		hbox.getChildren().add(button);
		return button;
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
		hbox.setLayoutY(520);
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
			xmlFile = fileChooser.showOpenDialog(myStage);
			if(xmlFile != null){
				loadXML(xmlFile);
				buildBoard();
				displayGridToBoard();	
			}
			break;
		case "Restart":
			if(xmlFile != null){
				loadXML(xmlFile);
				buildBoard();
				displayGridToBoard();	
			}
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