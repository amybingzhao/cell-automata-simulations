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
	 * Builds a board onto the current board group if necessary (if board size changes)
	 * @param myBoardGroup
	 */
	protected void buildBoard(Group myBoardGroup){
		cellPixelSize = (boardPixelSize / Math.min(maxCellsDisplayed, Math.max(myGridWidth, myGridHeight))) - 2 * borderPixelSize;
		myBoardGroup.getChildren().clear();
		myBoard = new Rectangle[myGridHeight][myGridWidth];
		antcounts = new Label[myGridWidth][myGridHeight];
		Grid grid = mySimulation.getGrid();
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				Rectangle bg = new Rectangle();
				bg.setLayoutY(r * (cellPixelSize + (2 * borderPixelSize)));
				bg.setLayoutX(c * (cellPixelSize + (2 * borderPixelSize)));
				bg.setWidth(cellPixelSize + (2 * borderPixelSize));
				bg.setHeight(cellPixelSize + (2 * borderPixelSize));
				bg.setFill(myView.getBorderColor());
				
				Label antcount = new Label();
				antcount.setFont(new Font("Arial", 15));
				antcount.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				antcount.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				antcount.setMaxWidth(cellPixelSize);
				antcount.setMaxHeight(cellPixelSize);
				
				antcounts[r][c] = antcount;
				
				Rectangle rect = new Rectangle();
				rect.setLayoutY((r * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setLayoutX((c * (cellPixelSize + (2 * borderPixelSize))) + borderPixelSize);
				rect.setWidth(cellPixelSize);
				rect.setHeight(cellPixelSize);
				int x = r; //java 8 MADE me do it Professor!
				int y = c; //"effectively final" and whatnot.
				rect.setOnMouseClicked(e -> myView.respondToMouse(x, y)); 
				myBoard[r][c] = rect;
				myBoardGroup.getChildren().addAll(bg,rect, antcount);
			}
		}
	}
	
	/**
	 * Displays the grid to the board based on the state of each of its cells
	 */
	protected void displayGridToBoard(){
		Grid grid = getMySimulation().getGrid();
		for(int r = 0; r < grid.getNumRows(); r++){
			for(int c = 0; c < grid.getNumCols(); c++){
				Color cellcolor = getMyView().getStateColorMap().get(grid.getCell(r,c).getCurState());
				myBoard[r][c].setFill(cellcolor);
				ForagingAntsCell ant = (ForagingAntsCell) getMySimulation().getGrid().getCell(r, c);
				antcounts[r][c].setText(Integer.toString(ant.getNumAnts()));
				antcounts[r][c].setTextFill(cellcolor.invert());
			}
		}
	}
	
	
}
