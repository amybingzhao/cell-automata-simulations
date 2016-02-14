/**
 * @author Amy Zhao
 * @author Austin Wu
 * Defines the rules of the Segregation simulation
 */

package Rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;

public class SegregationRules extends Rules {
	public static final String DEFAULT_RESOURCE = "Rules/SegregationRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private int NUM_NEIGHBORS = Integer.parseInt(myResource.getString("NumNeighbors"));
	private String EMPTY = myResource.getString("Empty");
	private String RED = myResource.getString("Red");
	private String BLUE = myResource.getString("Blue");
	private int MY_CELL_ROW = Integer.parseInt(myResource.getString("MyCellRow"));
	private int MY_CELL_COL = Integer.parseInt(myResource.getString("MyCellCol"));
	private String DEFAULT_STATE = myResource.getString("DefaultState");
	private ArrayList<Cell> toBeMoved;
	private Queue<Cell> emptyCellList;
	private double myThreshold;
	
	public SegregationRules(double thresh) {
		myThreshold = thresh;
		toBeMoved = new ArrayList<Cell>();
		emptyCellList = new LinkedList<Cell>();
	}
	
	/**
	 * Apply the rules of the Segregation simulation to a Cell based on its state. If the cell is the last Cell in the grid,
	 * handle the dissatisfied Cells that could not be moved this simulation.
	 * @param cell: Cell to apply rules to.
	 * @param grid: Simulation grid. 
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		if (curState.equals(EMPTY)) {
			handleEmptyCell(cell);
		} else {
			handleAgentCell(cell, grid);
		}
		
		if (isLastCellInGrid(cell, grid)) {
			handleUnmovedCells();
		}
	}
	
	/**
	 * Move dissatisfied Cells to empty Cells if any are available, otherwise do not move them for this round. 
	 */
	private void handleUnmovedCells() {
		Collections.shuffle(toBeMoved);
		while (toBeMoved.size() > 0 && !emptyCellList.isEmpty()) {
			Cell agentCell = toBeMoved.get(0);
			toBeMoved.remove(0);
			Cell emptyCell = emptyCellList.poll();
			switchCells(agentCell, emptyCell);
		}
		
		while (!toBeMoved.isEmpty()) {
			toBeMoved.remove(0);
		}
		
		while (!emptyCellList.isEmpty()){
			emptyCellList.poll();
		}
	}

	/**
	 * Handle empty Cells by either moving dissatisfied agent Cells into them or by adding them to a list of empty Cells.
	 * @param cell: empty Cell that's being handled.
	 */
	private void handleEmptyCell(Cell cell) {
		emptyCellList.add(cell);
	}
	
	/**
	 * Move dissatisfied agent Cells.
	 * @param cell: agent Cell that's being handled.
	 * @param grid: Simulation grid.
	 */
	private void handleAgentCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		double percentageSame = percentageSameNeighbors(neighborhood);
		if (percentageSame < myThreshold && percentageSame != 0.0) {
			toBeMoved.add(cell);
		}
	}

	/**
	 * Checks if a cell is satisfied with its neighborhood.
	 * @param neighborhood: 3x3 array of Cells with the Cell of interest in the center and its neighbors surrounding it.
	 * @return true if satisfied; false if dissatisfied.
	 */
	private double percentageSameNeighbors(Cell[][] neighborhood) {
		String myCellState = neighborhood[MY_CELL_ROW][MY_CELL_COL].getCurState();
		int numNeighbors = countSurroundingNeighborsOfType(neighborhood, RED) + countSurroundingNeighborsOfType(neighborhood, BLUE);
		int numSameNeighbors = countSurroundingNeighborsOfType(neighborhood, myCellState);
		
		return ((double) numSameNeighbors)/((double) numNeighbors)*100;
	}
	
	/**
	 * Description of the simulation.
	 */
	public String toString(){
		return "Segregation";
	}

	/**
	 * Gets the parameters for the Segregation simulation.
	 */
	@Override
	public List<String> getParameters() {
		List<String> parameters = new ArrayList<String>();
		parameters.add("Threshold:" + myThreshold);
		return parameters;
	}
	
	/**
	 * Returns the default state for the Segregation simulation.
	 */
	@Override
	public String getDefault() {
		return DEFAULT_STATE;
	}
}
