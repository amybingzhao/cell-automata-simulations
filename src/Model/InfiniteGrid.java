/**
 * @author Amy Zhao
 * Defines the variables and methods for an infinite grid object.
 */

package Model;

import Rules.Rules;

public class InfiniteGrid extends Grid {
	Rules myRules;
	
	/**
	 * Constructs an infinite grid specific to a given simulation's rules.
	 * @param rows: number of rows.
	 * @param cols: number of columns.
	 * @param initialStates: initial states of each cell.
	 * @param rules: rules specific to a given simulation.
	 */
	public InfiniteGrid(int rows, int cols, String[][] initialStates, Rules rules) {
		super(rows, cols, initialStates);
		myRules = rules;
	}
	
	/**
	 * Get the surrounding neighborhood; resize if getting a neighbor requires going out of bounds by adding an outer
	 * layer of cells to each side.
	 * @param row: row of current cell.
	 * @param col: column of current cell.
	 * @param numNeighbors: 4 or 8 depending on simulation.
	 */
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[getNeighborGridSideLength()][getNeighborGridSideLength()];
		int[] rowDirections = new int[]{-1, 0, 1};
		int[] colDirections = new int[]{-1, 0, 1};
		
		for (int rowOffset = 0; rowOffset < rowDirections.length; rowOffset++) {
			for (int colOffset = 0; colOffset < colDirections.length; colOffset++) {
				int rowToCheck = row + rowDirections[rowOffset];
				int colToCheck = col + colDirections[colOffset];
				if (inBounds(rowToCheck, colToCheck)) {
					neighborhood[rowDirections[rowOffset] + 1][colDirections[colOffset] + 1] = this.getCell(rowToCheck, colToCheck);
				} else {
					resizeGrid();
					row = row + 1;
					col = col + 1;
					neighborhood[rowDirections[rowOffset] + 1][colDirections[colOffset] + 1] = getCell(rowToCheck + 1, colToCheck + 1);
				}
			}
		}
		
		return neighborhood;
	}
	
	/**
	 * Resizes the grid by adding cells to the top, bottom, left, and right, and sets the resized grid to the current grid.
	 */
	private void resizeGrid() {
		int curRows = getNumRows();
		int curCols = getNumCols();
		Cell[][] newGrid = new Cell[curRows + getNumRowsColsToExpand()][curCols + getNumRowsColsToExpand()];
		populateGridWithExistingCells(newGrid, curRows, curCols);
		addTopOrBotLayer(newGrid, 0);
		addTopOrBotLayer(newGrid, newGrid.length - 1);
		addLeftOrRightLayer(newGrid, 0);
		addLeftOrRightLayer(newGrid, newGrid[0].length - 1);
		setGrid(newGrid);
	}
	
	/**
	 * Populates a new grid with the existing cells from the current grid.
	 * @param newGrid: resized grid.
	 * @param curRows: number of rows in the current grid.
	 * @param curCols: number of columns in the current grid.
	 */
	private void populateGridWithExistingCells(Cell[][] newGrid, int curRows, int curCols) {
		for (int row = 0; row < curRows; row ++) {
			for (int col = 0; col < curCols; col++) {
				Cell cell = getCell(row, col);
				cell.setLocation(row + 1, col + 1);
				newGrid[row + 1][col + 1] = cell;
			}
		}
	}

	/**
	 * Adds a layer of DEFAULT cells to the top or bottom of the grid.
	 * @param grid: grid to add to.
	 * @param relative row: 0 for top, grid.length - 1 for bot
	 */
	private void addTopOrBotLayer(Cell[][] grid, int row) {
		for (int col = 0; col < grid[0].length; col++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}

	/**
	 * Adds a layer of DEFAULT cells to the left or right of the grid.
	 * @param grid: grid to add to.
	 * @param relative col: 0 for left, grid.length[0] - 1 for right
	 */
	private void addLeftOrRightLayer(Cell[][] grid, int col) {
		for (int row = 0; row < grid.length; row++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}
}
