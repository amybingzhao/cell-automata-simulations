/**
 * @author Amy Zhao
 * Defines the variables and methods for each sugar scape cell object
 */

package Model;

public class SugarScapeCell extends Cell {
	private int mySugar;
	private int myMaxSugarCapacity;
	private SugarScapeAgent myAgent;
	
	public SugarScapeCell(String initialState, int row, int col, int maxSugar, SugarScapeAgent agent) {
		super(initialState, row, col);
		mySugar = maxSugar;
		myMaxSugarCapacity = maxSugar;
		myAgent = agent;
	}
	
	/**
	 * Checks if cell has an agent on it.
	 * @return true if cell has an agent on it; false otherwise.
	 */
	public boolean hasAgent() {
		return myAgent != null; 
	}
	
	/**
	 * Gets the agent if the cell has one.
	 * @return agent on cell.
	 */
	public SugarScapeAgent getAgent() {
		if (hasAgent()) {
			return myAgent;
		} else {
			return null;
		}
	}
	
	/**
	 * Sets an agent to the cell.
	 * @param agent: agent to add to the cell.
	 */
	public void setAgent(SugarScapeAgent agent) {
		myAgent = agent;
		agent.setLocation(getCurRow(), getCurCol());
	}
	
	/**
	 * Removes an agent that has left the cell.
	 */
	public void removeAgent() {
		myAgent = null;
	}
	
	/**
	 * Gets the amount of sugar that an agent who has moved to this cell can consume.
	 * @return: amount of sugar on this cell.
	 */
	public int consumeSugar() {
		int sugar = mySugar;
		mySugar = 0;
		return sugar;
	}
	
	/**
	 * Adds sugar to this cell.
	 * @param sugarGrowBackRate: amount of sugar to add to this cell.
	 */
	public void addSugar(int sugarGrowBackRate) {
		if (mySugar + sugarGrowBackRate < myMaxSugarCapacity) {
			mySugar += sugarGrowBackRate;
		} else {
			mySugar = myMaxSugarCapacity;
		}
	}
	
	/**
	 * Gets the amount of sugar this cell has.
	 * @return amount of sugar this cell has.
	 */
	public int getMySugarAmount() {
		return mySugar;
	}
	
	/**
	 * Description of sugar scape cell.
	 */
	public String toString() {
		if (hasAgent()) {
			return "[my Agent: " + myAgent.toString() + ", my Sugar: " + mySugar + "]";
		} else {
			return String.valueOf(mySugar);
		}
	}
}
