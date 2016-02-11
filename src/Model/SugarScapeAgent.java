package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class SugarScapeAgent {
	private int mySugar;
	private int mySugarMetabolism;
	private int myVision;
	private int myRow;
	private int myCol;
	private static final int NUM_DIRECTIONS = 4;
		
	public SugarScapeCell findNextPatch(Grid grid) {
		List<SugarScapeCell> neighbors = getViableNeighbors(grid);
		SugarScapeCell nextPatch = compareViableNeighbors(neighbors, grid);
		return nextPatch;
	}
	
	public void moveToPatch(SugarScapeCell nextCell) {
		
	}
	
	private List<SugarScapeCell> getViableNeighbors(Grid grid) {
		List<SugarScapeCell> neighbors = new ArrayList<SugarScapeCell>();
		int row;
		int col;
		
		// up
		for (int distance = 1; distance <= myVision; distance++) {
			row = myRow;
			col = myCol - distance;
			if (viableNeighbor(row, col, grid)) {
				neighbors.add((SugarScapeCell) grid.getCell(row, col));
			}
		}
		
		// down
		for (int distance = 1; distance <= myVision; distance++) {
			row = myRow;
			col = myCol + distance;
			if (viableNeighbor(row, col, grid)) {
				neighbors.add((SugarScapeCell) grid.getCell(row, col));
			}
		}
		
		// left
		for (int distance = 1; distance <= myVision; distance++) {
			row = myRow - distance;
			col = myCol;
			if (viableNeighbor(row, col, grid)) {
				neighbors.add((SugarScapeCell) grid.getCell(row, col));
			}
		}
		
		//right
		for (int distance = 1; distance <= myVision; distance++) {
			row = myRow + distance;
			col = myCol;
			if (viableNeighbor(row, col, grid)) {
				neighbors.add((SugarScapeCell) grid.getCell(row, col));
			}
		}
		
		return neighbors;
	}
	
	private boolean inBounds(int row, int col, Grid grid) {
		return (row < grid.getNumRows() && row > 0 && col < grid.getNumCols() && col < 0);
	}
	
	private boolean viableNeighbor(int row, int col, Grid grid) {
		if (inBounds(row, col, grid)) {
			SugarScapeCell neighbor = (SugarScapeCell) grid.getCell(row, col);
			if (!neighbor.hasAgent()) {
				return true;
			}
		}
		
		return false;
	}
	
	private SugarScapeCell compareViableNeighbors(List<SugarScapeCell> neighbors, Grid grid) {
		SugarScapeCell patchToMoveTo = null;
		List<SugarScapeCell> highestSugarNeighbors = getHighestSugar(neighbors);
		
		if (highestSugarNeighbors.size() > 0) {
			patchToMoveTo = getClosestNeighbor(highestSugarNeighbors, grid);
		}
		
		return patchToMoveTo;
	}
	
	private List<SugarScapeCell> getHighestSugar(List<SugarScapeCell> neighbors) {
		List<SugarScapeCell> highestSugarNeighbors = new ArrayList<SugarScapeCell>();
		int maxSugar = 0;
		
		for (int i = 0; i < neighbors.size(); i++) {
			SugarScapeCell neighbor = neighbors.get(i);
			if (neighbor.getMySugarAmount() > maxSugar) {
				maxSugar = neighbor.getMySugarAmount();
			}
		}
		
		for (int j = 0; j < neighbors.size(); j++) {
			if (neighbors.get(j).getMySugarAmount() == maxSugar) {
				highestSugarNeighbors.add(neighbors.get(j));
			}
		}
		
		return highestSugarNeighbors;
	}
	
	private SugarScapeCell getClosestNeighbor(List<SugarScapeCell> neighbors, Grid grid) {
		SugarScapeCell curCell = (SugarScapeCell) grid.getCell(myRow, myCol);
		SugarScapeCell closestNeighbor = neighbors.get(0);
		int closestDist = getDist(curCell, closestNeighbor);
		
		for (int i = 1; i < neighbors.size(); i++) {
			if (getDist(curCell, neighbors.get(i)) < closestDist) {
				closestNeighbor = neighbors.get(i);
				closestDist = getDist(curCell, neighbors.get(i));
			}
		}
		
		return closestNeighbor;
	}
	
	private int getDist(SugarScapeCell cell, SugarScapeCell neighbor) {
		return Math.abs((cell.getCurRow() - neighbor.getCurRow()) + (cell.getCurCol() - neighbor.getCurCol()));
	}
	
	public void moveToPatch(SugarScapeCell curPatch, SugarScapeCell nextPatch) {
		curPatch.removeAgent();
		nextPatch.setAgent(this);
		int sugarToConsume = nextPatch.consumeSugar();
		mySugar += sugarToConsume;
		
		mySugar -= mySugarMetabolism;
		if (mySugar <= 0) {
			agentDies(nextPatch);
		}
	}
	
	private void agentDies(SugarScapeCell nextPatch) {
		nextPatch.removeAgent();
	}
}
