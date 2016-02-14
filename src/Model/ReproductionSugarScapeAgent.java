/**
 * @author Amy Zhao
 * Defines the variables and methods for a sugar scape agent for the Sugar Scape simulation's Reproduction preset.
 */

package Model;

import java.util.ArrayList;
import java.util.List;

public class ReproductionSugarScapeAgent extends SugarScapeAgent {
	private int myGender;
	private int myAge;
	private int myMaxAge;
	private int myFertileMin;
	private int myFertileMax;
	private int myInitSugar;
	private List<ReproductionSugarScapeAgent> myChildren;
	private static final int NUM_NEIGHBORS = 4;
	
	/**
	 * Constructs a sugar scape agent for the reproduction preset.
	 * @param initSugar: initial amount of sugar.
	 * @param metabolism: agent's metabolism.
	 * @param vision: agent's vision.
	 * @param row: row of the cell the agent is in.
	 * @param col: column of the cell the agent is in.
	 * @param gender: 0 for one gender, 1 for the other.
	 * @param maxAge: maximum age the agent can reach.
	 * @param fertileMin: minimum age at which the agent becomes fertile.
	 * @param fertileMax: maximum age at which the agent is still fertile.
	 */
	public ReproductionSugarScapeAgent(int initSugar, int metabolism, int vision, int row, int col, int gender, int maxAge, int fertileMin, int fertileMax) {
		super(initSugar, metabolism, vision, row, col);
		myInitSugar = initSugar;
		myChildren = new ArrayList<ReproductionSugarScapeAgent>();
		myAge = 0;
		myGender = gender;
		myMaxAge = maxAge;
		myFertileMin = fertileMin;
		myFertileMax = fertileMax;
	}
	
	/**
	 * Checks if the agent is fertile based on its age and amount of sugar.
	 * @return true if the agent is fertile; false otherwise.
	 */
	public boolean isFertile() {
		return (getMySugarAmount() >= myInitSugar) && (myAge >= myFertileMin) && (myAge <= myFertileMax);
	}
	
	/**
	 * Tries to find a fertile mate amongst the agent's visible neighbors. 
	 * @param grid: simulation grid
	 * @return a reproduction sugar scape agent to mate with if there is one, else return null.
	 */
	public ReproductionSugarScapeAgent findMate(Grid grid) {
		if (this.isFertile()) {
			List<SugarScapeCell> neighbors = getVisibleNeighbors(grid, false);
			while (!neighbors.isEmpty()) {
				int rand = (int) Math.round(Math.random() * (neighbors.size()-1));
				SugarScapeCell neighbor = neighbors.get(rand);
				if (neighbor.hasAgent()) {
					ReproductionSugarScapeAgent neighborAgent = (ReproductionSugarScapeAgent) neighbor.getAgent();
					if (neighborAgent.isFertile() && neighborAgent.isOppositeGender(this) && (this.hasEmptyNeighbor(grid) || neighborAgent.hasEmptyNeighbor(grid))) {
						return neighborAgent;
					}
				}
				neighbors.remove(neighbor);
			}
		}
		return null;
	}
	
	/**
	 * Increases the age of the agent by 1.
	 */
	public void increaseAge() {
		myAge++;
	}
	
	/**
	 * Checks if the agent is older than its maximum allowed age.
	 * @return true if the agent is older than max age; false otherwise.
	 */
	public boolean isTooOld() {
		return myAge > myMaxAge;
	}
	
	/**
	 * Splits the agent's sugar and its mate's sugar in half and adds the halves together.
	 * @param neighbor: neighbor cell to mate with.
	 * @return sum of half of each cell's sugar.
	 */
	// check for off by 1 b/c odd
	public int splitSugar(ReproductionSugarScapeAgent neighbor) {
		setSugar(getMySugarAmount()/2);
		neighbor.setSugar(neighbor.getMySugarAmount()/2);
		return getMySugarAmount()/2 + neighbor.getMySugarAmount()/2;
	}
	
	/**
	 * Checks if an adjacent cell is empty.
	 * @param grid: simulation grid.
	 * @return true if an adjacent cell is empty; false otherwise.
	 */
	private boolean hasEmptyNeighbor(Grid grid) {
		if (getEmptyNeighbor(grid) != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets an adjacent cell that is empty.
	 * @param grid: simulation grid.
	 * @return adjacent sugar scape cell that has no agent.
	 */
	public SugarScapeCell getEmptyNeighbor(Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(getRow(), getCol(), NUM_NEIGHBORS);
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood.length; col++) {
				SugarScapeCell neighbor = (SugarScapeCell) neighborhood[row][col];
				if (neighbor != null && !neighbor.hasAgent()) {
					return neighbor;
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if another agent is of the opposite gender.
	 * @param agent: agent to check.
	 * @return true if of the opposite gender; false otherwise.
	 */
	private boolean isOppositeGender(ReproductionSugarScapeAgent agent) {
		return agent.getGender() != myGender;
	}
	
	/**
	 * Gets agent's gender.
	 * @return agent's gender.
	 */
	private int getGender() {
		return myGender;
	}
	
	/**
	 * Description of agent.
	 */
	public String toString() {
		return "my vision: " + getVision();
	}
}
