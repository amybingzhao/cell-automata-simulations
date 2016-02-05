package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public abstract class Rules {

	protected static final Color ERRORCOLOR = Color.PINK;
	private List<Cell> toBeUpdated = new ArrayList<Cell>();
	
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
	 * Applies the simulation rules to each cell in the grid.
	 * @param grid: Simulation grid.
	 */
	public void applyRulesToGrid(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				applyRulesToCell(grid.getCell(row, col), grid);
			}
		}
		
		System.out.println(toBeUpdated + "\n");
	}
	
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
		toBeUpdated.add(cell);
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
}
