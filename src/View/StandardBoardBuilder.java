package View;

import java.util.Map;
import java.util.ResourceBundle;

import Controller.Simulation;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StandardBoardBuilder extends BoardBuilder{
	public StandardBoardBuilder(CSView view, Simulation sim){
		super(view, sim);
	}
	
	protected void buildBoard(Group myBoardGroup){
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
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
	protected void displayGridToBoard(){
		Grid grid = mySimulation.getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				myBoard[r][c].setFill(myView.getStateColorMap().get(grid.getCell(r,c).getCurState()));
			}
		}
	}
}
