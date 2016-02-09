package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ant {
	private static final int NUM_FORWARD_NEIGHBORS = 3;
	private boolean hasFood = false;
	private int myDirectionRow;
	private int myDirectionCol;
	private static final String FOOD = "FOOD";
	private static final String HOME = "HOME";
	
	public boolean hasFood() {
		return hasFood;
	}
	
	public void gotFood() {
		hasFood = true;
	}
	
	public int[] getDirection() {
		return new int[]{myDirectionRow, myDirectionCol};
	}
	
	public void setDirection(int[] direction) {
		myDirectionRow = direction[0];
		myDirectionCol = direction[1];
	}

	public void dropHomePheromones(ForagingAntsCell cell) {
		cell.increaseHomePheromones();
	}

	public void followFoodPheromones(Cell[][] neighborhood, List<Integer[]> directions) {
		followPheromones(FOOD, neighborhood, directions);
	}
	
	private void followPheromones(String type, Cell[][] neighborhood, List<Integer[]> directions) {
		List<Integer[]> forwardDirections = directions.subList(0, NUM_FORWARD_NEIGHBORS - 1);
		List<Integer[]> otherDirections = directions.subList(NUM_FORWARD_NEIGHBORS, directions.size() - 1); 
		
		ForagingAntsCell curLocation = (ForagingAntsCell) neighborhood[1][1];
		ForagingAntsCell nextLocation = selectLocation(type, neighborhood, forwardDirections, otherDirections);
	
		if (nextLocation != null) {
			dropHomePheromones((ForagingAntsCell) neighborhood[1][1]);
			int[] relativeNextLocation = new int[]{curLocation.getCurRow() - nextLocation.getCurRow(), curLocation.getCurCol() - nextLocation.getCurCol()};
			setDirection(relativeNextLocation); 
			moveToNextLocation(curLocation, nextLocation);
		}
	}
	
	private void moveToNextLocation(ForagingAntsCell curLocation, ForagingAntsCell nextLocation) {
		curLocation.removeAnt(this);
		nextLocation.addAnt(this);
	}
	private ForagingAntsCell selectLocation(String type, Cell[][] neighborhood, List<Integer[]> forwardDirections, List<Integer[]> otherDirections) {
		ForagingAntsCell nextLocation = null;
		
		if (canMoveForward(neighborhood, forwardDirections)) {
			Map<ForagingAntsCell, Integer> forwardWeights = assignWeights(type, neighborhood, forwardDirections);
			nextLocation = pickWeightedRandomCell(forwardWeights);
		}
		
		if (nextLocation == null) {
			Map<ForagingAntsCell, Integer> otherWeights = assignWeights(type, neighborhood, otherDirections);
			nextLocation = pickWeightedRandomCell(otherWeights);
		}
		
		return nextLocation;
	}

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

	private int generateRandom(int max) {
		return (int) Math.round(Math.random() * (max-1));
	}
	
	private boolean canMoveForward(Cell[][] neighborhood, List<Integer[]> forwardDirections) {
		int obstacleCount = 0;
		int fullCount = 0;

		for (int i = 0; i < forwardDirections.size(); i++) {
			Integer[] dirToCheck = forwardDirections.get(i);
			ForagingAntsCell neighbor = (ForagingAntsCell) neighborhood[dirToCheck[0]][dirToCheck[1]];
			if (neighbor.isFull()) {
				fullCount++;
			}
			if (neighbor.isObstacle()) {
				obstacleCount++;
			}
		}
		
		return (obstacleCount + fullCount) == NUM_FORWARD_NEIGHBORS;
	}
	
	private Map<ForagingAntsCell, Integer> assignWeights(String pheromoneType, Cell[][] neighborhood, List<Integer[]> directions) {
		Map<ForagingAntsCell, Integer> weights = new HashMap<ForagingAntsCell, Integer>();

		for (int i = 0; i < directions.size(); i++) {
			Integer[] dirToCheck = directions.get(i);
			ForagingAntsCell neighbor = (ForagingAntsCell) neighborhood[dirToCheck[0]][dirToCheck[1]];
			weights.put(neighbor, neighbor.getNumPheromones(pheromoneType));
		}
		
		return weights;
	}

	public void dropFoodPheromones(ForagingAntsCell cell) {
		cell.increaseFoodPheromones();
	}

	public void followHomePheromones(Cell[][] neighborhood, List<Integer[]> directions) {
		followPheromones(HOME, neighborhood, directions);
	}

	public void returnToNest(ForagingAntsCell cell, Cell[][] neighborhood, List<Integer[]> directions) {
		if (cell.isFood()) {
			Map<ForagingAntsCell, Integer> weights = assignWeights(HOME, neighborhood, directions);
			ForagingAntsCell nextLocation = pickWeightedRandomCell(weights);
			int[] relativeNextLocation = new int[]{cell.getCurRow() - nextLocation.getCurRow(), cell.getCurCol() - nextLocation.getCurCol()};
			setDirection(relativeNextLocation); 
		}
		
		followFoodPheromones(neighborhood, directions);
		
		if (cell.isHome()) {
			hasFood = false;
		}
	}

	public void findFoodSource(ForagingAntsCell cell, Cell[][] neighborhood, List<Integer[]> directions) {
		if (cell.isHome()) {
			Map<ForagingAntsCell, Integer> weights = assignWeights(FOOD, neighborhood, directions);
			ForagingAntsCell nextLocation = pickWeightedRandomCell(weights);
			int[] relativeNextLocation = new int[]{cell.getCurRow() - nextLocation.getCurRow(), cell.getCurCol() - nextLocation.getCurCol()};
			setDirection(relativeNextLocation);
		}
		
		followHomePheromones(neighborhood, directions);
		
		if (cell.isFood()) {
			hasFood = true;
		}
	}
}
