package Model;

public class StandardGrid extends Grid{
	
	private Cell[][] myGrid;

	public StandardGrid(int rows, int cols, String[][] initialStates) {
		super(rows, cols, initialStates);
		myGrid = this.getGrid();
		// TODO Auto-generated constructor stub
	}
	
	public Cell[][] getNeighborhood(int row, int col, int numNeighbors) {
		Cell[][] neighborhood = new Cell[3][3];
		neighborhood[1][1] = this.getGrid()[row][col];
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

}
