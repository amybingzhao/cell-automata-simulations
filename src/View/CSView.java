package View;

import java.io.File;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CSView {
	
	//View Objects
	private Stage myStage;
	private Group myBoardGroup;
	private Rectangle[][] myBoard;
	private Text myTitleDisplay;
	private Text mySpeedDisplay;

	//View Dimensions
	private int myGridWidth;
	private int myGridHeight;
	private int boardHeightOffset;
	private int boardWidthOffset;
	private int boardPixelSize;
	private int cellPixelSize;
		
	public static final String DEFAULT_VIEW_RESOURCE = "View/DefaultView";
	private ResourceBundle myViewResources;
	
	private Simulation mySimulation;;
	
	/**
	 * Constructor, creates a new CSView object
	 * Binds simulation to view object 
	 */
	public CSView(Simulation s) {
		mySimulation = s;
		s.setView(this);
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		loadResources(myViewResources);
	}
	
	/**
	 * Loads resources into instance variables from param r
	 * @param r
	 */
	private void loadResources(ResourceBundle r){
		boardPixelSize = Integer.parseInt(myViewResources.getString("BoardPixelSize"));
		boardWidthOffset = Integer.parseInt(myViewResources.getString("BoardWidthOffset"));
		boardHeightOffset = Integer.parseInt(myViewResources.getString("BoardHeightOffset"));
	}
	
	public Scene getScene(Stage stage){
		myStage = stage;
		Group root = buildUIRoot();
		int width = Integer.parseInt(myViewResources.getString("WindowWidth"));
		int height = Integer.parseInt(myViewResources.getString("WindowHeight"));
		Scene myScene = new Scene(root, width, height, Color.WHITE);
		return myScene;
	}
	
	/**
	 * Sets up 
	 * @return returns a new group that represents the root node for the entire UI, with buttons and board background attached
	 */
	private Group buildUIRoot(){
		Group group = new Group();
		
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.BLACK);
		boardBackground.setX(boardWidthOffset);
		boardBackground.setY(boardHeightOffset);
		boardBackground.setWidth(boardPixelSize);
		boardBackground.setHeight(boardPixelSize);
		group.getChildren().add(boardBackground);
		
		//set boardGroup instance variable
		myBoardGroup = new Group();
		myBoardGroup.setLayoutX(boardWidthOffset);
		myBoardGroup.setLayoutY(boardHeightOffset);
		group.getChildren().add(myBoardGroup);
		
		VBox vbox = new VBox(3);
		attachButtonsToVBox(vbox);
		attachFieldsToVBox(vbox);
		vbox.setLayoutX(boardWidthOffset);
		vbox.setLayoutY(boardPixelSize + boardHeightOffset + 20);
		group.getChildren().add(vbox);
		return group;
	}
	
	/**
	 * Attach buttons to the UI display.
	 */
	private void attachButtonsToVBox(VBox vbox){
		String[] firstrow = {"Start", "Stop", "Step", "Load XML"};
		String[] secondrow= {"Speed Up", "Slow Down", "Restart", "Save"};
		HBox hbox1 = new HBox(firstrow.length);
		HBox hbox2 = new HBox(secondrow.length);
		for(int i = 0; i < firstrow.length; i++){
			addButtonToHbox(firstrow[i], hbox1);
		}
		for(int i = 0; i < secondrow.length; i++){
			addButtonToHbox(secondrow[i], hbox2);
		}
		
		hbox1.setPrefWidth(boardPixelSize);
		hbox2.setPrefWidth(boardPixelSize);
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
	}
	
	private Button addButtonToHbox(String name, HBox hbox){
		Button button = new Button(name);
		button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        respondToButton(e.toString().split("'")[1]);
		    }
		});
		HBox.setHgrow(button, Priority.ALWAYS);
	    button.setMaxWidth(Double.MAX_VALUE);
		hbox.getChildren().add(button);
		return button;
	}
	
	/**
	 * Attach title and speed display to UI.
	 * @param group
	 */
	private void attachFieldsToVBox(VBox vbox){
		HBox hbox = new HBox(2);
		myTitleDisplay = new Text();
		myTitleDisplay.setText("Current Simulation: None");
		mySpeedDisplay = new Text();
		mySpeedDisplay.setText("    Current Speed: " + mySimulation.getSpeed());		
	    hbox.getChildren().add(myTitleDisplay);
		hbox.getChildren().add(mySpeedDisplay);
		vbox.getChildren().add(hbox);
	}
	
	/**
	 * Determines response to button press based on the button.
	 */
	private void respondToButton(String button){
		switch(button) {
		case "Start":
			if(mySimulation.getCurrent() != null)
				mySimulation.setRunning(true);
			break;
		case "Stop":
			mySimulation.setRunning(false);
			break;
		case "Step":
			if(mySimulation.getCurrent() != null){
				mySimulation.step(0.0, true);
				displayGridToBoard();
			}
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
			loadXMLPressed();
			break;
		case "Restart":
			restartPressed();
		case "Save":
			mySimulation.saveXML();
			break;
		}
	}
	
	/**
	 * Handles loadXML Button press
	 */
	private void loadXMLPressed(){
		mySimulation.setRunning(false);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load XML File");
		File xmlFile = fileChooser.showOpenDialog(myStage);
		if(xmlFile != null){
			mySimulation.loadXML(xmlFile);
			buildBoard();
			displayGridToBoard();	
		}
	}
	
	/**
	 * Handles restart Button press
	 */
	private void restartPressed(){
		if(mySimulation.getXML() != null){
			mySimulation.loadXML(null);
			buildBoard();
			displayGridToBoard();	
		}
	}
	
	/**
	 * Loads in the grid's width, grid's height, and current simulation name
	 * Displays the name of the simulation
	 * @param w
	 * @param h
	 * @param current
	 */
	public void setGridInfo(int w, int h, String current){
		myGridWidth = w;
		myGridHeight = h;
		int borderPixelSize = Integer.parseInt(myViewResources.getString("BorderPixelSize"));
		cellPixelSize = (boardPixelSize / Math.max(myGridWidth, myGridHeight)) - 2 * borderPixelSize;
		myTitleDisplay.setText("Current Simulation: " + current);
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 */
	private void buildBoard(){
		int borderPixelSize = Integer.parseInt(myViewResources.getString("BorderPixelSize"));
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
}


