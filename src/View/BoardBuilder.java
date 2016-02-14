package View;

import java.util.Map;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.Grid;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardBuilder {
	/*
	 * Class is responsible for:
	 * building the board
	 * displaying the board
	 * takes in the current view object
	 * only rebuild if 1) its infinite mode and 2) size changes
	 */
	
	private Rectangle[][] myBoard;
	
	//UI Metrics
	private int boardPixelSize;
	private int cellPixelSize;
	private int borderPixelSize;
	private int maxCellsDisplayed;
	private int myGridWidth;
	private int myGridHeight;
	
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	private ResourceBundle myViewResources;
	
	private CSView myView;
	private Simulation mySimulation;
	
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
	 * Generates the background rectangle for the board
	 * @return a Rectangle object
	 */
	protected Rectangle getBackground(){
		Rectangle boardBackground = new Rectangle();
		boardBackground.setFill(Color.WHITE);
		boardBackground.setWidth(boardPixelSize);
		boardBackground.setHeight(boardPixelSize);
		return boardBackground;
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
	 * Builds a board onto the current board group if necessary (if board size changes)
	 * @param myBoardGroup
	 */
	private void buildBoard(Group myBoardGroup){
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
				bg.setFill(myView.getBorderColor());
				
				Rectangle rect = new Rectangle();
				rect.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				int x = r; //java 8 MADE me do it Professor!
				int y = c; //"effectively final" and whatnot.
				rect.setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoard[r][c] = rect;
				myBoardGroup.getChildren().addAll(bg,rect);
			}
		}
	}
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
	private void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				myBoard[r][c].setFill(myView.getStateColorMap().get(grid.getCell(r,c).getCurState()));
			}
		}
	}
	
	protected void setBorderPixelSize(int size){
		borderPixelSize = size;
	}
	
	
}
