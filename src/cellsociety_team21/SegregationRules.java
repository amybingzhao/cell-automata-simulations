package cellsociety_team21;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SegregationRules extends Rules {

	private static final int NUM_NEIGHBORS = 8;
	private static final String EMPTY = "EMPTY";
	private static final String RED = "RED";
	private static final String BLUE = "BLUE";
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
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		
		if (curState.equals(EMPTY)) {
			handleEmptyCell(cell);
		} else {
			handleAgentCell(cell, grid);
			if ((cell.getRow() == (grid.getNumRows() - 1)) && (cell.getCol() == (grid.getNumCols() - 1))) {
				handleUnmovedCells();
			}
		}
	}
	
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

	private void handleEmptyCell(Cell cell) {
		if (toBeMoved.size() == 0) {
			emptyCellList.add(cell);
		} else {
			switchCells(cell, toBeMoved.poll());
		}
	}
	
	private void handleAgentCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getRow(), cell.getCol(), NUM_NEIGHBORS);
		if (!satisfiedWithNeighbors (neighborhood)) {
			cell.setNextState(EMPTY);
			toBeMoved.add(cell);
		}
	}

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
}
