package Model;

import Rules.Rules;

public class InfiniteGrid extends Grid {
	Rules myRules;
	
	public InfiniteGrid(int rows, int cols, String[][] initialStates, Rules rules) {
		super(rows, cols, initialStates);
		myRules = rules;
	}
	
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[3][3];
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
	
	private void resizeGrid() {
		int curRows = getNumRows();
		int curCols = getNumCols();
		Cell[][] newGrid = new Cell[curRows + 2][curCols + 2];
		populateGridWithExistingCells(newGrid, curRows, curCols);
		addTopLayer(newGrid);
		addBotLayer(newGrid);
		addLeftLayer(newGrid);
		addRightLayer(newGrid);
		setGrid(newGrid);
	}
	
	private void populateGridWithExistingCells(Cell[][] newGrid, int curRows, int curCols) {
		for (int row = 0; row < curRows; row ++) {
			for (int col = 0; col < curCols; col++) {
				Cell cell = getCell(row, col);
				cell.setLocation(row + 1, col + 1);
				newGrid[row + 1][col + 1] = cell;
			
			}
		}
	}

	private void addTopLayer(Cell[][] grid) {
		int row = 0;
		for (int col = 0; col < grid[0].length; col++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}

	private void addBotLayer(Cell[][] grid) {
		int row = grid.length - 1;
		for (int col = 0; col < grid[0].length; col++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}

	private void addLeftLayer(Cell[][] grid) {
		int col = 0;
		for (int row = 0; row < grid.length; row++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}

	private void addRightLayer(Cell[][] grid) {
		int col = grid[0].length - 1;
		for (int row = 0; row < grid.length; row++) {
			grid[row][col] = myRules.createDefaultCell(row, col);
		}
	}
}
