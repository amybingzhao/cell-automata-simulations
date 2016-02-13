/**
 * @author Amy Zhao
 * @author Austin Wu
 * The class responsible for maintaining the grid of Cell objects
 */

package Model;

public abstract class Grid {
	private int myRows;
	private int myCols;
	private Cell[][] myGrid;
	private boolean resizedImmediatelyBefore;
	private boolean resizedThisStep;
	
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
		resizedImmediatelyBefore = false;
		resizedThisStep = false;
	}
	
	/**
	 * Sets a flag indicating whether the grid was resized when checking the current cell.
	 * @param bool: true if the grid was resized when checking the current cell; false otherwise.
	 */
	public void setResizedImmediatelyBefore(boolean bool) {
		resizedImmediatelyBefore = bool;
	}
	
	/**
	 * Gets whether or not the grid was resized when checking the current cell.
	 * @return true if the grid was resized when checking the current cell; false otherwise.
	 */
	public boolean hasBeenResizedImmediatelyBefore() {
		return resizedImmediatelyBefore;
	}
	
	/**
	 * Sets a flag indicating whether the grid was resized during this step.
	 * @param bool: true if the grid was resized at any point during this step; false otherwise.
	 */
	public void setResizedThisStep(boolean bool) {
		resizedThisStep = bool;
	}
	
	/**
	 * Gets whether or not the grid was resized during this step.
	 * @return true if the grid was resized at any point during this step; false otherwise.
	 */
	public boolean hasBeenResizedThisStep() {
		return resizedThisStep;
	}
	
	/**
	 * Adds a new Cell to the Grid.
	 * @param row: the row that the Cell belongs in.
	 * @param col: the column that the Cell belongs in.
	 * @param cell: the Cell to be added.
	 */
	public void addCellToGrid(int row, int col, Cell cell) {
		myGrid[row][col] = cell;
	}
	
	/**
	 * Returns all neighbors of a given Cell and the Cell itself in their relative orientation.
	 * @param row: the row of the Cell whose neighborhood is of interest.
	 * @param col: the column o the Cell whose neighborhood is of interest.
	 * @param numNeighbors: the number of neighbors of interest for the simulation (4 or 8).
	 */
	public abstract Cell[][] getNeighborhood(int row, int col, int numNeighbors);
	
	/**
	 * Gets number of rows in the Grid.
	 * @return number of rows in the grid.
	 */
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

	/**
	 * Sets the grid if it has been resized.
	 * @param newGrid: resized grid.
	 */
	protected void setGrid(Cell[][] newGrid) {
		myGrid = newGrid;
		myRows = newGrid.length;
		myCols = newGrid[0].length;
		setResizedImmediatelyBefore(true);
		setResizedThisStep(true);
	}
	
	/**
	 * Checks whether a cell at (row, col) is within the bounds of the grid.
	 * @param row: row of cell to check.
	 * @param col: column of cell to check.
	 * @return true if in bounds; false otherwise.
	 */
	public boolean inBounds(int row, int col) {
		return (row >= 0 && row < myRows && col >= 0 && col < myCols);
	}

	/**
	 * Gets this grid.
	 * @return myGrid.
	 */
	public Cell[][] getGrid(){
		return myGrid;
	}
}
