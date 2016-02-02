package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

public class PredatorPreyRules extends Rules {
	private int mySharkEnergy;
	private int mySharkReproductionTime;
	private int myFishReproductionTime;
	private int myInitSharkReproductionTime;
	private int myInitFishReproductionTime;
	private static final String FISH = "FISH";
	private static final String SHARK = "SHARK";
	private static final String WATER = "WATER";
	private static final int NUM_NEIGHBORS = 4;
	
	public PredatorPreyRules(int initialSharkEnergy, int sharkReproductionTime, int fishReproductionTime) {
		mySharkEnergy = initialSharkEnergy;
		mySharkReproductionTime = sharkReproductionTime;
		myFishReproductionTime = fishReproductionTime;
		myInitSharkReproductionTime = sharkReproductionTime;
		myInitFishReproductionTime = fishReproductionTime;
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();
		
		if (cell.getCurRow() == grid.getNumRows() - 1 && cell.getCurCol() == grid.getNumCols() - 1) {
			updateReproductionTimes();
		}
		
		if (curState.equals(FISH)) {
			handleFishCell(cell, grid);
		} else if (curState.equals(SHARK)) {
			handleSharkCell(cell, grid);
		}
	}

	private void updateReproductionTimes() {
		if (mySharkReproductionTime > 0) {
			mySharkReproductionTime = myInitSharkReproductionTime;
		} else {
			mySharkReproductionTime--;
		}
		
		if (myFishReproductionTime > 0) {
			myFishReproductionTime = myInitFishReproductionTime;
		} else {
			myFishReproductionTime--;
		}
	}

	private void handleFishCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		Cell nextLocation = cellToMoveTo(neighborhood, WATER);
		
		if (nextLocation != null) {
			switchCells(cell, nextLocation);
			checkForReproduction(cell);
		}
	}

	private void handleSharkCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		
		Cell fishToEat = cellToMoveTo(neighborhood, FISH);
		if (fishToEat != null) {
			eatFish(fishToEat, cell, grid);
			checkForReproduction(cell);
			mySharkEnergy++;
		} else {
			mySharkEnergy--;
			if (noMoreEnergy(cell)) {
				cell.setNextState(WATER);
			} else {
				Cell nextLocation = cellToMoveTo(neighborhood, WATER);
				if (nextLocation != null) {
					switchCells(cell, nextLocation);
					checkForReproduction(cell);
				}
			}
		}
	}
	
	private boolean noMoreEnergy(Cell cell) {
		return mySharkEnergy == 0;
	}

	private void eatFish(Cell fishToEat, Cell curShark, Grid grid) {
		if (fishAlreadyMoved(curShark, fishToEat)) {
			undoFishMove(fishToEat, grid);
		}
		
		fishToEat.setCurState(WATER);
		switchCells(fishToEat, curShark);
	}
	
	private void undoFishMove(Cell fishToEat, Grid grid) {
		Cell fishNextLocation = grid.getCell(fishToEat.getNextRow(), fishToEat.getNextCol());
		fishNextLocation.setNextState(WATER);
		removeCellToBeUpdated(fishNextLocation);
	}

	private boolean fishAlreadyMoved(Cell shark, Cell fish) {
		int fishRow = fish.getCurRow();
		int fishCol = fish.getCurCol();
		int sharkRow = shark.getCurRow();
		int sharkCol = shark.getCurCol();
		
		return ((fishRow < sharkRow) || (fishRow == sharkRow && fishCol < sharkCol));
	}
	
	private void checkForReproduction(Cell cell) {
		switch (cell.getCurState()) {
			case FISH:
				if (fishCanReproduce()) {
					cell.setNextState(FISH);
				}
				break;
			case SHARK:
				if (sharkCanReproduce()) {
					cell.setNextState(SHARK);
				}
		}
	}
	
	private boolean fishCanReproduce() {
		return (myFishReproductionTime == 0);
	}
	
	private boolean sharkCanReproduce() {
		return (mySharkReproductionTime == 0);
	}

	private int generateRandom(int max) {
		return (int) Math.round(Math.random() * max);
	}
	
	private Cell cellToMoveTo(Cell[][] neighborhood, String stateToMoveTo) {
		List<Cell> optionList = new ArrayList<Cell>();

		checkIfCanMoveTo(neighborhood[0][1], stateToMoveTo, optionList);
		checkIfCanMoveTo(neighborhood[1][0], stateToMoveTo, optionList);
		checkIfCanMoveTo(neighborhood[1][2], stateToMoveTo, optionList);
		checkIfCanMoveTo(neighborhood[2][1], stateToMoveTo, optionList);
		
		if (optionList.size() > 0) {
			return optionList.get(generateRandom(optionList.size()));
		} else {
			return null;
		}
	}
	
	private void checkIfCanMoveTo(Cell cellToCheck, String stateToMoveTo, List<Cell> optionList) {
		if (canMoveTo(cellToCheck, stateToMoveTo)) {
			optionList.add(cellToCheck);
		}
	}
	private boolean canMoveTo(Cell cellToCheck, String stateToMoveTo) {
		if (cellToCheck == null) {
			return false;
		} else {
			return cellToCheck.getCurState().equals(stateToMoveTo);
		}
	}
}
