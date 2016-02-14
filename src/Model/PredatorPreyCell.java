/**
 * @author Amy Zhao
 * @author Austin Wu
 * The class responsible for the cell objects used in the Predator Prey simulation
 */

package Model;

public class PredatorPreyCell extends Cell {
	private int myEnergy;
	private int myInitialSharkEnergy;
	private static final String SHARK = "SHARK";
	private static final String FISH = "FISH";
	private static final String WATER = "WATER";

	public PredatorPreyCell(String initialState, int row, int col, int initialEnergy) {
		super(initialState, row, col);
		if (initialState.equals(SHARK)) {
			myEnergy = initialEnergy;
		}
		myInitialSharkEnergy = initialEnergy;
	}
	
	/**
	 * Decreases the shark's energy by 1.
	 */
	public void decreaseEnergy() {
		if (getCurState().equals(SHARK) && myEnergy > 0) {
			myEnergy--;
		}
	}
	
	/**
	 * Changes the shark's state to water as though it died.
	 */
	public void sharkDies() {
		if (getCurState().equals(SHARK)) {
			setNextState(WATER);
		}
	}
	
	/**
	 * Initializes a shark to be born next round with the correct state and energy.
	 */
	public void initShark() {
		setNextState(SHARK);
		myEnergy = myInitialSharkEnergy;
	}
	
	/**
	 * Gets the shark's current amount of energy.
	 * @return int representing shark's energy.
	 */
	public int getSharkEnergy() {
		return myEnergy;
	}
	
	/**
	 * Sets the shark's energy to a new amount.
	 * @param energy: amount of energy that shark should be updated to have.
	 */
	public void setSharkEnergy(int energy) {
		myEnergy = energy;
	}
	
	/**
	 * Description of cell.
	 */
	public String toString() {
		return "(" + getCurState() + ", " + getNextState() + ")";
	}
}
