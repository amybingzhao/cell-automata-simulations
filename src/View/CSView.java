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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private Text myTitleDisplay;
	private Text mySpeedDisplay;
	private LineChart<Number,Number> lineChart;
	private Map<String, XYChart.Series<Number, Number>> seriesMap;
	private Map<String, Button> buttonMap;
	private Map<String, Color> stateColorMap;
	private Color borderColor;
	
	//UI Metrics
	private int uiWidth;
	
	//resources
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	private ResourceBundle myViewResources;

	private Simulation mySimulation;
	private BoardBuilder myBB;
	/**
	 * Constructor, creates a new CSView object
	 * Binds simulation to view object 
	 */
	public CSView(Simulation s) {
		mySimulation = s;
		mySimulation.setView(this);
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		loadResources(myViewResources);
		borderColor = Color.BLACK;
		buttonMap = new HashMap<String, Button>();
		myBB = new BoardBuilder(this, mySimulation);
	}
	
	/**
	 * Loads resources into instance variables from param r
	 * @param r
	 */
	private void loadResources(ResourceBundle r){
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
		
		VBox vbox = new VBox(5);
		vbox.setPrefWidth(Integer.parseInt(myViewResources.getString("WindowWidth")));
		vbox.setAlignment(Pos.CENTER);
		
		//add chart
		VBox chartVBox = new VBox();
		chartVBox.setMaxWidth(uiWidth);
		chartVBox.setMaxHeight(100);
		buildChart(chartVBox);
		
		//add board
		HBox spHBox = new HBox(5);
		myBoardGroup = new Group();
		myBoardGroup.getChildren().add(myBB.getBackground());
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(myBoardGroup);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		spHBox.getChildren().add(sp);
		spHBox.setAlignment(Pos.CENTER);
		spHBox.setMaxSize(uiWidth, uiWidth);
		
		VBox buttonsVBox = new VBox(5);
		buttonsVBox.setMaxWidth(uiWidth);
		attachButtonsToVBox(buttonsVBox);
		enableButtons();
		
		VBox fieldsVBox = new VBox(5);
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
	 * Attach buttons to the UI display.
	 */
	private void attachButtonsToVBox(VBox vbox){
		String[] firstrow = myViewResources.getString("ButtonRowOne").split(",");
		String[] secondrow= myViewResources.getString("ButtonRowTwo").split(",");
		HBox hbox1 = new HBox(5);
		HBox hbox2 = new HBox(5);
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
	protected void addButtonToHbox(String name, HBox hbox){
		Button button = new Button(name);
		buttonMap.put(name, button);
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
		HBox hbox = new HBox(20);
		myTitleDisplay = new Text();
		myTitleDisplay.setText("Current Simulation: None");
		myTitleDisplay.setFont(new Font(14));
		mySpeedDisplay = new Text();
		mySpeedDisplay.setFont(new Font(14));
		mySpeedDisplay.setText("Current Speed: " + mySimulation.getSpeed());
	    hbox.getChildren().add(myTitleDisplay);
		hbox.getChildren().add(mySpeedDisplay);
		vbox.getChildren().add(hbox);
	}
	
	/**
	 * Sets up the board and the chart
	 */
	private void setupUI(){
		myBB.displayBoard(myBoardGroup);
		setupChart();
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
		case "Start": mySimulation.setRunning(true); break;
		case "Stop": mySimulation.setRunning(false); break;
		case "Step": mySimulation.step(true); break;
		case "Speed Up":
			mySpeedDisplay.setText("Current Speed: " + mySimulation.changeSpeed(1)); break;
		case "Slow Down":
			mySpeedDisplay.setText("Current Speed: " + mySimulation.changeSpeed(-1)); break;
		case "Generate XML": generateXML(); break;
		case "Load XML": loadXMLPressed(); break;
		case "Reset": resetPressed(); break;
		case "Save": mySimulation.saveXML(); break;
		case "Config": createConfigPanel();break;
		}
		enableButtons();
	}
	
	/**
	 * Enables and disables buttons as necessary
	 */
	private void enableButtons(){
		for(String k: buttonMap.keySet()){
			buttonMap.get(k).setDisable(mySimulation.getRules() == null);
		}
		buttonMap.get("Speed Up").setDisable(mySimulation.getRules() == null || mySimulation.getSpeed() >= 20);
		buttonMap.get("Slow Down").setDisable(mySimulation.getRules() == null || mySimulation.getSpeed() <= 1);
		buttonMap.get("Generate XML").setDisable(false);
		buttonMap.get("Load XML").setDisable(false);
		
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
	 * Creates a new window to generate an xml
	 */
	private void generateXML(){
		XMLGeneratorView xgview = new XMLGeneratorView();
	}
	
	/**
	 * Handles reset Button press
	 */
	private void resetPressed(){
		if(mySimulation.getXML() != null){
			mySimulation.setRunning(false);
			mySimulation.loadXML(null);
			setupUI();
		}
	}
	
	/**
	 * Responds to mouse events on a given cell
	 */
	protected void respondToMouse(int r, int c){
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
	 * @return returns a map with the current color of each state
	 */
	protected Map<String, Color> getStateColorMap(){
		return stateColorMap;
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
		result.ifPresent(name -> myBB.setBorderPixelSize(result.get()));
		myBB.displayBoard(myBoardGroup);
	}
	
	/**
	 * Opens a dialog to change border color, redisplay board
	 */
	private void openBorderColorConfig(){
		final ColorPicker colorPicker = new ColorPicker(borderColor);
		myBoardGroup.getChildren().add(colorPicker);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) { 
                borderColor = colorPicker.getValue();
                myBoardGroup.getChildren().remove(colorPicker);
        		myBB.displayBoard(myBoardGroup);
            }
        });
	}
	
	/**
	 * @return returns the current color of the border
	 */
	protected Color getBorderColor(){
		return borderColor;
	}
	
	/**
	 * Updates the board and the chart 
	 */
	public void updateUI(){
		myBB.displayBoard(myBoardGroup);
		updateChart();
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


