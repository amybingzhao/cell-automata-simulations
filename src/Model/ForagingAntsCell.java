package Model;

import java.util.ArrayList;
import java.util.List;

public class ForagingAntsCell extends Cell {
	private int myFoodPheromones;
	private int myHomePheromones;
	private int myNumAnts;
	private int myMaxNumAnts;
	private List<Ant> myAnts;
	private static final String HOME = "HOME";
	private static final String OBSTACLE = "OBSTACLE";
	private static final String FOOD = "FOOD";
	private static final int MAX_ANT_PER_CELL = 10;
	
	public ForagingAntsCell(String initialState, int row, int col, int numTotalAnts) {
		super(initialState, row, col);
		myAnts = new ArrayList<Ant>();
		if (getCurState().equals(HOME)) {
			myNumAnts = numTotalAnts;
			for (int i = 0; i < numTotalAnts; i++) {
				myAnts.add(new Ant());
			}
		} else {
			myNumAnts = 0;
		}
		myMaxNumAnts = MAX_ANT_PER_CELL;
		myFoodPheromones = 0;
		myHomePheromones = 0;
	}

	public int getNumAnts() {
		return myNumAnts;
	}
	
	public boolean setNumAnts(int numAnts) {
		if (numAnts < myMaxNumAnts) {
			myNumAnts = numAnts;
			return true;
		} else {
			return false;
		}
	}
	
	public void increaseFoodPheromones() {
		myFoodPheromones++;
	}
	
	public int getNumPheromones(String type) {
		if (type.equals(HOME)) {
			return myHomePheromones;
		} else{
			return myFoodPheromones;
		}
	}
	
	public void increaseHomePheromones() {
		myHomePheromones++;
	}
	
	public void addAnt(Ant ant) {
		myAnts.add(ant);
		myNumAnts++;
	}
	
	public List<Ant> getAnts() {
		return myAnts;
	}
	
	public void removeAnt(Ant ant) {
		myAnts.remove(ant);
		myNumAnts--;
	}
	
	public boolean isFull() {
		return myNumAnts >= myMaxNumAnts;
	}
	
	public boolean isObstacle() {
		return getCurState().equals(OBSTACLE);
	}
	
	public boolean isFood() {
		return getCurState().equals(FOOD);
	}
	
	public boolean isHome() {
		return getCurState().equals(HOME);
	}
	
	public String toString() {
		return ("Cell row: " + this.getCurRow() + ", col: " + this.getCurCol() + "\n" + 
				"\t num Ants: " + myNumAnts + "\n\tnum food pheromones: " + myFoodPheromones
				+ ", num home pheromones: " + myHomePheromones + "\n");
	}
}
