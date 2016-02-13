package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ant {
	private static final int NUM_FORWARD_NEIGHBORS = 3;
	private boolean hasFood;
	private int myDirectionRow;
	private int myDirectionCol;
	private static final String FOOD = "FOOD";
	private static final String HOME = "HOME";
	private static final int NUM_NEIGHBORS_PER_SIDE = 3;
	private boolean hasMovedThisTurn;
	
	/**
	 * Constructs an ant, initializing it as not yet moved, having no food, and orienting it in a random direction.
	 */
	public Ant() {
		hasFood = false;
		hasMovedThisTurn = false;
		setRandomDirection();
	}
	
	/**
	 * Sets the hasMovedThisTurn flag.
	 * @param bool true if the ant has moved this turn, false otherwise.
	 */
	public void setHasMovedThisTurn(boolean bool) {
		hasMovedThisTurn = bool;
	}
	
	/**
	 * Gets whether or not the ant has moved yet this turn.
	 * @return true if the ant has moved; false otherwise.
	 */
	public boolean hasMovedThisTurn() {
		return hasMovedThisTurn;
	}
	
	/**
	 * Gets whether or not the ant is carrying food.
	 * @return true if the ant has food; false otherwise.
	 */
	public boolean hasFood() {
		return hasFood;
	}
	
	/**
	 * Sets the hasFood flag to true to show that the ant got food.
	 */
	public void gotFood() {
		hasFood = true;
	}
	
	/**
	 * Gets the current orientation of the ant.
	 * @return integer array with index 0 holding the row and index 1 holding the column towards which the ant is
	 * 		facing in a 3x3 grid of it and its neighbors.
	 */
	public Integer[] getDirection() {
		return new Integer[]{myDirectionRow, myDirectionCol};
	}
	
	/**
	 * Sets the current orientation of the ant.
	 * @param direction: integer array with index 0 holding the row and index 1 holding the column towards which the ant is
	 * 		facing in a 3x3 grid of it and its neighbors.
	 */
	public void setDirection(int[] direction) {
		myDirectionRow = direction[0];
		myDirectionCol = direction[1];
	}

	/**
	 * Follows the food pheromone gradient towards the food source.
	 * @param neighborhood: cell of interest and its surrounding 8 neighbors.
	 * @param directions: directions to check for food pheromones in the order that they should be checked.
	 */
	public void followFoodPheromones(ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		ForagingAntsCell nextLocation = followPheromones(FOOD, neighborhood, directions);
		if (nextLocation.isFood()) {
			System.out.println("found da food stuffsz");
			hasFood = true;
		}
	}
	
	/**
	 * Follows the pheromone gradient of a specific type.
	 * @param type: type of pheromone to check for (HOME or FOOD).
	 * @param neighborhood: cell of interest and its surrounding 8 neighbors.
	 * @param directions: directions to check for pheromones in the order that they should be checked.
	 */
	private ForagingAntsCell followPheromones(String type, ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		List<Integer[]> forwardDirections = directions.subList(0, NUM_FORWARD_NEIGHBORS - 1);
		List<Integer[]> otherDirections = directions.subList(NUM_FORWARD_NEIGHBORS, directions.size() - 1); 
		
		ForagingAntsCell curLocation = neighborhood[1][1];
		ForagingAntsCell nextLocation = selectLocation(type, neighborhood, forwardDirections, otherDirections);
		
		dropPheromones(type, curLocation);

		System.out.println("Next: " + nextLocation);
		if (nextLocation != null) {
			int[] relativeNextLocation = new int[]{nextLocation.getCurRow() - curLocation.getCurRow() + 1, nextLocation.getCurCol() - curLocation.getCurCol() + 1};
			setDirection(relativeNextLocation); 
			moveToNextLocation(curLocation, nextLocation);
		}
		
		return nextLocation;
	}
	
	/**
	 * Moves an ant to another cell.
	 * @param curLocation: current cell that the ant is on.
	 * @param nextLocation: cell the ant wants to move to.
	 */
	private void moveToNextLocation(ForagingAntsCell curLocation, ForagingAntsCell nextLocation) {
		curLocation.removeAnt(this);
		nextLocation.addAnt(this);
		setHasMovedThisTurn(true);
	}
	
	/**
	 * Chooses a cell for the ant to move to based on pheromone type, ant's orientation, and pheromone gradient. 
	 * @param type: type of pheromone the ant is following (HOME or FOOD).
	 * @param neighborhood: cell of interest and its surrounding 8 neighbors.
	 * @param forwardDirections: forward directions relative to the ant's current orientation.
	 * @param otherDirections: non-forward directions.
	 * @return
	 */
	private ForagingAntsCell selectLocation(String type, ForagingAntsCell[][] neighborhood, List<Integer[]> forwardDirections, List<Integer[]> otherDirections) {
		ForagingAntsCell nextLocation = null;
		if (canMoveForward(neighborhood, forwardDirections)) {
			Map<ForagingAntsCell, Integer> forwardWeights = assignWeights(type, neighborhood, forwardDirections);
			nextLocation = pickWeightedRandomCell(forwardWeights);
		} else {
			Map<ForagingAntsCell, Integer> otherWeights = assignWeights(type, neighborhood, otherDirections);
			nextLocation = pickWeightedRandomCell(otherWeights);
		}
		
		while (nextLocation == null || nextLocation.isObstacle()) {
			nextLocation = neighborhood[myDirectionRow][myDirectionCol];
			setRandomDirection();
		}
		
		return nextLocation;
	}

	/**
	 * Picks the highest-weighted cell and breaks ties randomly.
	 * @param weights: map of ForagingAntsCells and their corresponding weights based on pheromone level.
	 * @return highest-weighted cell that can be moved to.
	 */
	private ForagingAntsCell pickWeightedRandomCell(Map<ForagingAntsCell, Integer> weights) {
		int totalWeights = 0;
		for (ForagingAntsCell key : weights.keySet()) {
			totalWeights += weights.get(key);
		}

		if (totalWeights > 0) {
			ForagingAntsCell[] weightedOptions = new ForagingAntsCell[totalWeights];
			int counter = 0;

			for (ForagingAntsCell key : weights.keySet()) {
				for (int i = counter; i < weights.get(key); i++) {
					weightedOptions[i] = key;
				}
				counter += weights.get(key);
			}

			int randomIndex = generateRandom(totalWeights);
			return weightedOptions[randomIndex];
		} else {
			return null;
		}
	}

	/**
	 * Generates a random number between 0 and max - 1.
	 * @param max: 1 greater than max random number to be generated.
	 * @return random number between 0 and max - 1.
	 */
	private int generateRandom(int max) {
		return (int) Math.round(Math.random() * (max-1));
	}
	
	/**
	 * Checks an ant's forward directions to see if any are both not at max capacity and not an obstacle.
	 * @param neighborhood: cell of interest and 8 surrounding neighbors.
	 * @param forwardDirections: forward directions based on ant's current orientation.
	 * @return true if ant can move forward; false otherwise.
	 */
	private boolean canMoveForward(ForagingAntsCell[][] neighborhood, List<Integer[]> forwardDirections) {
		int obstacleCount = 0;
		int fullCount = 0;

		for (int i = 0; i < forwardDirections.size(); i++) {
			Integer[] dirToCheck = forwardDirections.get(i);

			ForagingAntsCell neighbor = neighborhood[dirToCheck[0]][dirToCheck[1]];
			if (neighbor != null) {
				if (neighbor.isFull()) {
					fullCount++;
				}
				if (neighbor.isObstacle()) {
					obstacleCount++;
				}
			}
		}
		
		return (obstacleCount + fullCount) == NUM_FORWARD_NEIGHBORS;
	}
	
	/**
	 * Assigns weights to each neighbor cell based on pheromone level.
	 * @param pheromoneType: pheromone type of interest (HOME or FOOD).
	 * @param neighborhood: cell of interest and its surrounding 8 neighbors.
	 * @param directions: directions to check in the order they should be checked.
	 * @return
	 */
	private Map<ForagingAntsCell, Integer> assignWeights(String pheromoneType, ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		Map<ForagingAntsCell, Integer> weights = new HashMap<ForagingAntsCell, Integer>();

		for (int i = 0; i < directions.size(); i++) {
			Integer[] dirToCheck = directions.get(i);
			ForagingAntsCell neighbor = neighborhood[dirToCheck[0]][dirToCheck[1]];
			if (neighbor != null) {
				weights.put(neighbor, neighbor.getNumPheromones(pheromoneType));
			}
		}
		
		return weights;
	}

	/**
	 * Drops pheromones of a specific type on a given cell.
	 * @param oppositeType: opposite type of pheromone that you're following (i.e. if following FOOD, then HOME).
	 * @param cell: cell to drop the pheromones on.
	 */
	public void dropPheromones(String oppositeType, ForagingAntsCell cell) {
		if (oppositeType.equals(HOME)) {
			cell.increaseFoodPheromones();
		} else {
			cell.increaseHomePheromones();
		}
	}

	/**
	 * Follows the home pheromone gradient towards the ant home.
	 * @param neighborhood: cell of interest and its surrounding 8 neighbors.
	 * @param directions: directions to check for home pheromones in the order that they should be checked.
	 */
	public void followHomePheromones(ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		ForagingAntsCell nextLocation = followPheromones(HOME, neighborhood, directions);
		if (nextLocation.isHome()) {
			hasFood = false;
		}
	}

	/**
	 * Follow home pheromone gradient to try to move towards home.
	 * @param cell: cell that ant is currently on.
	 * @param neighborhood: cell of interest and surrounding 8 neighbors.
	 * @param directions: directions to check for pheromones in the order that they should be checked.
	 */
	public void returnToNest(ForagingAntsCell cell, ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		if (cell.isFood()) {
			System.out.println("ant found food");
			Map<ForagingAntsCell, Integer> weights = assignWeights(HOME, neighborhood, directions);
			ForagingAntsCell nextLocation = pickWeightedRandomCell(weights);
			if (nextLocation != null) {
				int[] relativeNextLocation = new int[]{cell.getCurRow() - nextLocation.getCurRow() + 1, cell.getCurCol() - nextLocation.getCurCol() + 1};
				setDirection(relativeNextLocation);
			}
		}
		
		followHomePheromones(neighborhood, directions);
	}

	/**
	 * Follow food pheromone gradient to try to move towards food source.
	 * @param cell: cell that ant is currently on.
	 * @param neighborhood: cell of interest and surrounding 8 neighbors.
	 * @param directions: directions to check for pheromones in the order that they should be checked.
	 */
	public void findFoodSource(ForagingAntsCell cell, ForagingAntsCell[][] neighborhood, List<Integer[]> directions) {
		if (cell.isHome()) {
			Map<ForagingAntsCell, Integer> weights = assignWeights(FOOD, neighborhood, directions);
			ForagingAntsCell nextLocation = pickWeightedRandomCell(weights);
			if (nextLocation != null) {
				int[] relativeNextLocation = new int[]{nextLocation.getCurRow() - cell.getCurRow() + 1, nextLocation.getCurCol() - cell.getCurCol() + 1};
				setDirection(relativeNextLocation);
			}
		}
		
		followFoodPheromones(neighborhood, directions);
	}
	
	/**
	 * Sets a random direction for the ant's orientation within a neighbors grid. (Cannot be [1, 1] as that is the current
	 * cell itself).
	 */
	public void setRandomDirection() {
		myDirectionRow = (int) Math.round(Math.random() * (NUM_NEIGHBORS_PER_SIDE - 1));
		myDirectionCol = (int) Math.round(Math.random() * (NUM_NEIGHBORS_PER_SIDE - 1));
		if ((myDirectionRow == 1 && myDirectionCol == 1)) {
			setRandomDirection();
		}
	}
}
