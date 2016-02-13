package Model;

public class StandardGrid extends Grid{
	
	private Cell[][] myGrid;

	public StandardGrid(int rows, int cols, String[][] initialStates) {
		super(rows, cols, initialStates);
		myGrid = this.getGrid();
	}
	
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[3][3];
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
