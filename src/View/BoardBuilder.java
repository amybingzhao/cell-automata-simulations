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
	
	/**
	 * Builds a board onto the current board group if necessary (if board size changes)
	 * @param myBoardGroup
	 */
//	private void buildBoard(Group myBoardGroup){
//		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
//		myBoardGroup.getChildren().clear();
//		myBoard = new Rectangle[myGridHeight][myGridWidth];
//		Grid grid = mySimulation.getGrid();
//		for (int r = 0; r < grid.getNumRows(); r++) {
//			for (int c = 0; c < grid.getNumCols(); c++) {
//				Rectangle bg = new Rectangle();
//				bg.setLayoutY(r * (cellPixelSize + (2 * borderPixelSize)));
//				bg.setLayoutX(c * (cellPixelSize + (2 * borderPixelSize)));
//				bg.setWidth(cellPixelSize + (2 * borderPixelSize));
//				bg.setHeight(cellPixelSize + (2 * borderPixelSize));
//				bg.setFill(myView.getBorderColor());
//				
//				Rectangle rect = new Rectangle();
//				rect.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
//				rect.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
//				rect.setWidth(cellPixelSize);
//				rect.setHeight(cellPixelSize);
//				int x = r; //java 8 MADE me do it Professor!
//				int y = c; //"effectively final" and whatnot.
//				rect.setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
//				myBoard[r][c] = rect;
//				myBoardGroup.getChildren().addAll(bg,rect);
//			}
//		}
//	}
	
	protected abstract void buildBoard(Group myBoardGroup);
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
//	private void displayGridToBoard(){
//		Grid grid = mySimulation.getGrid();
//		for(int r = 0; r < grid.getNumRows(); r++){
//			for(int c = 0; c < grid.getNumCols(); c++){
//				myBoard[r][c].setFill(myView.getStateColorMap().get(grid.getCell(r,c).getCurState()));
//			}
//		}
//	}
	
	protected abstract void displayGridToBoard();
	
	protected void setBorderPixelSize(int size){
		borderPixelSize = size;
	}
	
	protected Rectangle[][] getMyBoard() {
		return myBoard;
	}

	protected void setMyBoard(Rectangle[][] myBoard) {
		this.myBoard = myBoard;
	}

	protected int getBoardPixelSize() {
		return boardPixelSize;
	}

	protected void setBoardPixelSize(int boardPixelSize) {
		this.boardPixelSize = boardPixelSize;
	}

	protected int getCellPixelSize() {
		return cellPixelSize;
	}

	protected void setCellPixelSize(int cellPixelSize) {
		this.cellPixelSize = cellPixelSize;
	}

	protected int getMaxCellsDisplayed() {
		return maxCellsDisplayed;
	}

	protected void setMaxCellsDisplayed(int maxCellsDisplayed) {
		this.maxCellsDisplayed = maxCellsDisplayed;
	}

	protected int getMyGridWidth() {
		return myGridWidth;
	}

	protected void setMyGridWidth(int myGridWidth) {
		this.myGridWidth = myGridWidth;
	}

	protected int getMyGridHeight() {
		return myGridHeight;
	}

	protected void setMyGridHeight(int myGridHeight) {
		this.myGridHeight = myGridHeight;
	}

	protected ResourceBundle getMyViewResources() {
		return myViewResources;
	}

	protected void setMyViewResources(ResourceBundle myViewResources) {
		this.myViewResources = myViewResources;
	}

	protected CSView getMyView() {
		return myView;
	}

	protected void setMyView(CSView myView) {
		this.myView = myView;
	}

	protected Simulation getMySimulation() {
		return mySimulation;
	}

	protected void setMySimulation(Simulation mySimulation) {
		this.mySimulation = mySimulation;
	}

	protected int getBorderPixelSize() {
		return borderPixelSize;
	}
}
