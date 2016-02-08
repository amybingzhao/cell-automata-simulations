/**
 * @author Autin Wu
 * This class takes care of overall logic and UI elements
 */

package Controller;

import java.io.File;
import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Rules.Rules;
import View.CSView;
import XML.XMLParser;
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
	public static final String TITLE = "Cell Society Simulation";
	
	
	private boolean running;
	private int mySpeed;
	private int tick;
	
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
	}
	
	/**
	 * Returns name of the game.
	 */
	public String getTitle() {
		return TITLE;
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
		myView.setGridDimensions(inputgrid.length, inputgrid[1].length, current);
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
	
	public int changeSpeed(int del){
		mySpeed += del;
		return mySpeed;
	}
	
	public String getCurrent(){
		return current;
	}
	
	public void setRunning(boolean b){
		running = b;
	}
	
	public Grid getGrid(){
		return myGrid;
	}
	
	public Rules getRules(){
		return myRules;
	}
	
	public File getXML(){
		return xmlFile;
	}
	
	public void setView(CSView v){
		myView = v;
	}
	
	
	
	
}