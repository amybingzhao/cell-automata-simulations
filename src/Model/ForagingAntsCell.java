package Model;

public class ForagingAntsCell extends Cell {
	private int myFoodPheromones;
	private int myHomePheromones;
	private int myNumAnts;
	private static final String HOME = "HOME";
	
	public ForagingAntsCell(String initialState, int row, int col, int numTotalAnts) {
		super(initialState, row, col);
		if (getCurState().equals(HOME)) {
			myNumAnts = numTotalAnts;
		} else {
			myNumAnts = 0;
		}
		myFoodPheromones = 0;
		myHomePheromones = 0;
	}

	public int getNumAnts() {
		return myNumAnts;
	}
	
	public void setNumAnts(int numAnts) {
		myNumAnts = numAnts;
	}
	
	public int numFoodPheromones() {
		return myFoodPheromones;
	}
	
	public void increaseFoodPheromones() {
		myFoodPheromones++;
	}
	
	public int numHomePheromones() {
		return myHomePheromones;
	}
	
	public void increaseHomePheromones() {
		myHomePheromones++;
	}
}
