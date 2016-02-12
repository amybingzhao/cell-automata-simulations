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
	private boolean resized;
	
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
		resized = false;
	}
	
	public void setResized(boolean bool) {
		resized = bool;
	}
	
	public boolean hasBeenResized() {
		return resized;
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

	protected void setGrid(Cell[][] newGrid) {
		myGrid = newGrid;
		myRows = newGrid.length;
		myCols = newGrid[0].length;
		setResized(true);
	}
	
	public boolean inBounds(int row, int col) {
		System.out.println("grid dimensions:");
		System.out.println("\t(" + myRows + ", " + myCols + ")");
		return (row >= 0 && row < myRows && col >= 0 && col < myCols);
	}

	public Cell[][] getGrid(){
		return myGrid;
	}
}
