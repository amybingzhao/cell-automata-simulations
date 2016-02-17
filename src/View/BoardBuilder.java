// This entire file is part of my masterpiece.
// Blake Kaplan

package View;

import java.util.ResourceBundle;

import Controller.Simulation;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public abstract class BoardBuilder {
	private static final String DEFAULT_MAX_CELLS_DISPLAYED = "DefaultMaxCellsDisplayed";
	private static final String DEFAULT_BORDER_PIXEL_SIZE = "DefaultBorderPixelSize";
	private static final String BOARD_PIXEL_SIZE = "BoardPixelSize";
	protected Rectangle[][] myBoard;
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
		boardPixelSize = Integer.parseInt(r.getString(BOARD_PIXEL_SIZE));
		borderPixelSize = Integer.parseInt(r.getString(DEFAULT_BORDER_PIXEL_SIZE));
		maxCellsDisplayed = Integer.parseInt(r.getString(DEFAULT_MAX_CELLS_DISPLAYED));
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

	protected void setBorderPixelSize(int newBorderPixelSize) {
		this.borderPixelSize = borderPixelSize;
	}

	protected abstract void buildBoard(Group myBoardGroup);
	
	protected abstract void displayGridToBoard();
	

}
