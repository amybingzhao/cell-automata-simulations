package Rules;

import java.util.ArrayList;
import java.util.List;
import Model.Cell;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.paint.Color;
import Model.Ant;

public class ForagingAntsRules extends Rules {
	private int numTotalAnts;
	private static final String HOME = "HOME";
	private static final String GROUND = "GROUND";
	private static final String OBSTACLE = "OBSTACLE";
	private static final String FOOD = "FOOD";
	private static final String DEFAULT_STATE = GROUND;
	private static final int NUM_NEIGHBORS = 8;
	private static final Integer[] NORTH = new Integer[]{0, 1};
	private static final Integer[] SOUTH = new Integer[]{2, 1};
	private static final Integer[] WEST = new Integer[]{1, 0};
	private static final Integer[] EAST = new Integer[]{1, 2};
	private static final Integer[] NW = new Integer[]{0, 0};
	private static final Integer[] NE = new Integer[]{0, 2};
	private static final Integer[] SW = new Integer[]{2, 0};
	private static final Integer[] SE = new Integer[]{2, 2};
	private List<Integer[]> myDirections;
	
	
	public ForagingAntsRules(int numAnts) {
		numTotalAnts = numAnts;
		initDirections();
	}
	
	/**
	 * Initializes an array of the possible directions an ant can face.
	 */
	private void initDirections() {
		myDirections = new ArrayList<Integer[]>();
		myDirections.add(NORTH);
		myDirections.add(SOUTH);
		myDirections.add(WEST);
		myDirections.add(EAST);
		myDirections.add(NE);
		myDirections.add(NW);
		myDirections.add(SE);
		myDirections.add(SW);
	}

	/**
	 * Creates a ForagingAntsCell for use by this simulation.
	 */
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new ForagingAntsCell(initialState, row, col, numTotalAnts);
	}

	/**
	 * Applies rules to ants in each cell according to the rules of the Foraging Ants simulation.
	 * @param cell: cell that ants occupy.
	 * @param grid: simulation grid.
	 */
	public void applyRulesToCell(ForagingAntsCell cell, Grid grid) {
		if (cell.getNumAnts() > 0) {
			List<Ant> ants = cell.getAnts();

			while(ants.size() > 0) {
				if (!ants.get(0).hasMovedThisTurn()) {
					handleAnt(ants.get(0), cell, grid);
				} else {
					break;
				}
			}
		}
		
		if (isLastCellInGrid(cell, grid)) {
			resetAllAntHasMovedFlags(grid);
		}
	}
	
	/**
	 * Resets hasMoved flags for all ants at the end of each step.
	 * @param grid: simulation grid.
	 */
	private void resetAllAntHasMovedFlags(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				ForagingAntsCell cell = (ForagingAntsCell) grid.getCell(row, col);
				if (cell.getNumAnts() > 0) {
					List<Ant> ants = cell.getAnts();
					for (int i = 0; i < ants.size(); i++) {
						ants.get(i).setHasMovedThisTurn(false);
					}
				}
			}
		}
	}
	
	/**
	 * Handle movement of a single ant.
	 * @param ant: ant to be moved.
	 * @param cell: cell that ant occupies.
	 * @param grid: simulation grid.
	 */
	private void handleAnt(Ant ant, ForagingAntsCell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		List<Integer[]> directions = getDirectionsToCheck(ant);
		if (ant.hasFood()) {
			ant.returnToNest(cell, neighborhood, directions);
		} else {
			ant.findFoodSource(cell, neighborhood, directions);
		}
	}

	/**
	 * Gets the directions that the ant should check with the forward directions at the front end of the list.
	 * @param ant: ant being handled currently.
	 * @return directions that ant should check in the order it should check them.
	 */
	private List<Integer[]> getDirectionsToCheck(Ant ant) {
		List<Integer[]> directions = new ArrayList<Integer[]>();
		directions.addAll(getForwardDirections(ant.getDirection()));

		for (int i = 0; i < myDirections.size(); i++) {
			if (!directions.contains(myDirections.get(i))) {
				directions.add(myDirections.get(i));
			}
		}
		
		return directions;
	}
	
	/**
	 * Gets the forward directions based on the ant's current direction.
	 * @param curDirection: ant's current direction.
	 * @return list of forward directions.
	 */
	private List<Integer[]> getForwardDirections(Integer[] curDirection) {
		int curRow = curDirection[0];
		int curCol = curDirection[1];

		List<Integer[]> forwardDirections = new ArrayList<Integer[]>();
		forwardDirections.add(curDirection);
		
		if (curRow == 0) {
			if (curCol == 0) {
				forwardDirections.add(NORTH);
				forwardDirections.add(WEST);
			} else if (curCol == 1) {
				forwardDirections.add(NW);
				forwardDirections.add(NE);
			} else if (curCol == 2) {
				forwardDirections.add(NORTH);
				forwardDirections.add(EAST);
			}
		} else if (curRow == 1) {
			if (curCol == 0) {
				forwardDirections.add(NW);
				forwardDirections.add(SW);
			} else if (curCol == 2) {
				forwardDirections.add(NE);
				forwardDirections.add(SE);
			}
		} else if (curRow == 2) {
			if (curCol == 0) {
				forwardDirections.add(WEST);
				forwardDirections.add(SOUTH);
			} else if (curCol == 1) {
				forwardDirections.add(SW);
				forwardDirections.add(SE);
			} else if (curCol == 2) {
				forwardDirections.add(EAST);
				forwardDirections.add(SOUTH);
			}
		}
		
		return forwardDirections;
	}
	
	/**
	 * Applies rules of this specific simulation to cells.
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		applyRulesToCell((ForagingAntsCell) cell, grid);
	}

	/**
	 * Description of the simulation rules.
	 */
	@Override
	public String toString() {
		return "Foraging Ants";
	}

	/**
	 * Gets the parameters for this simulation.
	 */
	@Override
	public ArrayList<String> getParameters() {
		ArrayList<String> parameters = new ArrayList<String>();
		return parameters;
	}
	
	/**
	 * Default state for a cell in this simulation.
	 */
	@Override
	protected String getDefault() {
		return DEFAULT_STATE;
	}
}
