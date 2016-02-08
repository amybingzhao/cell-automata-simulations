package Model;

public class ForagingAntsCell extends Cell {
	private int myFoodPheromones;
	private int myHomePheromones;
	private int myNumAnts;
	
	public ForagingAntsCell(String initialState, int row, int col) {
		super(initialState, row, col);
		// TODO Auto-generated constructor stub
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
