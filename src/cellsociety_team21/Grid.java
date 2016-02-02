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
	
	/**
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
	
	/**
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
	
	/**
	 * Returns all neighbors of a given Cell and the Cell itself in their relative orientation.
	 * @param: r is the row of the Cell whose neighborhood is of interest.
	 * @param: c is the column o the Cell whose neighborhood is of interest.
	 * @param: numNeighbors is the number of neighbors of interest for the simulation (4 or 8).
	 */
	public Cell[][] getNeighborhood(int r, int c, int numNeighbors) {
		Cell[][] neighborhood = new Cell[3][3];
		neighborhood[1][1] = myGrid[r][c];
		int numRows = getNumRows();
		int numCols = getNumCols();
		
		if (r - 1 >= 0) {
			neighborhood[0][1] = myGrid[r-1][c];
		} else {
			neighborhood[0][1] = null;
		}
		
		if (r + 1 < numRows) {
			neighborhood[2][1] = myGrid[r+1][c];
		} else {
			neighborhood[2][1] = null;
		}
		
		if (c - 1 >= 0) {
			neighborhood[1][0] = myGrid[r][c-1];
		} else {
			neighborhood[1][0] = null;
		}
		
		if (c + 1 < numCols) {
			neighborhood[1][2] = myGrid[r][c+1];
		} else {
			neighborhood[1][2] = null;
		}
		
		if (numNeighbors == 8) {
			if (r - 1 >= 0) {
				if (c - 1 >= 0) {
					neighborhood[0][0] = myGrid[r-1][c-1];
				} else {
					neighborhood[0][0] = null;
				}
				if (c + 1 < numCols) {
					neighborhood[0][2] = myGrid[r-1][c+1];
				} else {
					neighborhood[0][2] = null;
				}
			}
			if (r + 1 < numRows) {
				if (c - 1 >= 0) {
					neighborhood[2][0] = myGrid[r+1][c-1];
				} else {
					neighborhood[2][0] = null;
				}
				if (c + 1 < numCols) {
					neighborhood[2][2] = myGrid[r+1][c+1];
				} else {
					neighborhood[2][2] = null;
				}
			}
		}
		return neighborhood;
	}
	
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
	
	public void updateEachState(){
		for(int r = 0; r < myGrid.length; r++){
			for(int c = 0; c < myGrid.length; c++){
				myGrid[r][c].updateState();
			}
		}
	}
}
