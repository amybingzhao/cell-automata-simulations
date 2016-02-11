package View;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CSView {
	
	//UI Objects
	private Stage myStage;
	private Group myBoardGroup;
	private Rectangle[][] myBoard;
	private Text myTitleDisplay;
	private Text mySpeedDisplay;
	private LineChart<Number,Number> lineChart;
	private Map<String, XYChart.Series> seriesMap;
	
	
	//UI Metrics
	private int myGridWidth;
	private int myGridHeight;
	private int boardHeightOffset;
	private int boardWidthOffset;
	private int boardPixelSize;
	private int cellPixelSize;
	private int chartHeightOffset;
	
	//resources
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
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
		chartHeightOffset = Integer.parseInt(myViewResources.getString("ChartHeightOffset"));
	}
	
	public Scene getScene(Stage stage){
		myStage = stage;
		Group root = buildUIRoot();
		int width = Integer.parseInt(myViewResources.getString("WindowWidth"));
		int height = Integer.parseInt(myViewResources.getString("WindowHeight"));
		Scene myScene = new Scene(root, width, height, Color.WHITE);
		myScene.setOnMouseClicked(e -> respondToMouse(e.getX(), e.getY()));
		return myScene;
	}
	
	/**
	 * Sets up 
	 * @return returns a new group that represents the root node for the entire UI, with buttons and board background attached
	 */
	private Group buildUIRoot(){
		Group group = new Group();
		group.getChildren().add(getBackground());
		
		//add chart
		VBox vbox1 = new VBox();
		vbox1.setPrefWidth(boardPixelSize);
		vbox1.setMaxHeight(100);
		vbox1.setLayoutX(boardWidthOffset);
		vbox1.setLayoutY(chartHeightOffset);
		buildChart(vbox1);
		
		//set boardGroup instance variable
		myBoardGroup = new Group();
		myBoardGroup.setLayoutX(boardWidthOffset);
		myBoardGroup.setLayoutY(boardHeightOffset);
		group.getChildren().add(myBoardGroup);
		
		VBox vbox2 = new VBox(3);
		attachButtonsToVBox(vbox2);
		attachFieldsToVBox(vbox2);
		vbox2.setPrefWidth(boardPixelSize);
		vbox2.setLayoutX(boardWidthOffset);
		vbox2.setLayoutY(boardPixelSize + boardHeightOffset + 20);
		group.getChildren().addAll(vbox1, vbox2);
		return group;
	}
	
	/**
	 * Generates the background rectangle for the board
	 * @return a Rectangle object
	 */
	private Rectangle getBackground(){
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.BLACK);
		boardBackground.setX(boardWidthOffset);
		boardBackground.setY(boardHeightOffset);
		boardBackground.setWidth(boardPixelSize);
		boardBackground.setHeight(boardPixelSize);
		return boardBackground;
	}
	
	/**
	 * Attach buttons to the UI display.
	 */
	private void attachButtonsToVBox(VBox vbox){
		String[] firstrow = myViewResources.getString("ButtonRowOne").split(",");
		String[] secondrow= myViewResources.getString("ButtonRowTwo").split(",");
		HBox hbox1 = new HBox(firstrow.length);
		HBox hbox2 = new HBox(secondrow.length);
		for(int i = 0; i < firstrow.length; i++){
			addButtonToHbox(firstrow[i], hbox1);
		}
		for(int i = 0; i < secondrow.length; i++){
			addButtonToHbox(secondrow[i], hbox2);
		}

		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
	}
	
	/**
	 * Adds buttons of a certain name to some hbox
	 * @param name name of button
	 * @param hbox hbox to be added to
	 */
	private void addButtonToHbox(String name, HBox hbox){
		Button button = new Button(name);
		button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        respondToButton(e.toString().split("'")[1]);
		    }
		});
		HBox.setHgrow(button, Priority.ALWAYS);
	    button.setMaxWidth(Double.MAX_VALUE);
		hbox.getChildren().add(button);
	}
	
	/**
	 * Attach title and speed display to UI.
	 * @param group
	 */
	private void attachFieldsToVBox(VBox vbox){
		HBox hbox = new HBox(2);
		myTitleDisplay = new Text();
		myTitleDisplay.setText("Current Simulation: None");
		myTitleDisplay.setFont(new Font(14));
		mySpeedDisplay = new Text();
		mySpeedDisplay.setFont(new Font(14));
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
			if(mySimulation.getRules() != null)
				mySimulation.setRunning(true);
			break;
		case "Stop":
			mySimulation.setRunning(false);
			break;
		case "Step":
			if(mySimulation.getRules() != null)
				mySimulation.step(true);
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
			if(mySimulation.getRules() != null){
				restartPressed();
			}
			break;
		case "Save":
			if(mySimulation.getRules() != null){
				mySimulation.setRunning(false);
				mySimulation.saveXML();
			}
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
			setupChart();
		}
	}
	
	/**
	 * Creates a file picker to get a file name
	 * @return returns the file 
	 */
	public File promptForFileName(){
		FileChooser myFileChooser = new FileChooser();
		FileChooser.ExtensionFilter myFilter = new FileChooser.ExtensionFilter("XML Files (.xml)", "*.xml");
		myFileChooser.getExtensionFilters().add(myFilter);
		File fileName = myFileChooser.showSaveDialog(myStage);
		return fileName;
	}
	
	/**
	 * Handles restart Button press
	 */
	private void restartPressed(){
		if(mySimulation.getXML() != null){
			mySimulation.loadXML(null);
			buildBoard();
			setupChart();
		}
	}
	
	/**
	 * Responds to mouse events at location x,y by allowing user to change states.
	 * @param x x coord of mouse event
	 * @param y y coord of mouse event
	 */
	private void respondToMouse(double x, double y){
		if(mySimulation.getRules() == null)
			return;
		for(int r = 0; r < myBoard.length; r++){
			for(int c = 0; c < myBoard[r].length; c++){
				if(myBoard[r][c].contains(x - boardWidthOffset, y - boardHeightOffset)){
					mySimulation.setRunning(false);
					String current = mySimulation.getGrid().getCell(r, c).getCurState();
					List<String> choices = new ArrayList<String>();
					choices.addAll(mySimulation.getRules().getMyStatesCount().keySet());
					ChoiceDialog<String> dialog = new ChoiceDialog<>(current , choices);
					dialog.setTitle("State Picker");
					dialog.setHeaderText("Please pick a state to set cell: (" + r + "," + c + ") to");
					dialog.setContentText("Choose a state:");
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()){
					    mySimulation.getGrid().getCell(r, c).setCurState(result.get());
					    mySimulation.getRules().applyRulesToCell(mySimulation.getGrid().getCell(r, c), mySimulation.getGrid());
					}
					updateUI();
				}
			}
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
		displayGridToBoard();
	}
	
	/**
	 * Updates the board and the chart 
	 */
	public void updateUI(){
		displayGridToBoard();
		updateChart();
	}
	
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
	private void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Color color = mySimulation.getRules().getFill(grid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(color);
			}
		}
	}
	
	/**
	 * Adds a linechart graph to the specified vbox
	 * @param vbox
	 */
	private void buildChart(VBox vbox){
		NumberAxis xAxis = new NumberAxis();
	    NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("Seconds");
	    //creating the chart
	    lineChart = new LineChart<Number,Number>(xAxis,yAxis);
	    lineChart.setCreateSymbols(false);
	    lineChart.setLegendSide(Side.RIGHT);
	    lineChart.setTitle("Cells");
	    vbox.getChildren().add(lineChart);
	}
	
	/**
	 * Clears the old chart
	 * adds series to chart based on current series
	 * Loads initial data into chart
	 */
	private void setupChart(){
		lineChart.getData().clear();
	    seriesMap = new HashMap<String, XYChart.Series>();
	    Map<String, Integer> statesCount = mySimulation.getRules().getMyStatesCount();
	    //define series for each type of cell
	    for(String key : statesCount.keySet()){
	    	XYChart.Series series = new XYChart.Series();
	 	    series.setName(key);
	 	    seriesMap.put(key, series);
	 	    lineChart.getData().add(series);
	 	    series.getData().add(new XYChart.Data(0, statesCount.get(key)));
	    }
	}
	
	/**
	 * updates the chart in time increments of 100 ms
	 */
	private void updateChart(){
		Map<String, Integer> statesCount = mySimulation.getRules().getMyStatesCount();		 
		for(String key : statesCount.keySet()){
	 	    seriesMap.get(key).getData().add(new XYChart.Data(mySimulation.getTime()/100, statesCount.get(key)));
	    }
	}
}


