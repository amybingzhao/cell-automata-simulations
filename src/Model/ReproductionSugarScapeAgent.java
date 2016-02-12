package Model;

import java.util.ArrayList;
import java.util.List;

public class ReproductionSugarScapeAgent extends SugarScapeAgent {
	private int myGender;
	private int myAge;
	private int myMaxAge;
	private int myFertileMin;
	private int myFertileMax;
	private int myInitSugar;
	private List<ReproductionSugarScapeAgent> myChildren;
	private static final int MAX_AGE_MINIMUM = 60;
	private static final int NUM_NEIGHBORS = 4;
	
	public ReproductionSugarScapeAgent(int initSugar, int metabolism, int vision, int row, int col, int gender, int maxAge, int fertileMin, int fertileMax) {
		super(initSugar, metabolism, vision, row, col);
		myInitSugar = initSugar;
		myChildren = new ArrayList<ReproductionSugarScapeAgent>();
		myAge = 0;
		myGender = gender;
		myMaxAge = maxAge;
		myFertileMin = fertileMin;
		myFertileMax = fertileMax;
	}
	
	public boolean isFertile() {
		return (getMySugarAmount() >= myInitSugar) && (myAge >= myFertileMin) && (myAge <= myFertileMax);
	}
	
	public ReproductionSugarScapeAgent findMate(Grid grid) {
		if (this.isFertile()) {
			List<SugarScapeCell> neighbors = getViableNeighbors(grid);
			while (!neighbors.isEmpty()) {
				int rand = (int) Math.round(Math.random() * (neighbors.size()-1));
				SugarScapeCell neighbor = neighbors.get(rand);
				if (neighbor.hasAgent()) {
					ReproductionSugarScapeAgent neighborAgent = (ReproductionSugarScapeAgent) neighbor.getAgent();
					if (neighborAgent.isFertile() && neighborAgent.isOppositeGender(this) && (this.hasEmptyNeighbor(grid) || neighborAgent.hasEmptyNeighbor(grid))) {
						return neighborAgent;
					}
				}
			}
		}
		return null;
	}
	
	// check for off by 1 b/c odd
	public int splitSugar(ReproductionSugarScapeAgent neighbor) {
		setSugar(getMySugarAmount()/2);
		neighbor.setSugar(neighbor.getMySugarAmount()/2);
		return getMySugarAmount()/2 + neighbor.getMySugarAmount()/2;
	}
	
	private boolean hasEmptyNeighbor(Grid grid) {
		SugarScapeCell[][] neighborhood = (SugarScapeCell[][]) grid.getNeighborhood(getRow(), getCol(), NUM_NEIGHBORS);
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood.length; col++) {
				if (!neighborhood[row][col].hasAgent()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public SugarScapeCell getEmptyNeighbor(Grid grid) {
		SugarScapeCell[][] neighborhood = (SugarScapeCell[][]) grid.getNeighborhood(getRow(), getCol(), NUM_NEIGHBORS);
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood.length; col++) {
				if (!neighborhood[row][col].hasAgent()) {
					return neighborhood[row][col];
				}
			}
		}
		return null;
	}
	private boolean isOppositeGender(ReproductionSugarScapeAgent agent) {
		return agent.getGender() != myGender;
	}
	
	private int getGender() {
		return myGender;
	}
	
	public String toString() {
		return "my vision: " + getVision();
	}
}
