// This entire file is part of my masterpiece.
// Austin Wu
package View;

import Controller.Simulation;
import Model.Grid;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public class StandardBoardBuilder extends BoardBuilder{
	public StandardBoardBuilder(CSView view, Simulation sim){
		super(view, sim);
	}
	
	/**
	 * Builds a board onto the given Group, will resize if necessary
	 * @param myBoardGroup Group to build nodes onto to create the board
	 */
	protected void buildBoard(Group myBoardGroup){
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
		Grid grid = mySimulation.getGrid();
		
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		int bgsize = cellPixelSize + (2 * borderPixelSize);
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = createRectangle(r*bgsize, c*bgsize, bgsize, bgsize);
				bg.setFill(myView.getBorderColor());
				
				myBoard[r][c] = createRectangle((r * bgsize) + borderPixelSize, (c * bgsize) + borderPixelSize, cellPixelSize, cellPixelSize);
				int x = r; 
				int y = c; 
				myBoard[r][c].setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoardGroup.getChildren().addAll(bg,myBoard[r][c]);
			}
		}
	}
	
	/**
	 * Displays the current grid of cells to the board
	 */
	protected void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				myBoard[r][c].setFill(myView.getStateColorMap().get(grid.getCell(r,c).getCurState()));
			}
		}
	}
}
