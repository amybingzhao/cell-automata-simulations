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
	
	public Ant() {
		hasFood = false;
		hasMovedThisTurn = false;
		setRandomDirection();
	}
	
	public void setHasMovedThisTurn(boolean bool) {
		hasMovedThisTurn = bool;
	}
	
	public boolean hasMovedThisTurn() {
		return hasMovedThisTurn;
	}
	
	public boolean hasFood() {
		return hasFood;
	}
	
	public void gotFood() {
		hasFood = true;
	}
	
	public Integer[] getDirection() {
		return new Integer[]{myDirectionRow, myDirectionCol};
	}
	
	public void setDirection(int[] direction) {
		myDirectionRow = direction[0];
		myDirectionCol = direction[1];
	}

	public void followFoodPheromones(Cell[][] neighborhood, List<Integer[]> directions) {
		ForagingAntsCell nextLocation = followPheromones(FOOD, neighborhood, directions);
		if (nextLocation.isFood()) {
				System.out.println("found da food stuffsz");
				hasFood = true;
		}
	}
	
	private ForagingAntsCell followPheromones(String type, Cell[][] neighborhood, List<Integer[]> directions) {
		List<Integer[]> forwardDirections = directions.subList(0, NUM_FORWARD_NEIGHBORS - 1);
		List<Integer[]> otherDirections = directions.subList(NUM_FORWARD_NEIGHBORS, directions.size() - 1); 
		
		ForagingAntsCell curLocation = (ForagingAntsCell) neighborhood[1][1];
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
	
	private void moveToNextLocation(ForagingAntsCell curLocation, ForagingAntsCell nextLocation) {
		//System.out.println("tryna move");
		curLocation.removeAnt(this);
		//System.out.println(curLocation);
		nextLocation.addAnt(this);
		setHasMovedThisTurn(true);
		//System.out.println(nextLocation);
		//System.out.println("end");
	}
	
	private ForagingAntsCell selectLocation(String type, Cell[][] neighborhood, List<Integer[]> forwardDirections, List<Integer[]> otherDirections) {
		ForagingAntsCell nextLocation = null;
		if (canMoveForward(neighborhood, forwardDirections)) {
			Map<ForagingAntsCell, Integer> forwardWeights = assignWeights(type, neighborhood, forwardDirections);
			nextLocation = pickWeightedRandomCell(forwardWeights);
		} else {
			Map<ForagingAntsCell, Integer> otherWeights = assignWeights(type, neighborhood, otherDirections);
			nextLocation = pickWeightedRandomCell(otherWeights);
		}
		
		while (nextLocation == null || nextLocation.isObstacle()) {
			nextLocation = (ForagingAntsCell) neighborhood[myDirectionRow][myDirectionCol];
			setRandomDirection();
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
	
	private Map<ForagingAntsCell, Integer> assignWeights(String pheromoneType, Cell[][] neighborhood, List<Integer[]> directions) {
		Map<ForagingAntsCell, Integer> weights = new HashMap<ForagingAntsCell, Integer>();

		for (int i = 0; i < directions.size(); i++) {
			Integer[] dirToCheck = directions.get(i);
			ForagingAntsCell neighbor = (ForagingAntsCell) neighborhood[dirToCheck[0]][dirToCheck[1]];
			if (neighbor != null) {
				weights.put(neighbor, neighbor.getNumPheromones(pheromoneType));
			}
		}
		
		return weights;
	}

	public void dropPheromones(String oppositeType, ForagingAntsCell cell) {
		if (oppositeType.equals(HOME)) {
			cell.increaseFoodPheromones();
		} else {
			cell.increaseHomePheromones();
		}
	}

	public void followHomePheromones(Cell[][] neighborhood, List<Integer[]> directions) {
		ForagingAntsCell nextLocation = followPheromones(HOME, neighborhood, directions);
		if (nextLocation.isHome()) {
			hasFood = false;
		}
	}

	public void returnToNest(ForagingAntsCell cell, Cell[][] neighborhood, List<Integer[]> directions) {
		if (cell.isFood()) {
			System.out.println("ant found food");
			Map<ForagingAntsCell, Integer> weights = assignWeights(HOME, neighborhood, directions);
			ForagingAntsCell nextLocation = pickWeightedRandomCell(weights);
			if (nextLocation != null) {
				int[] relativeNextLocation = new int[]{cell.getCurRow() - nextLocation.getCurRow(), cell.getCurCol() - nextLocation.getCurCol()};
				setDirection(relativeNextLocation);
			}
		}
		
		followHomePheromones(neighborhood, directions);
	}

	public void findFoodSource(ForagingAntsCell cell, Cell[][] neighborhood, List<Integer[]> directions) {
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
	
	public void setRandomDirection() {
		myDirectionRow = (int) Math.round(Math.random() * (NUM_NEIGHBORS_PER_SIDE - 1));
		myDirectionCol = (int) Math.round(Math.random() * (NUM_NEIGHBORS_PER_SIDE - 1));
		if ((myDirectionRow == 1 && myDirectionCol == 1)) {
			setRandomDirection();
		}
	}
}
