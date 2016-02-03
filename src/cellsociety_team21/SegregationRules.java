package cellsociety_team21;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javafx.scene.paint.Color;

public class SegregationRules extends Rules {

	private static final int NUM_NEIGHBORS = 8;
	private static final String EMPTY = "EMPTY";
	private static final String RED = "RED";
	private static final String BLUE = "BLUE";
	private static final Color REDCOLOR = Color.RED;
	private static final Color BLUECOLOR = Color.BLUE;
	private static final int MY_CELL_ROW = 1;
	private static final int MY_CELL_COL = 1;
	private Queue<Cell> toBeMoved;
	private Queue<Cell> emptyCellList;
	private double myThreshold;
	
	public SegregationRules(double thresh) {
		myThreshold = thresh;
		toBeMoved = new LinkedList<Cell>();
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
			if ((cell.getCurRow() == (grid.getNumRows() - 1)) && (cell.getCurCol() == (grid.getNumCols() - 1))) {
				handleUnmovedCells();
			}
		}
	}
	
	/**
	 * Move dissatisfied Cells to empty Cells if any are available, otherwise do not move them for this round. 
	 */
	private void handleUnmovedCells() {
		while (!toBeMoved.isEmpty() && !emptyCellList.isEmpty()) {
			Cell agentCell = toBeMoved.poll();
			Cell emptyCell = emptyCellList.poll();
			switchCells(agentCell, emptyCell);
		}
		
		while (!toBeMoved.isEmpty()) {
			Cell cell = toBeMoved.poll();
			cell.setNextState(null);
			removeCellToBeUpdated(cell);
		}
	}

	/**
	 * Handle empty Cells by either moving dissatisfied agent Cells into them or by adding them to a list of empty Cells.
	 * @param cell: empty Cell that's being handled.
	 */
	private void handleEmptyCell(Cell cell) {
		if (toBeMoved.size() == 0) {
			emptyCellList.add(cell);
		} else {
			switchCells(cell, toBeMoved.poll());
		}
	}
	
	/**
	 * Move dissatisfied agent Cells.
	 * @param cell: agent Cell that's being handled.
	 * @param grid: Simulation grid.
	 */
	private void handleAgentCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		if (!satisfiedWithNeighbors (neighborhood)) {
			cell.setNextState(EMPTY);
			toBeMoved.add(cell);
		}
	}

	/**
	 * Checks if a cell is satisfied with its neighborhood.
	 * @param neighborhood: 3x3 array of Cells with the Cell of interest in the center and its neighbors surrounding it.
	 * @return true if satisfied; false if dissatisfied.
	 */
	private boolean satisfiedWithNeighbors(Cell[][] neighborhood) {
		int numNeighbors = 0;
		int numSameNeighbors = 0;
		String myCellState = neighborhood[MY_CELL_ROW][MY_CELL_COL].getCurState();
		
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood[row].length; col++) {
				if (neighborhood[row][col] != null) {
					if (row != MY_CELL_ROW || col != MY_CELL_COL) {
						String neighborState = neighborhood[row][col].getCurState();
						if (!neighborState.equals(EMPTY)) {
							numNeighbors++;
							if (neighborhood[row][col].getCurState().equals(myCellState)) {
								numSameNeighbors++;
							}
						}
					}
				}
			}
		}
		System.out.println("numNeighbors: " + numNeighbors);
		System.out.println("numSameNeighbors: " + numSameNeighbors);

		System.out.println(((((double) numSameNeighbors)/((double) numNeighbors) >= myThreshold) || numNeighbors == 0));
		System.out.println();
		
		return ((((double) numSameNeighbors)/((double) numNeighbors) >= myThreshold) || numNeighbors == 0);
	}
	public Color getFill(String s){
		switch(s){
		case BLUE:
			return BLUECOLOR;
		case RED:
			return REDCOLOR;
		default:
			return ERRORCOLOR;
		}
	}
	
	public String toString(){
		return "Segregation";
	}
}
