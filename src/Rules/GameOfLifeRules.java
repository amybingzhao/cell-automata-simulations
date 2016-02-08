/**
 * @author Amy Zhao
 * @author Austin Wu
 * Defines the rules for the Game of Life simulation
 */

package Rules;

import Model.Cell;
import Model.Grid;
import Model.StandardCell;
import javafx.scene.paint.Color;

public class GameOfLifeRules extends Rules {
	private static final int NUM_NEIGHBORS = 8;
	private static final String DEAD = "DEAD";
	private static final String ALIVE = "ALIVE";
	private static final Color DEADCOLOR = Color.RED;
	private static final Color ALIVECOLOR = Color.GREEN;
	private static final int MY_CELL_ROW = 1;
	private static final int MY_CELL_COL = 1;
	
	/**
	 * Apply the rules of the Game of Life simulation to a Cell based on its state.
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		int numLiveNeighbors = countNumLiveNeighbors(neighborhood);
		
		if (curState.equals(DEAD)) {
			handleDeadCells(cell, numLiveNeighbors);
		} else {
			handleAliveCells(cell, numLiveNeighbors);
		}
	}

	/**
	 * Counts the number of live neighbors a Cell has.
	 * @param neighborhood: 3x3 array of Cells with the Cell of interest in the center and its neighbors surrounding it.
	 * @return number of live neighbors.
	 */
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

	/**
	 * If a live Cell has < 2 neighbors, it dies of underpopulation; if it has > 3 neighbors, it dies of overpopulation.
	 * @param cell: alive Cell of interest.
	 * @param numLiveNeighbors: number of live neighbors the cell has.
	 */
	private void handleAliveCells(Cell cell, int numLiveNeighbors) {
		if (numLiveNeighbors < 2 || numLiveNeighbors > 3) {
			cell.setNextState(DEAD);
			addCellToBeUpdated(cell);
		}
	}

	/**
	 * If a dead Cell has exactly 3 live neighbors, it becomes alive as though through reproduction.
	 * @param cell: dead Cell of interest.
	 * @param numLiveNeighbors: number of live neighbors the cell has.
	 */
	private void handleDeadCells(Cell cell, int numLiveNeighbors) {
		if (numLiveNeighbors == 3) {
			cell.setNextState(ALIVE);
			addCellToBeUpdated(cell);
		}
	}
	
	/**
	 * Returns the color corresponding to the state.
	 */
	public Color getFill(String state){
		switch(state){
		case DEAD:
			return DEADCOLOR;
		case ALIVE:
			return ALIVECOLOR;
		default:
			return ERRORCOLOR;
		}
	}

	/**
	 * Creates a Standard Cell for this simulation.
	 */
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new StandardCell(initialState, row, col);
	}
	
	public String toString(){
		return "Game Of Life";
	}
}
