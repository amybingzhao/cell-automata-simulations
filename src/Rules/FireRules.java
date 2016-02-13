/**
 * @author Amy Zhao
 * Defines the rules for the Fire simulation
 */

package Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Model.StandardCell;

public class FireRules extends Rules {
	public static final String DEFAULT_RESOURCE = "Rules/FireRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private int NUM_NEIGHBORS = Integer.parseInt(myResource.getString("NumNeighbors"));
	private String EMPTY = myResource.getString("Empty");
	private String TREE = myResource.getString("Tree");
	private String BURNING = myResource.getString("Burning");
	private String DEFAULT_STATE = myResource.getString("DefaultState");
	private double myProbCatch;

	public FireRules(double probCatch) {
		myProbCatch = probCatch;
	}

	/**
	 * Apply the rules of the Fire simulation to a Cell based on its state.
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();

		if (curState.equals(TREE)) {
			handleTreeCell(cell, grid);
		} else if (curState.equals(BURNING)) {
			handleBurningCell(cell);
		}
	}

	/**
	 * Determine whether or not a tree Cell catches fire based on neighbors and
	 * its probability of catching fire.
	 * 
	 * @param cell:
	 *            tree Cell of interest.
	 * @param grid:
	 *            Simulation grid.
	 */
	private void handleTreeCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);

		if (neighborIsBurning(cell, neighborhood, grid)) {
			double x = Math.random();
			if (x < myProbCatch) {
				cell.setNextState(BURNING);
				addCellToBeUpdated(cell);
			}
		}
	}

	/**
	 * Burning Cell should become empty next round.
	 * 
	 * @param cell:
	 *            burning Cell of interest.
	 */
	private void handleBurningCell(Cell cell) {
		cell.setNextState(EMPTY);
		addCellToBeUpdated(cell);
	}

	/**
	 * Checks if an adjacent neighbor Cell is burning.
	 * 
	 * @param cell:
	 *            Cell of interest.
	 * @param neighborhood:
	 *            3x3 array of Cells with the Cell of interest in the center and
	 *            its neighbors surrounding it.
	 * @param grid:
	 *            Simulation grid.
	 * @return true if an adjacent neighbor is burning; false if none are
	 *         burning.
	 */
	private boolean neighborIsBurning(Cell cell, Cell[][] neighborhood, Grid grid) {

		if (cellIsBurning(neighborhood[1][0])) {
			return true;
		}
		if (cellIsBurning(neighborhood[0][1])) {
			return true;
		}
		if (cellIsBurning(neighborhood[1][2])) {
			return true;
		}
		if (cellIsBurning(neighborhood[2][1])) {
			return true;
		}

		return false;
	}

	/**
	 * Check if a Cell is burning.
	 * 
	 * @param cell:
	 *            Cell to check.
	 * @return true if Cell is burning; false otherwise.
	 */
	private boolean cellIsBurning(Cell cell) {
		if (cell != null) {
			return cell.getCurState().equals(BURNING);
		} else {
			return false;
		}
	}

	/**
	 * Creates a Standard Cell for this simulation.
	 */
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new StandardCell(initialState, row, col);
	}

	/**
	 * Description of the simulation.
	 */
	public String toString() {
		return "Fire";
	}

	/**
	 * Gets the parameters for the Fire simulation.
	 */
	@Override
	public List<String> getParameters() {
		List<String> parameters = new ArrayList<String>();
		parameters.add("ProbCatch:" + myProbCatch);
		return parameters;
	}

	/**
	 * Returns default state for the Fire simulation.
	 */
	@Override
	protected String getDefault() {
		return DEFAULT_STATE;
	}

}
