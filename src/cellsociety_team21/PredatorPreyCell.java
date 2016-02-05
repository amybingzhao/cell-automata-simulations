package cellsociety_team21;

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
	
	public void increaseEnergy() {
		if (getCurState().equals(SHARK)) {
			myEnergy++;
		}
	}
	
	public void decreaseEnergy() {
		if (getCurState().equals(SHARK) && myEnergy > 0) {
			myEnergy--;
		}
	}
	
	public void sharkDies() {
		if (getCurState().equals(SHARK)) {
			setCurState(WATER);
		}
	}
	
	public void initShark() {
		setNextState(SHARK);
		myEnergy = myInitialSharkEnergy;
	}
	
	public int getSharkEnergy() {
		return myEnergy;
	}
	
	public void setSharkEnergy(int energy) {
		myEnergy = energy;
	}
	
	public String toString() {
		return "(" + getCurState() + ", " + getNextState() + ")";
	}
}
