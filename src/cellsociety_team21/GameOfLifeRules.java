package cellsociety_team21;

import javafx.scene.paint.Color;

public class GameOfLifeRules extends Rules {
	private static final int NUM_NEIGHBORS = 8;
	private static final String DEAD = "DEAD";
	private static final String ALIVE = "ALIVE";
	private static final Color DEADCOLOR = Color.RED;
	private static final Color ALIVECOLOR = Color.GREEN;
	private static final int MY_CELL_ROW = 1;
	private static final int MY_CELL_COL = 1;
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		// TODO Auto-generated method stub
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
		
		for (int r = 0; r < neighborhood.length; r++) {
			for (int c = 0; c < neighborhood[r].length; c++) {
				if (neighborhood[r][c] != null) {
					if (r != MY_CELL_ROW || c != MY_CELL_COL) {
						if (neighborhood[r][c].getCurState().equals(ALIVE)) {
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
	
	public Color getFill(String s){
		switch(s){
		case DEAD:
			return DEADCOLOR;
		case ALIVE:
			return ALIVECOLOR;
		default:
			return ERRORCOLOR;
		}
	}
	
	public String toString(){
		return "Game of Life";
	}

}
