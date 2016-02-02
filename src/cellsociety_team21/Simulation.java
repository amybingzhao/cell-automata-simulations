package cellsociety_team21;

import java.io.File;
import java.util.Arrays;

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

/**
 * @author austinwu
 * This class takes care of overall logic and UI elements
 */

public class Simulation {
	public static final String TITLE = "Cell Society Simulation";
	
	// UI variables
	private Group boardGroup;
	private Text titleDisplay;
	private int boardPixelSize;
	private int borderPixelSize = 1;
	private int cellPixelSize;
	private static final int BOARDWIDTHOFFSET = 50;
	private static final int BOARDHEIGHTOFFSET = 50;
	
	//xml determined variables
	private int gridwidth;
	private int gridheight;
	private Rectangle[][] myBoard;
	private Grid myGrid;
	private Rules rules;
	private String currentSimulation; 
	
	private boolean running;
	private int speed;
	
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
		currentSimulation = "NONE";
		Scene myScene = new Scene(root, ww, wh, Color.WHITE);
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
		boardGroup = new Group();
		boardGroup.setLayoutX(BOARDWIDTHOFFSET);
		boardGroup.setLayoutY(BOARDHEIGHTOFFSET);
		group.getChildren().add(boardGroup);
		attachButtonsToUI(group);
		attachFieldsToUI(group);
		/*
		 * A text field for which config file, and a button to select files
		 */
		return group;
	}
	
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
	
	private void attachFieldsToUI(Group group){
		HBox hbox = new HBox();
		titleDisplay = new Text();
		titleDisplay.setFont(new Font(15));
		titleDisplay.setText("Current Simulation: None");
		hbox.getChildren().add(titleDisplay);
		hbox.setLayoutX(50);
		hbox.setLayoutY(525);
		group.getChildren().add(hbox);
	}
	
	private void respondToButton(String s){
		switch(s) {
		case "Start":
			if(!currentSimulation.equals("NONE")){
				running = true;
			}
			break;
		case "Stop":
			running = false;
			break;
		case "Step":
			if(!currentSimulation.equals("NONE")){
				step(0.0, true);
			}
			break;
		case "Speed Up":
			break;
		case "Slow Down":
			break;
		case "Load XML":
			loadXML();
			buildBoard();
			displayGridToBoard();	
			break;
		}		
	}
	
	/**
	 * Helper method that loads XML with the parser and then sets instance variables
	 */
	private void loadXML(){
		XMLParser parser = new XMLParser();
		parser.parse(new File("data/simulation.txt"));
		String[][] inputgrid = parser.getGrid();
		

		for(int i = 0; i < inputgrid.length; i++){
			for(int k = 0; k < inputgrid[i].length; k++){
				if(inputgrid[i][k] == null)
					inputgrid[i][k] = "EMPTY";
			}
		}
		
		gridwidth = inputgrid.length;
		gridheight = inputgrid[1].length;
		cellPixelSize = (boardPixelSize / Math.max(gridwidth, gridheight)) - 2 * borderPixelSize;
		myGrid = new Grid(gridwidth, gridheight, inputgrid);
		rules = parser.getRules(); 
		currentSimulation = rules.toString();
		titleDisplay.setText("Current Simulation: " + currentSimulation);
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 */
	private void buildBoard(){
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
	
	/**
	 * Updates the fill of every cell in myGrid
	 */
	private void displayGridToBoard(){
		for(int r = 0; r < myGrid.getNumRows(); r++){
			for(int c = 0; c < myGrid.getNumCols(); c++){
				Color color = rules.getFill(myGrid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(color);
			}
		}
	}
	
	/**
	 * Applys rules to cells, updates their states, displays new states
	 */
	public void step(double elapsedTime, boolean stepping) {
		if(running || stepping){
//			System.out.println("STEPPING");
//			apply rules to all cells, update state
//			applyRulesToGrid();

			//temporary "state updating code" for demo purpose, DOES NOT FOLLOW RULE
			for(int r = 0; r < myGrid.getNumRows(); r++){
				for(int c = 0; c < myGrid.getNumCols(); c++){
					switch(myGrid.getCell(r,c).getCurState()){
					case "EMPTY":
						myGrid.getCell(r,c).setNextState("TREE");
						break;
					case "TREE":
						myGrid.getCell(r,c).setNextState("BURNING");
						break;
					case "BURNING":
						myGrid.getCell(r,c).setNextState("EMPTY");
						break;
					default:
						
					}
				}
			}
			
			myGrid.updateEachState();
			displayGridToBoard();	
		}
	}
	
	/**
	 * Helper method that applies the specified rules to each method in the grid
	 */
	private void applyRulesToGrid(){
		for(int r = 0; r < myGrid.getNumRows(); r++){
			for(int c = 0; c < myGrid.getNumCols(); c++){
				rules.applyRulesToCell(myGrid.getCell(r,c), myGrid);
			}
		}
	}
	
	
	
	/*
	 * General flow:
	 * 1. UI, board background loads
	 * 2. Load XML is pressed, xml loads, board is populated with correctly quantity of correctly sized cells, show initial states
	 * 3. Start is pressed, stepping begins
	 * 4. For each step, apply rules, update state, display grid to board, repeat
	 * 5. End is pressed, stepping stops.
	 */
}