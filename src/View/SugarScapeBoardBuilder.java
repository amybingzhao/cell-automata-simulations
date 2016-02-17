// This entire file is part of my masterpiece.
// Austin Wu
package View;

import Controller.Simulation;
import Model.Grid;
import Model.SugarScapeCell;
import Rules.SugarScapeRules;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SugarScapeBoardBuilder extends BoardBuilder {

	Circle[][] myCircles;
	
	public SugarScapeBoardBuilder(CSView view, Simulation sim) {
		super(view, sim);
	}
	
	/**
	 * Builds a board onto the given Group, will resize if necessary
	 * @param myBoardGroup Group to build nodes onto to create the board
	 */
		protected void buildBoard(Group myBoardGroup){
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
		myCircles = new Circle[myGridHeight][myGridWidth];
		Grid grid = mySimulation.getGrid();
		
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		int bgsize = cellPixelSize + (2 * borderPixelSize);
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = createRectangle(r*bgsize, c*bgsize, bgsize, bgsize);
				bg.setFill(myView.getBorderColor());
				
				myCircles[r][c] = buildCircle(r, c, bgsize);
				
				myBoard[r][c] = createRectangle((r * bgsize) + borderPixelSize, (c * bgsize) + borderPixelSize, cellPixelSize, cellPixelSize);
				int x = r; 
				int y = c; 
				myBoard[r][c].setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoardGroup.getChildren().addAll(bg,myBoard[r][c], myCircles[r][c]);
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
				Color sugaramountcolor = sugarColor((SugarScapeCell) grid.getCell(r, c), myView.getStateColorMap().get("OCCUPIED"));
				myBoard[r][c].setFill(sugaramountcolor);
				Color cellcolor = myView.getStateColorMap().get(grid.getCell(r,c).getCurState());
				if(grid.getCell(r, c).getCurState().equals(mySimulation.getRules().getDefault())){
					myCircles[r][c].setFill(sugaramountcolor);
				} else {
					myCircles[r][c].setFill(cellcolor);
				}
			}
		}
	}
	
	/**
	 * Generates a correct color for a given amount of sugar
	 * @return
	 */
	private Color sugarColor(SugarScapeCell cell, Color cellcolor){
		double mysugar = cell.getMySugarAmount();
		double maxsugar = ((SugarScapeRules) mySimulation.getRules()).getMyMaxCellSugarCapacity();
		double ratio = mysugar/maxsugar;
		Color color = cellcolor.deriveColor(0, 0.8 * ratio, 1, 1);
		return color;
	}
	
	/**
	 * Builds and positions a circle at the given location and size
	 * @param r row of the label
	 * @param c col of the label
	 * @param bgsize size of the label's background
	 * @return
	 */
	private Circle buildCircle(int r, int c, int bgsize){
		Circle cir = new Circle();
		cir.setCenterY(((double) r + .5) * bgsize);
		cir.setCenterX(((double) c + .5) * bgsize);
		cir.setRadius(cellPixelSize/4);
		cir.setFill(myView.getBorderColor());
		return cir;
	}
	
	

}
