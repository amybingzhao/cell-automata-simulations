package Model;

public class StandardGrid extends Grid{
	
	public StandardGrid(int rows, int cols, String[][] initialStates) {
		super(rows, cols, initialStates);
	}
	
	/**
	 * Get neighborhood of the cell at (row, col) in the grid.
	 * @param row: row of the cell of interest.
	 * @param col: column of the cell of interest.
	 * @param numNeighbors: number of neighbors to get based on simulation type (4 or 8).
	 */
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[getNeighborGridSideLength()][getNeighborGridSideLength()];
		if (numNeighbors == 4) {
			int[] rowDirections = new int[]{-1, 0, 1, 0};
			int[] colDirections = new int[]{0, 1, 0, -1};
			for (int rowOffset = 0; rowOffset < rowDirections.length; rowOffset++) {
				int colOffset = rowOffset;
				tryToAddNeighbor(row, col, rowDirections, colDirections, rowOffset, colOffset, neighborhood);
			}
		} else if (numNeighbors == 8) {
			int[] rowDirections = new int[]{-1, 0, 1};
			int[] colDirections = new int[]{-1, 0, 1};
			for (int rowOffset = 0; rowOffset < rowDirections.length; rowOffset++) {
				for (int colOffset = 0; colOffset < colDirections.length; colOffset++) {
					tryToAddNeighbor(row, col, rowDirections, colDirections, rowOffset, colOffset, neighborhood);
				}
			}
		}

		return neighborhood;
	}
	
	/**
	 * Check if the desired neighbor is within the bounds of the grid; otherwise add a null to signify there is no neighbor.
	 * @param row: row of cell whose neighbors you are getting.
	 * @param col: column of cell whose neighbors you are getting.
	 * @param rowDirections: relative directions to check in the row dimension.
	 * @param colDirections: relative directions to check in the column dimension.
	 * @param rowOffset: index within the rowDirections array.
	 * @param colOffset: index within the colDirections array.
	 * @param neighborhood: neighborhood to add to.
	 */
	private void tryToAddNeighbor(int row, int col, int[] rowDirections, int[] colDirections, int rowOffset, int colOffset, Cell[][] neighborhood) {
		int rowToCheck = row + rowDirections[rowOffset];
		int colToCheck = col + colDirections[colOffset];
		if (inBounds(rowToCheck, colToCheck)) {
			neighborhood[rowDirections[rowOffset] + 1][colDirections[colOffset] + 1] = this.getCell(rowToCheck, colToCheck);
		} else {
			neighborhood[rowDirections[rowOffset] + 1][colDirections[colOffset] + 1] = null;
		}
	}

}
