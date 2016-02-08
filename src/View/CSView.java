package View;

import java.io.File;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import Rules.Rules;
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

public class CSView {
	
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
	private int tick;
	
	//xml determined variables
	private int myGridWidth;
	private int myGridHeight;
	private Rectangle[][] myBoard;
	private String myCurrentSimulation; 
		
	public static final String DEFAULT_VIEW_RESOURCE = "View/DefaultView";
	private ResourceBundle myViewResources;
	
	private Simulation mySimulation;;
		
	/**
	 * CellSocietyView class is responsible for:
	 * 1. Holding references to model object(s)
	 * 2. Generating UI, returning the root node of the UI
	 * 3. Keeping a reference to the "board" node of the UI
	 * 4. Receiving button/mouse event but handling them with Controller method calls
	 * 5. 
	 */
	
	
	
	/**
	 * Constructor, creates a new 
	 */
	public CSView(Simulation s) {
		mySimulation = s;
		s.setView(this);
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
	}
	/*
	 * Main takes care of: creating model, creating display, 
	 * Stage.setScene(display)
	 * stage.show();
	 * THATS IT.
	 */
	
	public Scene getScene(Stage stage){
		myStage = stage;
		tick = 0;
		boardPixelSize = Integer.parseInt(myViewResources.getString("BoardSize")); 
		myCurrentSimulation = "NONE";
		Group root = buildUI();
		int width = Integer.parseInt(myViewResources.getString("WindowWidth"));
		int height = Integer.parseInt(myViewResources.getString("WindowHeight"));
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
		mySpeedDisplay.setText("    Current Speed: " + mySimulation.getSpeed());
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
			if(!mySimulation.getCurrent().equals("NONE")){
				mySimulation.setRunning(true);
			}
			break;
		case "Stop":
			mySimulation.setRunning(false);
			break;
		case "Step":
			if(!mySimulation.getCurrent().equals("NONE"))
				mySimulation.step(0.0, true);
				displayGridToBoard();
			break;
		case "Speed Up":
			if(mySimulation.getSpeed() < 20)
				mySpeedDisplay.setText("    Current Speed: " + mySimulation.changeSpeed(1));
			break;
		case "Slow Down":
			if(mySimulation.getSpeed() > 1)
				mySpeedDisplay.setText("    Current Speed: " + mySimulation.changeSpeed(-1));
			break;
		case "Load XML":
			running = false;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Load XML File");
			File xmlFile = fileChooser.showOpenDialog(myStage);
			if(xmlFile != null){
				mySimulation.loadXML(xmlFile);
				buildBoard();
				displayGridToBoard();	
			}
			break;
		case "Restart":
			if(mySimulation.getXML() != null){
				mySimulation.loadXML(null);
				buildBoard();
				displayGridToBoard();	
			}
		}	
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 */
	private void buildBoard(){
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridWidth][myGridHeight];
		Grid grid = mySimulation.getGrid();
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
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
	 * Displays the grid to the board based on the state of each of its cells
	 */
	public void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Color color = mySimulation.getRules().getFill(grid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(color);
			}
		}
	}
	
	public void setGridDimensions(int w, int h, String current){
		myGridWidth = w;
		myGridHeight = h;
		cellPixelSize = (boardPixelSize / Math.max(myGridWidth, myGridHeight)) - 2 * borderPixelSize;
		myTitleDisplay.setText("Current Simulation: " + current);
	}
	
	/*
	 * When a button is pressed, event handler calls internal method,
	 * internal method updates model and then calls updates View
	 */
}


