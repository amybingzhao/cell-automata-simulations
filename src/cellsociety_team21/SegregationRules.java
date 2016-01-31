package cellsociety_team21;

import java.util.ArrayList;
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
	private List<Cell> emptyCellList;
	private double myThreshold;
	
	public SegregationRules(double thresh) {
		myThreshold = thresh;
		toBeMoved = new PriorityQueue<Cell>();
		emptyCellList = new ArrayList<Cell>();
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		
		if (curState.equals(EMPTY)) {
			handleEmptyCell(cell);
		} else {
			handleAgentCell(cell, grid);
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
	
	private void switchCells(Cell emptyCell, Cell agentCell) {
		int emptyRow = emptyCell.getRow();
		int emptyCol = emptyCell.getCol();
		String agentState = agentCell.getCurState();
		
		emptyCell.setLocation(agentCell.getRow(), agentCell.getCol());
		emptyCell.setNextState(agentState);
		
		agentCell.setLocation(emptyRow, emptyCol);
		agentCell.setNextState(EMPTY);
	}

	private boolean satisfiedWithNeighbors(Cell[][] neighborhood) {
		int numNeighbors = 0;
		int numSameNeighbors = 0;
		String myCellState = neighborhood[MY_CELL_ROW][MY_CELL_COL].getCurState();
		
		for (int r = 0; r < neighborhood.length; r++) {
			for (int c = 0; c < neighborhood[r].length; c++) {
				if (r != MY_CELL_ROW || c != MY_CELL_COL) {
					numNeighbors++;
					if (neighborhood[r][c].getCurState().equals(myCellState)) {
						numSameNeighbors++;
					}
				}
			}
		}
		return (((double) numSameNeighbors)/((double) numNeighbors) >= myThreshold);
	}
	
	
	
}
