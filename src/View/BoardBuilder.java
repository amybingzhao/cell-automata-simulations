package View;

import java.util.Map;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class BoardBuilder {
	/*
	 * Class is responsible for:
	 * building the board
	 * displaying the board
	 * takes in the current view object
	 * only rebuild if 1) its infinite mode and 2) size changes
	 */
	
	protected Rectangle[][] myBoard;
	
	//UI Metrics
	protected int boardPixelSize;
	protected int cellPixelSize;
	protected int borderPixelSize;
	protected int maxCellsDisplayed;
	protected int myGridWidth;
	protected int myGridHeight;
	
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	protected ResourceBundle myViewResources;
	
	protected CSView myView;
	protected Simulation mySimulation;
	
	public BoardBuilder(CSView view, Simulation sim){
		myView = view;
		mySimulation = sim;
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		myGridWidth = 0;
		myGridHeight = 0;
		loadResources(myViewResources);
		
	}
	
	private void loadResources(ResourceBundle r){
		boardPixelSize = Integer.parseInt(myViewResources.getString("BoardPixelSize"));
		borderPixelSize = Integer.parseInt(myViewResources.getString("DefaultBorderPixelSize"));
		maxCellsDisplayed = Integer.parseInt(myViewResources.getString("DefaultMaxCellsDisplayed"));
	}
	
	/**
	 * builds board by adding rectangles, with the size and quantity based on xml input
	 * If board shape changes, rebuilds board, otherwise just redisplays
	 */
	protected void displayBoard(Group myBoardGroup){
		int currentGridWidth = mySimulation.getGrid().getNumCols();
		int currentGridHeight = mySimulation.getGrid().getNumRows();
		if(currentGridWidth != myGridWidth || currentGridHeight != myGridHeight){
			myGridWidth = currentGridWidth;
			myGridHeight = currentGridHeight;
			buildBoard(myBoardGroup);
		}
		displayGridToBoard();
	}

	protected int getBorderPixelSize() {
		return borderPixelSize;
	}

	protected void setBorderPixelSize(int borderPixelSize) {
		this.borderPixelSize = borderPixelSize;
	}

	protected abstract void buildBoard(Group myBoardGroup);
	
	protected abstract void displayGridToBoard();
	

}
