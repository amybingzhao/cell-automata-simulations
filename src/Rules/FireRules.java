/**
 * @author Amy Zhao
 * Defines the rules for the Fire simulation
 */

package Rules;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Cell;
import Model.Grid;
import Model.StandardCell;
import javafx.scene.paint.Color;

public class FireRules extends Rules {
	private static final int NUM_NEIGHBORS = 4;
	private static final String EMPTY = "EMPTY";
	private static final String TREE = "TREE";
	private static final String BURNING = "BURNING";
	private static final String DEFAULT_STATE = TREE;
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
		System.out.println("check for burning cell for cell at: (" + cell.getCurRow() + ", " + cell.getCurCol() + ")");
		System.out.println(neighborIsBurning(cell, neighborhood, grid));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(neighborhood[i][j]);
			}
			System.out.println();
		}
		System.out.println();

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
			System.out.println(cell.getCurState());
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

	public String toString() {
		return "Fire";
	}

	@Override
	public ArrayList<String> getParameters() {
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("ProbCatch:" + myProbCatch);
		return parameters;
	}

	@Override
	protected String getDefault() {
		return DEFAULT_STATE;
	}

}
