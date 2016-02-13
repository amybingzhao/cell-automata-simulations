package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class SugarScapeAgent {
	private int mySugar;
	private int mySugarMetabolism;
	private int myVision;
	private int myRow;
	private int myCol;	
	private boolean hasMoved;
	private static final int INIT_SUGAR_MIN = 5;
	private static final String ROW = "ROW";
	private static final String COL = "COL";
	
	/**
	 * Constructs a sugar scape agent with an initial amount of sugar, a random metabolism within the limit of the simulation,
	 * a random vision within the limit of the simulation, and a row and column of the cell that the agent is on.
	 * @param initSugar: initial amount of sugar the agent has.
	 * @param metabolism: amount of sugar consumed by agent per move.
	 * @param vision: number of cells it can see in any given cardinal direction.
	 * @param row: row of the cell the agent is on.
	 * @param col: column of the cell the agent is on.
	 */
	public SugarScapeAgent(int initSugar, int metabolism, int vision, int row, int col) {
		if (initSugar > INIT_SUGAR_MIN) {
			mySugar = initSugar;
		} else {
			mySugar = INIT_SUGAR_MIN;
		}
		mySugarMetabolism = metabolism;
		myVision = vision;
		myRow = row;
		myCol = col;
		hasMoved = false;
	}
	
	/**
	 * Finds the closest highest-sugar neighbor cell in sight of the agent.
	 * @param grid: simulation grid.
	 * @return a SugarScapeCell that the agent will move to.
	 */
	public SugarScapeCell findNextPatch(Grid grid) {
		List<SugarScapeCell> neighbors = getViableNeighbors(grid);
		SugarScapeCell nextPatch = compareViableNeighbors(neighbors, grid);
//		System.out.println("my loc: (" + myRow + ", " + myCol + ")");
	//	System.out.println("next: (" + nextPatch.getCurRow() + ", " + nextPatch.getCurCol() + ")");
		return nextPatch;
	}
	
	/**
	 * Gets the vision of the agent.
	 * @return integer representing the vision of the agent.
	 */
	public int getVision() {
		return myVision;
	}
	
	/**
	 * Gets the metabolism of the agent.
	 * @return integer representing the sugar metabolism of the agent.
	 */
	public int getMetabolism() {
		return mySugarMetabolism;
	}
	
	/**
	 * Gets the row of the cell that the agent is in.
	 * @return row of the cell that the agent is in.
	 */
	public int getRow() {
		return myRow;
	}
	
	/**
	 * Gets the column of the cell that the agent is in.
	 * @return column of the cell that the agent is in.
	 */
	public int getCol() {
		return myCol;
	}
	
	/**
	 * Gets the empty neighbor cells that're within the agent's vision (viable neighbors to move to).
	 * @param grid: simulation grid.
	 * @return a list of neighboring SugarScapeCells that the agent can move to.
	 */
	protected List<SugarScapeCell> getViableNeighbors(Grid grid) {
		List<SugarScapeCell> neighbors = new ArrayList<SugarScapeCell>();
		checkNeighborsInOneDirection(neighbors, 1, ROW, grid);
		checkNeighborsInOneDirection(neighbors, -1, ROW, grid);
		checkNeighborsInOneDirection(neighbors, 1, COL, grid);
		checkNeighborsInOneDirection(neighbors, -1, COL, grid);
		return neighbors;
	}
	

	protected void checkNeighborsInOneDirection(List<SugarScapeCell> neighbors, int offset, String rowOrCol, Grid grid) {
		for (int distance = 1; distance <= myVision; distance++) {
			int row = myRow;
			int col = myCol;
			if (rowOrCol.equals(ROW)) {
				row = myRow + offset * distance;
			} else {
				col = myCol + offset * distance;
			}
			if (viableNeighbor(row, myCol, grid)) {
				neighbors.add((SugarScapeCell) grid.getCell(row, myCol));
			}
		}
	}
	
	/**
	 * Checks if a neighbor cell is a viable candidate to move to.
	 * @param row: row that the neighbor is in.
	 * @param col: column that the neighbor is in.
	 * @param grid: simulation grid.
	 * @return true if the neighbor can be moved to; false otherwise.
	 */
	private boolean viableNeighbor(int row, int col, Grid grid) {
		if (grid.inBounds(row, col)) {
			SugarScapeCell neighbor = (SugarScapeCell) grid.getCell(row, col);
			if (!neighbor.hasAgent()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Compares the viable neighbors to determine the closest, highest-sugar neighbor.
	 * @param neighbors: list of empty neighbors that the agent can move to.
	 * @param grid: simulation grid.
	 * @return closest, highest-sugar neighboring SugarScapeCell.
	 */
	private SugarScapeCell compareViableNeighbors(List<SugarScapeCell> neighbors, Grid grid) {
		SugarScapeCell patchToMoveTo = null;
	//	System.out.println(neighbors);
		List<SugarScapeCell> highestSugarNeighbors = getHighestSugar(neighbors);
	//	System.out.println("size: " + highestSugarNeighbors.size());
		if (highestSugarNeighbors.size() > 0) {
			patchToMoveTo = getClosestNeighbor(highestSugarNeighbors, grid);
		}
		
		return patchToMoveTo;
	}
	
	/**
	 * Finds the highest-sugar cells in a list.
	 * @param neighbors: neighboring cells that can be moved to.
	 * @return a list of neighboring cells with the highest-sugar amount.
	 */
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
	
	/**
	 * Finds the closest cell in a list.
	 * @param neighbors: neighboring cells that can be moved to.
	 * @return the closest cell in the list.
	 */
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
	
	/**
	 * Calculates the relative distance between two cells. 
	 * @param cell: cell the current agent is in.
	 * @param neighbor: neighboring cell.
	 * @return integer representing the relative distance between the two cells.
	 */
	private int getDist(SugarScapeCell cell, SugarScapeCell neighbor) {
		return Math.abs((cell.getCurRow() - neighbor.getCurRow()) + (cell.getCurCol() - neighbor.getCurCol()));
	}
	
	/**
	 * Moves an agent to another patch and finishes its turn.
	 * @param curPatch: cell the agent is currently on.
	 * @param nextPatch: cell the agents wants to move to.
	 */
	public void moveToPatch(SugarScapeCell curPatch, SugarScapeCell nextPatch) {
		curPatch.removeAgent();
		nextPatch.setAgent(this);
		hasMoved = true;
		int sugarToConsume = nextPatch.consumeSugar();
		mySugar += sugarToConsume;
		
		mySugar -= mySugarMetabolism;
		if (mySugar <= 0) {
			agentDies(nextPatch);
		}
	}
	
	/**
	 * Sets the agent's sugar amount.
	 * @param amount: sugar amount.
	 */
	public void setSugar(int amount) {
		mySugar = amount;
	}
	
	/**
	 * Removes an agent if it dies.
	 * @param nextPatch: cell the agent tried to move to.
	 */
	public void agentDies(SugarScapeCell nextPatch) {
		nextPatch.removeAgent();
	}
	
	/**
	 * Gets the amount of sugar the agent has.
	 * @return amount of sugar the agent has.
	 */
	public int getMySugarAmount() {
		return mySugar;
	}
	
	/**
	 * Sets the location of the agent.
	 * @param row: row of the cell the agent is in.
	 * @param col: column of the cell the agent is in.
	 */
	public void setLocation(int row, int col) {
		myRow = row;
		myCol = col;
	}
	
	/**
	 * Checks if the agent has moved this turn yet.
	 * @return true if the agent has not yet moved this turn; false otherwise.
	 */
	public boolean hasNotMoved() {
		return hasMoved == false;
	}
}
