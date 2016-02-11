package Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Cell;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.paint.Color;
import Model.Ant;

public class ForagingAntsRules extends Rules {
	private int numTotalAnts;
	private static final String HOME = "HOME";
	private static final String GROUND = "GROUND";
	private static final String OBSTACLE = "OBSTACLE";
	private static final String FOOD = "FOOD";
	private static final int NUM_NEIGHBORS = 8;
	private Map<String, String[]> myForwardDirectionMap;
	private Map<String, Integer[]> myDirectionToCoordMap;
	private Map<Integer[], String> myCoordToDirectionMap;
	
	public ForagingAntsRules(int numAnts) {
		numTotalAnts = numAnts;
		initForwardDirectionMap();
		initDirectionToCoordMap();	
		initCoordToDirectionMap();
	}
	
	private void initDirectionToCoordMap() {
		myDirectionToCoordMap = new HashMap<String, Integer[]>();
		myDirectionToCoordMap.put("N", new Integer[]{0, 1});
		myDirectionToCoordMap.put("S", new Integer[]{2, 1});
		myDirectionToCoordMap.put("E", new Integer[]{1, 2});
		myDirectionToCoordMap.put("W", new Integer[]{1, 0});
		myDirectionToCoordMap.put("NW", new Integer[]{0, 0});
		myDirectionToCoordMap.put("NE", new Integer[]{0, 2});
		myDirectionToCoordMap.put("SW", new Integer[]{2, 0});
		myDirectionToCoordMap.put("SE", new Integer[]{2, 2});
	}
	
	private void initCoordToDirectionMap() {
		myCoordToDirectionMap = new HashMap<Integer[], String>();
		myCoordToDirectionMap.put(new Integer[]{0, 1}, "N");
		myCoordToDirectionMap.put(new Integer[]{2, 1}, "S");
		myCoordToDirectionMap.put(new Integer[]{1, 2}, "E");
		myCoordToDirectionMap.put(new Integer[]{1, 0}, "W");
		myCoordToDirectionMap.put(new Integer[]{0, 0}, "NW");
		myCoordToDirectionMap.put(new Integer[]{0, 2}, "NE");
		myCoordToDirectionMap.put(new Integer[]{2, 0}, "SW");
		myCoordToDirectionMap.put(new Integer[]{2, 2}, "SE");
	}

	private void initForwardDirectionMap() {
		myForwardDirectionMap = new HashMap<String, String[]>();
		myForwardDirectionMap.put("N", new String[]{"N", "NW", "NE"});
		myForwardDirectionMap.put("W", new String[]{"W", "NW", "SW"});
		myForwardDirectionMap.put("E", new String[]{"E", "NE", "SE"});
		myForwardDirectionMap.put("S", new String[]{"S", "SW", "SE"});
		myForwardDirectionMap.put("NW", new String[]{"NW", "N", "W"});
		myForwardDirectionMap.put("SW", new String[]{"SW", "S", "W"});
		myForwardDirectionMap.put("NE", new String[]{"NE", "N", "E"});
		myForwardDirectionMap.put("SE", new String[]{"SE", "S", "E"});
	}

	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new ForagingAntsCell(initialState, row, col, numTotalAnts);
	}

	public void applyRulesToCell(ForagingAntsCell cell, Grid grid) {
		List<Ant> ants = cell.getAnts();
		
		for (int i = 0; i < ants.size(); i++) {
			handleAnt(ants.get(i), cell, grid);
		}
	}
	
	private void handleAnt(Ant ant, ForagingAntsCell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		List<Integer[]> directions = getDirectionsToCheck(ant);
		if (ant.hasFood()) {
			ant.returnToNest(cell, neighborhood, directions);
		} else {
			ant.findFoodSource(cell, neighborhood, directions);
		}
	}

	private List<Integer[]> getDirectionsToCheck(Ant ant) {
		List<Integer[]> directions = new ArrayList<Integer[]>();
		String[] forwardDirections = myForwardDirectionMap.get(ant.getDirection());
		
		for (int i = 0; i < forwardDirections.length; i++) {
			directions.add(myDirectionToCoordMap.get(forwardDirections[i]));
		}
		
		for (String key : myDirectionToCoordMap.keySet()) {
			Integer[] dir = myDirectionToCoordMap.get(key);
			if (!directions.contains(dir)) {
				directions.add(dir);
			}
		}
		
		return directions;
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		applyRulesToCell((ForagingAntsCell) cell, grid);
	}

	@Override
	public Color getFill(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Foraging Ants";
	}

	@Override
	public List<String> getParameters() {
		List<String> parameters = new ArrayList<String>();
		return parameters;
	}

}
