package View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	private Map<String, XYChart.Series<Number, Number>> seriesMap;
	private Map<String, Color> stateColorMap;
	private Color borderColor;
	
	//UI Metrics
	private int boardPixelSize;
	private int cellPixelSize;
	private int borderPixelSize;
	private int maxCellsDisplayed;
	private int uiWidth;
	
	//resources
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	private ResourceBundle myViewResources;
	

	private Simulation mySimulation;
	
	/**
	 * Constructor, creates a new CSView object
	 * Binds simulation to view object 
	 */
	public CSView(Simulation s) {
		mySimulation = s;
		s.setView(this);
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		loadResources(myViewResources);
		borderColor = Color.BLACK;
	}
	
	/**
	 * Loads resources into instance variables from param r
	 * @param r
	 */
	private void loadResources(ResourceBundle r){
		boardPixelSize = Integer.parseInt(myViewResources.getString("BoardPixelSize"));
		borderPixelSize = Integer.parseInt(myViewResources.getString("DefaultBorderPixelSize"));
		maxCellsDisplayed = Integer.parseInt(myViewResources.getString("MaxCellsDisplayed"));
		uiWidth= Integer.parseInt(myViewResources.getString("UIWidth"));
	}
	
	public Scene getScene(Stage stage){
		myStage = stage;
		Group root = buildUI();
		int width = Integer.parseInt(myViewResources.getString("WindowWidth"));
		int height = Integer.parseInt(myViewResources.getString("WindowHeight"));
		Scene myScene = new Scene(root, width, height, Color.WHITE);
		return myScene;
	}
	
	/**
	 * Sets up 
	 * @return returns a new group that represents the root node for the entire UI, with buttons and board background attached
	 * Keep convention of setting max height/widths on VBox/Hboxes and then adding to overall vbox
	 */
	private Group buildUI(){
		Group group = new Group();
		
		VBox vbox = new VBox(4);
		vbox.setPrefWidth(Integer.parseInt(myViewResources.getString("WindowWidth")));
		vbox.setAlignment(Pos.CENTER);
		
		//add chart
		VBox chartVBox = new VBox();
		chartVBox.setMaxWidth(uiWidth);
		chartVBox.setMaxHeight(100);
		buildChart(chartVBox);
		
		//add board
		HBox spHBox = new HBox();
		myBoardGroup = new Group();
		myBoardGroup.getChildren().add(getBackground());
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(myBoardGroup);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		spHBox.getChildren().add(sp);
		spHBox.setAlignment(Pos.CENTER);
		spHBox.setMaxSize(uiWidth, uiWidth);
		
		VBox buttonsVBox = new VBox(2);
		buttonsVBox.setMaxWidth(uiWidth);
		attachButtonsToVBox(buttonsVBox);
		
		VBox fieldsVBox = new VBox();
		fieldsVBox.setMaxWidth(uiWidth);
		attachFieldsToVBox(fieldsVBox);
		
		vbox.getChildren().addAll(chartVBox, spHBox, buttonsVBox, fieldsVBox);
		group.getChildren().add(vbox);
		return group;
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
	 * Generates the background rectangle for the board
	 * @return a Rectangle object
	 */
	private Rectangle getBackground(){
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.WHITE);
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
	 * Sets up the board and the chart
	 */
	private void setupUI(){
		setupAndDisplayBoard();
		setupChart();
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 */
	private void setupAndDisplayBoard(){
		int myGridWidth = mySimulation.getGrid().getNumCols();
		int myGridHeight = mySimulation.getGrid().getNumRows();
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridWidth][myGridHeight];
		Grid grid = mySimulation.getGrid();
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = new Rectangle();
				bg.setLayoutY(r * (cellPixelSize + (2 * borderPixelSize)));
				bg.setLayoutX(c * (cellPixelSize + (2 * borderPixelSize)));
				bg.setWidth(cellPixelSize + (2 * borderPixelSize));
				bg.setHeight(cellPixelSize + (2 * borderPixelSize));
				bg.setFill(borderColor);
				
				Rectangle rect = new Rectangle();
				rect.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				int x = r; //java 8 MADE me do it Professor!
				int y = c; //"effectively final" and whatnot.
				rect.setOnMouseClicked(e -> respondToMouse(x, y)); 
				myBoard[r][c] = rect;
				myBoardGroup.getChildren().addAll(bg,rect);
			}
		}
		displayGridToBoard();
	}
	
	/**
	 * Clears the old chart
	 * adds series to chart based on current series
	 * Loads initial data into chart
	 */
	private void setupChart(){
		lineChart.getData().clear();
	    seriesMap = new HashMap<String, XYChart.Series<Number, Number>>();
	    Map<String, Integer> statesCount = mySimulation.getRules().getMyStatesCount();
	    //define series for each type of cell
	    for(String key : statesCount.keySet()){
	    	XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
	 	    series.setName(key);
	 	    seriesMap.put(key, series);
	 	    lineChart.getData().add(series);
	 	    series.getData().add(new XYChart.Data<Number, Number>(0, statesCount.get(key)));
	    }
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
		case "Config":
			if(mySimulation.getRules() != null){
				createConfigPanel();
			}
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
			stateColorMap = new HashMap<String, Color>(mySimulation.getRules().getMyStatesColors());
			myTitleDisplay.setText("Current Simulation: " + mySimulation.getRules().toString());
			setupUI();
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
			mySimulation.setRunning(false);
			mySimulation.loadXML(null);
			setupUI();
		}
	}
	
	/**
	 * Responds to mouse events on a given cell
	 */
	private void respondToMouse(int r, int c){
		mySimulation.setRunning(false);
		String current = mySimulation.getGrid().getCell(r, c).getCurState();
		createStateChanger(current, r, c);
		updateUI();
	}
	
	/**
	 * Takes in a currents state string and the location of a grid 
	 * and creates a dialog for the user to change the state
	 * @param current String, current state of the cel 
	 * @param r int, row
	 * @param c int, col
	 */
	private void createStateChanger(String current, int r, int c){
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(current, stateColorMap.keySet());
		dialog.setTitle("State Picker");
		dialog.setHeaderText("Please choose a state to set cell: (" + r + "," + c + ") to:");
		dialog.setContentText("Choose a state:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			mySimulation.getRules().decreaseStateCount(mySimulation.getGrid().getCell(r, c).getCurState());
		    mySimulation.getGrid().getCell(r, c).setCurState(result.get());
		    mySimulation.getRules().increaseStateCount(result.get());
		}
	}
	
	/**
	 * Crates a config dialog box to select border size, state colors, etc.
	 */
	private void createConfigPanel(){
		List<String> configOptions = Arrays.asList(myViewResources.getString("ConfigOptions").split(","));
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(configOptions.get(0),configOptions);
		dialog.setTitle("Config Picker");
		dialog.setHeaderText("Please pick a setting to change:");
		dialog.setContentText("Choose a setting:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			handleConfigOpen(result.get());
		}
	}
	
	/**
	 * Handle different kind of config changes
	 * @param config config to change
	 */
	private void handleConfigOpen(String config){
		switch(config){
		case "State Color":
			openStateColorConfig();
			break;
		case "Border Thickness":
			openBorderThicknessConfig();
		case "Border Color":
			openBorderColorConfig();
		}
	}
	
	/**
	 * Allows the user to pick a state to change color for
	 */
	private void openStateColorConfig(){
		List<String> states = new ArrayList<String>(stateColorMap.keySet());
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(states.get(0), states);
		dialog.setTitle("State Color Picker");
		dialog.setHeaderText("Pick a state to change the color of:");
		dialog.setContentText("Pick a state:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			final ColorPicker colorPicker = new ColorPicker(stateColorMap.get(result.get()));
			myBoardGroup.getChildren().add(colorPicker);
	        colorPicker.setOnAction(new EventHandler() {
	            public void handle(Event t) { 
	                stateColorMap.put(result.get(), colorPicker.getValue());
	                myBoardGroup.getChildren().remove(colorPicker);
	        		updateUI();
	            }
	        });
		}
	}
	
	/**
	 * Opens border thickness config dialog 
	 */
	private void openBorderThicknessConfig(){
		List<Integer> borderThicknesses = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++){
			borderThicknesses.add(i);
		}
		ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(0, borderThicknesses);
		dialog.setTitle("Border Thickness Picker");
		dialog.setHeaderText("Border Thickness Picker");
		dialog.setContentText("Border thickness (pixels):");
		Optional<Integer> result = dialog.showAndWait();
		result.ifPresent(name -> borderPixelSize = result.get());
		setupAndDisplayBoard();
	}
	
	/**
	 * Opens a dialog to change border color
	 */
	private void openBorderColorConfig(){
		final ColorPicker colorPicker = new ColorPicker(borderColor);
		myBoardGroup.getChildren().add(colorPicker);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) { 
                borderColor = colorPicker.getValue();
                myBoardGroup.getChildren().remove(colorPicker);
        		setupAndDisplayBoard();
            }
        });
	}
	
	/**
	 * Updates the board and the chart 
	 */
	public void updateUI(){
		setupAndDisplayBoard();
		updateChart();
	}
	
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
	private void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				myBoard[r][c].setFill(stateColorMap.get(grid.getCell(r,c).getCurState()));
			}
		}
	}
	
	
	/**
	 * updates the chart in time increments of 100 ms
	 */
	private void updateChart(){
		Map<String, Integer> statesCount = mySimulation.getRules().getMyStatesCount();		 
		for(String key : statesCount.keySet()){
	 	    seriesMap.get(key).getData().add(new XYChart.Data<Number, Number>(mySimulation.getTime()/100, statesCount.get(key)));
	    }
	}
}


