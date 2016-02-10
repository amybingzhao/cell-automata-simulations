/**
 * @author Austin Wu
 * @author Blake Kaplan
 * This class takes care of overall logic and UI elements
 */

package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Model.Cell;
import Model.Grid;
import Rules.Rules;
import View.CSView;
import XML.XMLGenerator;
import XML.XMLParser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/*
 * Todos:
 * 1. Refactor into view class
 * 2. Part 3 Visualization Stuff
 * 3. Different cell shapes
 * 4. Different grid edge types
 * 5. Simulation Styling
 * 6. Start resource file
 * 7. 
 * 
 */

/*
 * Sim keeps track of:
 * data
 * current simulation
 * current file
 * is the simulation running
 */

public class Simulation {
	private boolean running;
	private int mySpeed;
	private int tick;
	private boolean loaded;
	
	//xml determined variables
	private File xmlFile;
	private Grid myGrid;
	private Rules myRules;
	private String current; 
	
	private CSView myView;

	public static final String DEFAULT_CONTROLLER_RESOURCE = "Controller/DefaultController";
	private ResourceBundle myControllerResources;
	
	public Simulation(){
		myControllerResources = ResourceBundle.getBundle(DEFAULT_CONTROLLER_RESOURCE);
		mySpeed = Integer.parseInt(myControllerResources.getString("InitialSpeed"));
		tick = 0;
	}
	
	/**
	 * Returns name of the program.
	 */
	public String getTitle() {
		return myControllerResources.getString("Title");
	}


	/**
	 * Method that loads XML with the parser and then sets instance variables
	 */
	public void loadXML(File file){
		if(file != null)
			xmlFile = file;
		XMLParser parser = new XMLParser();
		parser.parse(xmlFile);
		myRules = parser.getRules(); 
		String[][] inputgrid = parser.getGrid();
		myGrid = new Grid(inputgrid[1].length, inputgrid[1].length, inputgrid);
		myRules.initGrid(myGrid, inputgrid);
		current = myRules.toString();
		myView.setGridInfo(inputgrid.length, inputgrid[1].length, current);
		loaded = true;
	}
	
	/**
	 * Applies rules to cells, updates their states, displays new states
	 */
	public void step(double elapsedTime, boolean stepping) {
		if(tick % (21 - mySpeed) != 0 && !stepping){
			tick++;
			return;
		}
		if(running || stepping){
			applyRulesToGrid();
			updateEachState();
			myView.displayGridToBoard();
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
	
	/**
	 * returns the current speed
	 */

	public int getSpeed(){
		return mySpeed;
	}
	
	/**
	 * setter method to change current speed by del
	 * @param del
	 * @return returns new speed
	 */
	public int changeSpeed(int del){
		mySpeed += del;
		return mySpeed;
	}
	
	/**
	 * @return returns the current speed
	 */
	public String getCurrent(){
		return current;
	}
	
	public void saveXML(){
		running = false;
		if (!loaded){
			Alert myAlert = new Alert(AlertType.INFORMATION);
			myAlert.setTitle("Saving Error");
			myAlert.setHeaderText(null);
			myAlert.setContentText("You must have a simulation loaded to save!");
			myAlert.showAndWait();
			return;
		}
		XMLGenerator myGenerator = new XMLGenerator();
		String myRulesName = current.replaceAll(" ", "");
		File myFile = myView.promptForFileName();
		if (myFile == null) return;
		myGenerator.save(myRulesName, myGrid.getNumRows(), myGrid.getNumCols(), myGrid.getGrid(), myRules.getParameters(), myFile);
	}
	
	/**
	 * sets the current state of running to the value of b
	 * @param b a boolean indicating running or not
	 */
	public void setRunning(boolean b){
		running = b;
	}
	
	/**
	 * @return returns the grid that this simulation is using
	 */
	public Grid getGrid(){
		return myGrid;
	}
	
	/**
	 * @return returns the rules that this simulation is using
	 * Applies rules to cells, updates their states, displays new states
	 */
	public Rules getRules(){
		return myRules;
	}
	
	/**
	 * @return returns the XMLFile that this simulation is using
	 */
	public File getXML(){
		return xmlFile;
	}
	
	/**
	 * @param sets the myView instance variable equal to v
	 */
	public void setView(CSView v){
		myView = v;
	}
	
	
	
	
}