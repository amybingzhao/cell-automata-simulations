// This entire file is part of my masterpiece.
// Austin Wu
package View;

import Controller.Simulation;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ForagingAntsBoardBuilder extends BoardBuilder{

	private Label[][] antcounts;
	
	public ForagingAntsBoardBuilder(CSView view, Simulation sim) {
		super(view, sim);
	}

	/**
	 * Builds a board onto the given Group, will resize if necessary
	 * @param myBoardGroup Group to build nodes onto to create the board
	 */
	protected void buildBoard(Group myBoardGroup){
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
		antcounts = new Label[myGridWidth][myGridHeight];
		Grid grid = mySimulation.getGrid();
		
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		int bgsize = cellPixelSize + (2 * borderPixelSize);
		
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = createRectangle(r*bgsize, c*bgsize, bgsize, bgsize);
				bg.setFill(myView.getBorderColor());
				antcounts[r][c] = buildLabel(r,c, bgsize);
				myBoard[r][c] = createRectangle((r * bgsize) + borderPixelSize, (c * bgsize) + borderPixelSize, cellPixelSize, cellPixelSize);
				int x = r; 
				int y = c; 
				myBoard[r][c].setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoardGroup.getChildren().addAll(bg,myBoard[r][c], antcounts[r][c]);
			}
		}
	}
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
	protected void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Color cellcolor = myView.getStateColorMap().get(grid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(cellcolor);
				ForagingAntsCell ant = (ForagingAntsCell) mySimulation.getGrid().getCell(r, c);
				antcounts[r][c].setText(Integer.toString(ant.getNumAnts()));
				antcounts[r][c].setTextFill(cellcolor.invert());
			}
		}
	}
	
	/**
	 * Builds and positions a label at the given location and size
	 * @param r row of the label
	 * @param c col of the label
	 * @param bgsize size of the label's background
	 * @return
	 */
	private Label buildLabel(int r, int c, int bgsize){
		Label antcount = new Label();
		antcount.setFont(new Font("Arial", 14));
		antcount.setLayoutY((r * bgsize) + borderPixelSize);
		antcount.setLayoutX((c * bgsize) + borderPixelSize);
		return antcount;
	}
}
