package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Grid {
	private static List<Cell> toBeUpdated;
	private static int myRows;
	private static int myCols;
	private Cell[][] myGrid;
	
	/* 
	 * Sets the rules, possible states, grid size, and initial states for the current simulation.
	 * @param: rows is the number of rows for the specific simulation, as determined by the XMLParser.
	 * @param: cols is the number of columns for the specific simulation, as determined by the XMLParser.
	 * @param: initialStates is the initialStates for the specific simulation, as determined by the XMLParser, with
	 * each state denoted by an integer. initialStates' size should match that of the Grid.
	 */
	public Grid(int rows, int cols, String[][] initialStates) {
		toBeUpdated = new ArrayList<Cell>();
		myRows = rows;
		myCols = cols;
		myGrid = new Cell[myRows][myCols];
		init(initialStates);
	}
	
	/*
	 * Initializes the states of each cell in the grid based on XML info.
	 */
	private void init(String[][] initialStates) {
		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				Cell cell = new Cell(initialStates[r][c], r, c);
				myGrid[r][c] = cell;
			}
		}
	}
	
	// TODO: Do we want to return a Cell[3][3] or do we want to return a Cell[8] with some predetermined order for
	// which neighbors are at which index (i.e. getNeighbors or getNeighborhood?)
	/*
	 * Returns all neighbors of a given cell.
	 * @param: myCell is the cell whose neighbors are being returned
	 */
	private Cell[] getNeighbors(Cell myCell) {
		// get # of neighbors needed from the Rules object (i.e. 4 or 8)
		
		return new Cell[9];
	}
	// TODO: unsure how to render the grid
	/*
	public Image render() {
		return new Image();
	}*/
	
	public int getNumRows() {
		return myRows;
	}
	
	public int getNumCols() {
		return myCols;
	}
	
	public List getToBeUpdatedList() {
		return toBeUpdated;
	}
	
	public Cell getCell(int r, int c) {
		return myGrid[r][c];
	}
}
