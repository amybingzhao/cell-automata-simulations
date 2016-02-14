/**
 * @author Amy Zhao
 * Defines the variables and methods for each rules object for the Foraging Ants simulation.
 */

package Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Model.Cell;
import Model.ForagingAntsCell;
import Model.Grid;
import Model.Ant;

public class ForagingAntsRules extends Rules {
	private int numTotalAnts;
	public static final String DEFAULT_RESOURCE = "Rules/ForagingAntsRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private String DEFAULT_STATE = myResource.getString("DefaultState");
	private int NUM_NEIGHBORS = Integer.parseInt(myResource.getString("NumNeighbors"));
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
			resetAllAntHasMovedFlagsAndIncrementPheromoneRecency(grid);
		}
	}
	
	/**
	 * Resets hasMoved flags for all ants at the end of each step and increments the recency counter for pheromones of all cells.
	 * @param grid: simulation grid.
	 */
	private void resetAllAntHasMovedFlagsAndIncrementPheromoneRecency(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				ForagingAntsCell cell = (ForagingAntsCell) grid.getCell(row, col);
				cell.incrementPheromoneRecency();
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
		ForagingAntsCell[][] foragingAntsNeighborhood = convertToForagingAntsCellNeighborhood(neighborhood);
		List<Integer[]> directions = getDirectionsToCheck(ant);
		if (ant.hasFood()) {
			ant.returnToNest(cell, foragingAntsNeighborhood, directions);
		} else {
			ant.findFoodSource(cell, foragingAntsNeighborhood, directions);
			if (ant.arrivedAtFood()) {
				ant.getCurCell().loseFood();
				addCellToBeUpdated(ant.getCurCell());
				ant.setArrivedAtFood(false);
			}
		}
	}

	/**
	 * Converts a generic Cell[][] to a ForagingAntsCell[][].
	 * @param neighborhood: Cell[][] to convert.
	 * @return ForagingAntsCell[][] version of neighborhood.
	 */
	private ForagingAntsCell[][] convertToForagingAntsCellNeighborhood(Cell[][] neighborhood) {
		ForagingAntsCell[][] foragingAntsNeighborhood = new ForagingAntsCell[neighborhood.length][neighborhood[0].length];
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood[0].length; col++) {
				foragingAntsNeighborhood[row][col] = (ForagingAntsCell) neighborhood[row][col];
			}
		}
		return foragingAntsNeighborhood;
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
		if (curRow == 0 && (curCol == 0 || curCol == 2)) {
			forwardDirections.add(NORTH);
		}
		if ((curRow == 0 || curRow == 2) && curCol == 0) {
			forwardDirections.add(WEST);
		}
		if ((curRow == 0 || curRow == 2) && curCol == 2) {
			forwardDirections.add(EAST);
		}
		if (curRow == 2 && (curCol == 0 || curCol == 2)) {
			forwardDirections.add(SOUTH);
		}
		if ((curRow == 0 && curCol == 1) || (curRow == 1 && curCol == 0)) {
			forwardDirections.add(NW);
		}
		if ((curRow == 0 && curCol == 1) || (curRow == 1 && curCol == 2)) {
			forwardDirections.add(NE);
		}
		if ((curRow == 1 && curCol == 0) || (curRow == 2 && curCol == 1)) {
			forwardDirections.add(SW);
		}
		if ((curRow == 1 && curCol == 2) || (curRow == 2 && curCol == 1)) {
			forwardDirections.add(SE);
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
	public List<String> getParameters() {
		List<String> parameters = new ArrayList<String>();
		parameters.add("TotalNumAnts:" + numTotalAnts);
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
