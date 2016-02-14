package View;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import Controller.Simulation;
import Model.Grid;
import Model.SugarScapeCell;
import Rules.SugarScapeRules;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SugarScapeBoardBuilder extends BoardBuilder {

	Circle[][] myCircles;
	
	public SugarScapeBoardBuilder(CSView view, Simulation sim) {
		super(view, sim);
	}
	
	/**
	 * Builds a board onto the current board group if necessary (if board size changes)
	 * @param myBoardGroup
	 */
	protected void buildBoard(Group myBoardGroup){
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
		myCircles = new Circle[myGridHeight][myGridWidth];
		Grid grid = mySimulation.getGrid();
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = new Rectangle();
				bg.setLayoutY(r * (cellPixelSize + (2 * borderPixelSize)));
				bg.setLayoutX(c * (cellPixelSize + (2 * borderPixelSize)));
				bg.setWidth(cellPixelSize + (2 * borderPixelSize));
				bg.setHeight(cellPixelSize + (2 * borderPixelSize));
				bg.setFill(myView.getBorderColor());
				
				Circle cir = new Circle();
				cir.setCenterY(((double) r + .5) * (cellPixelSize + (2 * borderPixelSize)));
				cir.setCenterX(((double) c + .5) * (cellPixelSize + (2 * borderPixelSize)));
				cir.setRadius(cellPixelSize/4);
				cir.setFill(myView.getBorderColor());
				myCircles[r][c] = cir;
				
				Rectangle rect = new Rectangle();
				rect.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				int x = r; //java 8 MADE me do it Professor!
				int y = c; //"effectively final" and whatnot.
				rect.setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoard[r][c] = rect;
				myBoardGroup.getChildren().addAll(bg,rect, cir);
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
		Color color = cellcolor.deriveColor(0, 0.9 * ratio, 1, 1);
		return color;
	}

}
