/**
 * @author Amy Zhao
 * Defines the rules for the Fire simulation
 */

package Rules;

import Model.Cell;
import Model.Grid;
import Model.StandardCell;
import javafx.scene.paint.Color;

public class FireRules extends Rules {
	private static final int NUM_NEIGHBORS = 4;
	private static final String EMPTY = "EMPTY";
	private static final String TREE = "TREE";
	private static final String BURNING = "BURNING";
	private static final Color EMPTYCOLOR = Color.YELLOW;
	private static final Color TREECOLOR = Color.GREEN;
	private static final Color BURNINGCOLOR = Color.RED;
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
	 * Determine whether or not a tree Cell catches fire based on neighbors and its probability of catching fire.
	 * @param cell: tree Cell of interest.
	 * @param grid: Simulation grid.
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
	 * @param cell: burning Cell of interest.
	 */
	private void handleBurningCell(Cell cell) {
		cell.setNextState(EMPTY);
		addCellToBeUpdated(cell);
	}

	/**
	 * Checks if an adjacent neighbor Cell is burning.
	 * @param cell: Cell of interest.
	 * @param neighborhood: 3x3 array of Cells with the Cell of interest in the center and its neighbors surrounding it.
	 * @param grid: Simulation grid.
	 * @return true if an adjacent neighbor is burning; false if none are burning.
	 */
	private boolean neighborIsBurning(Cell cell, Cell[][] neighborhood, Grid grid) {
		int cellRow = cell.getCurRow();
		int cellCol = cell.getCurCol();
		
		if (cellCol > 0) {
			if (cellIsBurning(neighborhood[1][0])) {
				return true;
			}
		}
		if (cellRow > 0) {
			if (cellIsBurning(neighborhood[0][1])) {
				return true;
			}
		}
		if (cellCol < (grid.getNumCols() - 1)) {
			if (cellIsBurning(neighborhood[1][2])) {
				return true;
			}
		}
		if (cellRow < (grid.getNumRows() - 1)) {
			if (cellIsBurning(neighborhood[2][1])) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check if a Cell is burning.
	 * @param cell: Cell to check.
	 * @return true if Cell is burning; false otherwise.
	 */
	private boolean cellIsBurning(Cell cell) {
		return cell.getCurState().equals(BURNING);
	}
	
	/**
	 * Returns the color corresponding to the state.
	 */
	public Color getFill(String state){
		switch(state){
		case EMPTY:
			return EMPTYCOLOR;
		case TREE:
			return TREECOLOR;
		case BURNING:
			return BURNINGCOLOR;
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
		return "Fire";
	}
}
