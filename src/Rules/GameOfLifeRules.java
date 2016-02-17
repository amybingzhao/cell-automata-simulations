// This entire file is part of my masterpiece.
// Amy Zhao (abz3)

// Subclass to demonstrate use of the abstract Rules base class.

/**
 * @author Amy Zhao
 * @author Austin Wu
 * Defines the rules for the Game of Life simulation
 */

package Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;

public class GameOfLifeRules extends Rules {
	public static final String DEFAULT_RESOURCE = "Rules/GameOfLifeRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private int numNeighborsToCheck = Integer.parseInt(myResource.getString("NumNeighbors"));
	private String DEAD = myResource.getString("Dead");
	private String ALIVE = myResource.getString("Alive");
	private List<Integer> numAllowableLiveNeighbors = new ArrayList<Integer>();
	private int numNeighborsNeededToReproduce = Integer.parseInt(myResource.getString("NumNeighborsNeededToReproduce"));
	
	public GameOfLifeRules() {
		String[] allowableNeighbors = myResource.getString("NumAllowableLiveNeighbors").split(",");
		for (int i = 0; i < allowableNeighbors.length; i++) {
			numAllowableLiveNeighbors.add(Integer.parseInt(allowableNeighbors[i]));
		}	
	}
	
	/**
	 * Apply the rules of the Game of Life simulation to a Cell based on its state.
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), numNeighborsToCheck);
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
		return countSurroundingNeighborsOfType(neighborhood, ALIVE);
	}

	/**
	 * If a live Cell has < 2 neighbors, it dies of underpopulation; if it has > 3 neighbors, it dies of overpopulation.
	 * @param cell: alive Cell of interest.
	 * @param numLiveNeighbors: number of live neighbors the cell has.
	 */
	private void handleAliveCells(Cell cell, int numLiveNeighbors) {
		if (!numAllowableLiveNeighbors.contains(numLiveNeighbors)) {
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
		if (numLiveNeighbors == numNeighborsNeededToReproduce) {
			cell.setNextState(ALIVE);
			addCellToBeUpdated(cell);
		}
	}

	/**
	 * Description of Game of Life simulation.
	 */
	public String toString(){
		return "Game Of Life: simulates effects of over- and underpopulation.";
	}

	/**
	 * Parameters for Game of Life simulation.
	 */
	@Override
	public List<String> getParameters() {
		List<String> parameters = new ArrayList<String>();
		return parameters;
	}	
}
