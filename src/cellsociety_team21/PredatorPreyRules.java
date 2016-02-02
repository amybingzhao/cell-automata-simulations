package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class PredatorPreyRules extends Rules {
	private int mySharkEnergy;
	private int mySharkReproductionTime;
	private int myFishReproductionTime;
	private int myInitSharkReproductionTime;
	private int myInitFishReproductionTime;
	private static final int MY_CELL_ROW = 1;
	private static final int MY_CELL_COL = 1;
	private static final String FISH = "FISH";
	private static final String SHARK = "SHARK";
	private static final String WATER = "WATER";
	private static final Color FISHCOLOR = Color.TEAL;
	private static final Color SHARKCOLOR = Color.GRAY;
	private static final Color WATERCOLOR = Color.BLUE;
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
		
		if (cell.getRow() == grid.getNumRows() - 1 && cell.getCol() == grid.getNumCols() - 1) {
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
		Cell[][] neighborhood = grid.getNeighborhood(cell.getRow(), cell.getCol(), NUM_NEIGHBORS);
		Cell nextLocation = cellToMoveTo(neighborhood, WATER);
		
		if (nextLocation != null) {
			switchCells(cell, nextLocation);
			checkForReproduction(cell);
		}
	}

	private void handleSharkCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getRow(), cell.getCol(), NUM_NEIGHBORS);
		
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
	
	// TODO: not sure what to do with this... should we have a nextLocation thing for cells?
	private void undoFishMove(Cell fishToEat, Grid grid) {
		// stuff
		//removeCellToBeUpdated(water cell that was changed)
	}

	private boolean fishAlreadyMoved(Cell shark, Cell fish) {
		int fishRow = fish.getRow();
		int fishCol = fish.getCol();
		int sharkRow = shark.getRow();
		int sharkCol = shark.getCol();
		
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
		
		for (int r = 0; r < neighborhood.length; r++) {
			for (int c = 0; c < neighborhood[r].length; c++) {
				if (neighborhood[r][c] != null) {
					Cell cell = neighborhood[r][c];
					if ((r != MY_CELL_ROW || c != MY_CELL_COL) && cell.getCurState().equals(stateToMoveTo)) {
						optionList.add(cell);
					}
				}
			}
		}
		
		if (optionList.size() > 0) {
			return optionList.get(generateRandom(optionList.size()));
		} else {
			return null;
		}
	}
	
	public Color getFill(String s){
		switch(s){
		case FISH:
			return FISHCOLOR;
		case SHARK:
			return SHARKCOLOR;
		case WATER:
			return WATERCOLOR;
		default:
			return ERRORCOLOR;
		}
	}
}
