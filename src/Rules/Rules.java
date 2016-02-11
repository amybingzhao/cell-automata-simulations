/**
 * @author Amy Zhao
 * An abstract class that specific simulation rules class are based off of
 */

package Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Cell;
import Model.Grid;
import javafx.scene.paint.Color;

public abstract class Rules {

	protected static final Color ERRORCOLOR = Color.PINK;
	private List<Cell> toBeUpdated = new ArrayList<Cell>();
	private Map<String, Integer> myStatesCount = new HashMap<String, Integer>();
	
	/**
	 * Initialize the Grid with the Cells corresponding to this simulation.
	 * @param grid: Simulation grid.
	 * @param initialStates: String 2D array with the initial states of each cell.
	 */
	public void initGrid(Grid grid, String[][] initialStates) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				Cell cell = createCell(initialStates[row][col], row, col);
				grid.addCellToGrid(row, col, cell);
				increaseStateCount(cell.getCurState());
			}
		}
	}
	
	/**
	 * Creates the type of Cell corresponding to the correct simulation.
	 * @param initialState: initial state of the Cell.
	 * @param row: row of the initial location of the Cell.
	 * @param col: column of the initial location of the Cell.
	 * @return
	 */
	protected abstract Cell createCell(String initialState, int row, int col);
	
	/**
	 * Rules for each simulation to be implemented by simulation-specific subclasses.
	 * @param cell: Cell to apply rules to.
	 * @param grid: Simulation grid.
	 */
	public abstract void applyRulesToCell(Cell cell, Grid grid);

	/**
	 * Switch cell1 and cell2's states, set their next location's for reference, and add to the list of cells to be updated.
	 * @param cell1: first cell to be swapped.
	 * @param cell2: second cell to be swapped.
	 */
	protected void switchCells(Cell cell1, Cell cell2) {
		int cell1Row = cell1.getCurRow();
		int cell1Col = cell1.getCurCol();
		String cell1State = cell1.getCurState();
		
		cell1.setNextLocation(cell2.getCurRow(), cell2.getCurCol());
		cell1.setNextState(cell2.getCurState());
		
		cell2.setNextLocation(cell1Row, cell1Col);
		cell2.setNextState(cell1State);
		
		addCellToBeUpdated(cell1);
		addCellToBeUpdated(cell2);
	}
	
	/**
	 * Gets the list of Cells that need to be updated this round of the Simulation.
	 * @return list of Cells to be updated.
	 */
	public List<Cell> getToBeUpdatedList() {
		return toBeUpdated;
	}
	
	/**
	 * Adds a Cell to the list of Cells that need to be updated this round of the Simulation.
	 * @param cell: Cell to be updated.
	 */
	public void addCellToBeUpdated(Cell cell) {
		if (!toBeUpdated.contains(cell)) {
			toBeUpdated.add(cell);
		}
	}
	
	/**
	 * Removes a Cell from the list of Cells that need to be updated this round of the Simulation.
	 * @param cell: Cell that no longer needs to be updated.
	 */
	public void removeCellToBeUpdated(Cell cell) {
		toBeUpdated.remove(cell);
	}
	
	/**
	 * Clears the "toBeUpdated" list.
	 */
	public void clearToBeUpdatedList(){
		toBeUpdated.clear();
	}
	
	/**
	 * Gets a map containing the number cells in each state.
	 * @return map mapping state to number of cells in that state.
	 */
	public Map<String, Integer> getMyStatesCount() {
		return myStatesCount;
	}
	
	/**
	 * Increases the count for the number of cells in a particular state by 1.
	 * @param state: state whose count to increase.
	 */
	protected void increaseStateCount(String state) {
		if (!myStatesCount.containsKey(state)) {
			myStatesCount.put(state, 1);
		} else {
			myStatesCount.put(state, myStatesCount.get(state) + 1);
		}
	}
	
	/**
	 * Decreases the count for the number of cells in a particular state by 1.
	 * @param state: state whose count to decrease.
	 */
	protected void decreaseStateCount(String state) {
		if (myStatesCount.containsKey(state)) {
			int num = myStatesCount.get(state);
			if (num > 0) {
				myStatesCount.put(state, num - 1);
			}
		}
	}

	/**
	 * Updates the state counts when a cell switches state.
	 * @param cell: cell that is switching states.
	 */
	public void updateStateCount(Cell cell) {
		if (cell.getNextState() != null) {
			decreaseStateCount(cell.getCurState());
			increaseStateCount(cell.getNextState());
		}
	}
	
	protected boolean isLastCellInGrid(Cell cell, Grid grid) {
		return (cell.getCurRow() == (grid.getNumRows() - 1)) && (cell.getCurCol() == (grid.getNumCols() - 1));
	}
	
	/**
	 * Generates a random integer between 0 and (max-1) for indexing into a list.
	 * @param max: size of list you're indexing into.
	 * @return an integer for the random index.
	 */
	protected int generateRandom(int max) {
		return (int) Math.round(Math.random() * (max-1));
	}
	
	/**
	 * 
	 * @param State for which the fill must be generated
	 * @return returns the fill color to be generated
	 * Currently it returns a Color, but in the future it 
	 * could also be an image.
	 */
	public abstract Color getFill(String state);
	
	/**
	 * Returns string name of the Simulation
	 */
	public abstract String toString();
	
	/**
	 * Retrieves a list of all the rule parameters
	 * @return
	 * An ArrayList of Strings containing the game parameters
	 */
	public abstract ArrayList<String> getParameters();
}
