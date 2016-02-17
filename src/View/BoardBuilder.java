// This entire file is part of my masterpiece.
// Austin Wu
package View;

import java.util.ResourceBundle;

import Controller.Simulation;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class BoardBuilder {
	
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
	
	/**
	 * Loads instance variables
	 * @param view view this BoardBuilder is to be linked to
	 * @param sim simulation this BoardBuilder is to be linked to
	 */
	public BoardBuilder(CSView view, Simulation sim){
		myView = view;
		mySimulation = sim;
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		loadResources(myViewResources);
		
	}
	
	/**
	 * Builds a board onto the given Group, will resize if necessary
	 * @param myBoardGroup Group to build nodes onto to create the board
	 */
	protected abstract void buildBoard(Group myBoardGroup);
	
	/**
	 * Displays the current grid of cells to the board
	 */
	protected abstract void displayGridToBoard();
	
	/**
	 * Creates a new rectangle with dimensions and locations specified by param
	 * @param layouty y coord of rectangle
	 * @param layoutx x coord of rectangle 
	 * @param width width of rectangle
	 * @param height height of rectangle
	 * @return
	 */
	protected Rectangle createRectangle(int layouty, int layoutx, int width, int height){
		Rectangle rect = new Rectangle();
		rect.setLayoutY(layouty);
		rect.setLayoutX(layoutx);
		rect.setHeight(width);
		rect.setWidth(height);
		return rect;
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

	/**
	 * 
	 * @return Returns the border width in pixels
	 */
	protected int getBorderPixelSize() {
		return borderPixelSize;
	}

	/**
	 * 
	 * @param borderPixelSize changes the border width in pixels to this int argument
	 */
	protected void setBorderPixelSize(int borderPixelSize) {
		this.borderPixelSize = borderPixelSize;
	}
	

	/**
	 * Loads resources from a resource bundle
	 * @param r resource bundle to be loaded from
	 */
	private void loadResources(ResourceBundle r){
		boardPixelSize = Integer.parseInt(myViewResources.getString("BoardPixelSize"));
		borderPixelSize = Integer.parseInt(myViewResources.getString("DefaultBorderPixelSize"));
		maxCellsDisplayed = Integer.parseInt(myViewResources.getString("DefaultMaxCellsDisplayed"));
	}
}
