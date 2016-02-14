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
	
	/**
	 * Constructs a ForagingAntsCell, initializing all ants to start at HOME.
	 * @param initialState
	 * @param row
	 * @param col
	 * @param numTotalAnts
	 */
	public ForagingAntsCell(String initialState, int row, int col, int numTotalAnts) {
		super(initialState, row, col);
		myAnts = new ArrayList<Ant>();
		if (initialState.equals(HOME)) {
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

	/**
	 * Gets number of ants currently on this cell.
	 * @return number of ants in this cell.
	 */
	public int getNumAnts() {
		return myNumAnts;
	}
	
	/**
	 * Sets the number of ants currently on this cell.
	 * @param numAnts: number of ants on the cell.
	 * @return: returns true if cell is not yet full; false otherwise.
	 */
	public boolean setNumAnts(int numAnts) {
		if (numAnts < myMaxNumAnts) {
			myNumAnts = numAnts;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Increases the amount of food pheromones on this cell by 1.
	 */
	public void increaseFoodPheromones() {
		myFoodPheromones++;
	}
	
	/**
	 * Gets the number of pheromones specified.
	 * @param type: type of phereomone of interest (HOME or FOOD).
	 * @return number of specific type of pheromone currently on this cell.
	 */
	public int getNumPheromones(String type) {
		if (type.equals(HOME)) {
			return myHomePheromones;
		} else{
			return myFoodPheromones;
		}
	}
	
	/**
	 * Increases the number of home pheromones on this cell by 1.
	 */
	public void increaseHomePheromones() {
		myHomePheromones++;
	}
	
	/**
	 * Adds an ant to this cell and updates the number of ants.
	 * @param ant: ant to add.
	 */
	public void addAnt(Ant ant) {
		myAnts.add(ant);
		myNumAnts++;
	}
	
	/**
	 * Returns list of ants currently on the cell.
	 * @return list of ants.
	 */
	public List<Ant> getAnts() {
		return myAnts;
	}
	
	/**
	 * Removes an ant that is currently on this cell and updates the number of ants.
	 * @param ant: ant to remove.
	 */
	public void removeAnt(Ant ant) {
		myAnts.remove(ant);
		myNumAnts--;
	}
	
	/**
	 * Checks if this cell is at max ant capacity.
	 * @return true if cell is at max capacity; false otherwise.
	 */
	public boolean isFull() {
		return myNumAnts >= myMaxNumAnts;
	}
	
	/**
	 * Checks if this cell is an obstacle.
	 * @return true if this cell is an obstacle; false otherwise.
	 */
	public boolean isObstacle() {
		return getCurState().equals(OBSTACLE);
	}
	
	/**
	 * Checks if this cell is a food source.
	 * @return true if this cell is a food source; false otherwise.
	 */
	public boolean isFood() {
		return getCurState().equals(FOOD);
	}
	
	/**
	 * Checks if this cell is the home.
	 * @return true if this cell is the home; false otherwise.
	 */
	public boolean isHome() {
		return getCurState().equals(HOME);
	}
	
	/**
	 * Description of ForagingAntCell.
	 */
	public String toString() {
		return ("Cell row: " + this.getCurRow() + ", col: " + this.getCurCol() + "\n" + 
				"\t num Ants: " + myNumAnts + "\n\tnum food pheromones: " + myFoodPheromones
				+ ", num home pheromones: " + myHomePheromones + "\n");
	}
}
