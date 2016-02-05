package cellsociety_team21;

public class Grid {
	private static int myRows;
	private static int myCols;
	private Cell[][] myGrid;
	
	/**
	 * Sets the rules, possible states, grid size, and initial states for the current simulation.
	 * @param rows: the number of rows for the specific simulation, as determined by the XMLParser.
	 * @param cols: the number of columns for the specific simulation, as determined by the XMLParser.
	 * @param initialStates: the initialStates for the specific simulation, as determined by the XMLParser, with
	 * each state denoted by an integer. initialStates' size should match that of the Grid.
	 */
	public Grid(int rows, int cols, String[][] initialStates) {
		myRows = rows;
		myCols = cols;
		myGrid = new Cell[myRows][myCols];
	}
	
	public void addCellToGrid(int row, int col, Cell cell) {
		myGrid[row][col] = cell;
	}
	
	/**
	 * Returns all neighbors of a given Cell and the Cell itself in their relative orientation.
	 * @param r: the row of the Cell whose neighborhood is of interest.
	 * @param c: the column o the Cell whose neighborhood is of interest.
	 * @param numNeighbors: the number of neighbors of interest for the simulation (4 or 8).
	 */
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[3][3];
		neighborhood[1][1] = myGrid[row][col];
		int numRows = getNumRows();
		int numCols = getNumCols();
		
		if (row - 1 >= 0) {
			neighborhood[0][1] = myGrid[row-1][col];
		} else {
			neighborhood[0][1] = null;
		}
		
		if (row + 1 < numRows) {
			neighborhood[2][1] = myGrid[row+1][col];
		} else {
			neighborhood[2][1] = null;
		}
		
		if (col - 1 >= 0) {
			neighborhood[1][0] = myGrid[row][col-1];
		} else {
			neighborhood[1][0] = null;
		}
		
		if (col + 1 < numCols) {
			neighborhood[1][2] = myGrid[row][col+1];
		} else {
			neighborhood[1][2] = null;
		}
		
		if (numNeighbors == 8) {
			if (row - 1 >= 0) {
				if (col - 1 >= 0) {
					neighborhood[0][0] = myGrid[row-1][col-1];
				} else {
					neighborhood[0][0] = null;
				}
				if (col + 1 < numCols) {
					neighborhood[0][2] = myGrid[row-1][col+1];
				} else {
					neighborhood[0][2] = null;
				}
			}
			if (row + 1 < numRows) {
				if (col - 1 >= 0) {
					neighborhood[2][0] = myGrid[row+1][col-1];
				} else {
					neighborhood[2][0] = null;
				}
				if (col + 1 < numCols) {
					neighborhood[2][2] = myGrid[row+1][col+1];
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
	
	/**
	 * Gets number of columns in the Grid.
	 * @return number of columns in the Grid.
	 */
	public int getNumCols() {
		return myCols;
	}
	
	/**
	 * Gets the Cell at a specific location within the Grid.
	 * @param row: row of the Cell of interest.
	 * @param col: column of the Cell of interest.
	 * @return Cell at [row, col] of the Grid.
	 */
	public Cell getCell(int row, int col) {
		return myGrid[row][col];
	}
	
	/**
	 * Returns a representation of the Cells in the Grid.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < myRows; row++) {
			for (int col = 0; col < myCols; col++) {
				sb.append(myGrid[row][col].toString());
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
