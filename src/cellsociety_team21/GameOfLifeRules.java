package cellsociety_team21;

public class GameOfLifeRules extends Rules {
	private static final int NUM_NEIGHBORS = 8;
	private static final String DEAD = "DEAD";
	private static final String ALIVE = "ALIVE";
	private static final int MY_CELL_ROW = 1;
	private static final int MY_CELL_COL = 1;
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		Cell[][] neighborhood = grid.getNeighborhood(cell.getRow(), cell.getCol(), NUM_NEIGHBORS);
		int numLiveNeighbors = countNumLiveNeighbors(neighborhood);
		if (curState.equals(DEAD)) {
			handleDeadCells(cell, numLiveNeighbors);
		} else {
			handleAliveCells(cell, numLiveNeighbors);
		}
	}

	private int countNumLiveNeighbors(Cell[][] neighborhood) {
		int numNeighbors = 0;
		
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood[row].length; col++) {
				if (neighborhood[row][col] != null) {
					if (row != MY_CELL_ROW || col != MY_CELL_COL) {
						if (neighborhood[row][col].getCurState().equals(ALIVE)) {
							numNeighbors++;
						}
					}
				}
			}
		}
		
		return numNeighbors;
	}

	private void handleAliveCells(Cell cell, int numLiveNeighbors) {
		if (numLiveNeighbors < 2 || numLiveNeighbors > 3) {
			cell.setNextState(DEAD);
			addCellToBeUpdated(cell);
		}
	}

	private void handleDeadCells(Cell cell, int numLiveNeighbors) {
		if (numLiveNeighbors == 3) {
			cell.setNextState(ALIVE);
			addCellToBeUpdated(cell);
		}
	}

}
