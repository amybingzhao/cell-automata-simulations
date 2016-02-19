// This entire file is part of my masterpiece.
// Blake Kaplan

//Extension to the XMLGenerator used exclusively for creating XML files differently weighted state abundances
//Utilizes code from the standard XMLGenerator
//No duplicate code

/**
 * @author Blake Kaplan
 * Weighted XML Generator Class
 */

package XML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WeightedGenerator extends XMLGenerator{
	
	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private Document myDocument;
	private Map<String, Double> stateWeights;
	private static final String CELLS = "Cells";
	private Stack<String> myStack;
	
	public WeightedGenerator(Map<String, Double> myWeights){
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			myDocument = myBuilder.newDocument();

		} catch (ParserConfigurationException e) {
			System.out.println("Document Builder Error");
		}
		stateWeights = myWeights;
	}
	
	/**
	 * Pushes all possible row, column locations to the stack and shuffles them
	 * 
	 * @param sideLength
	 *            The length of a side of the grid
	 */
	public void fillStack(int rows, int cols) {
		myStack = new Stack<String>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				myStack.push(row + "," + col);
			}
		}
		Collections.shuffle(myStack);
	}
	
	/**
	 * Creates a weighted cell state simulation setup where the percentages of
	 * cells follow data provided in a HashMap given to one of the constructors
	 * 
	 * @param sideLength
	 *            The length of a side of the grid
	 * @param rule
	 *            The name of the rule to be applied
	 * @return A JavaFX element containing the weighted cells
	 */
	public Element createWeightedRandomCells(int rows, int cols, String rule) {
		fillMap(rule);
		fillStack(rows, cols);
		Element myCells = myDocument.createElement(CELLS);
		for (String state : stateWeights.keySet()) {
			int numCells = (int) Math.round(((stateWeights.get(state) / 100) * (rows * cols)));
			for (int i = 0; i < numCells && !myStack.isEmpty(); i++) {
				myCells.appendChild(getCoordinates(state, rule));
			}
		}
		if (myStack.isEmpty())
			return myCells;

		while (!myStack.isEmpty()) {
			myCells.appendChild(getCoordinates(null, rule));
		}

		return myCells;
	}

	/**
	 * Completes the stateWeights map, adding in states for which no desired
	 * percentage was provided. The remaining percentage is split evenly among
	 * unprovided states.
	 * 
	 * @param rule
	 *            The name of the rule to be applied
	 */
	public void fillMap(String rule) {
		String[] states = getStates(rule);
		double sum = 0;
		ArrayList<String> statesNotIncluded = new ArrayList<String>();
		for (String state : states) {
			if (stateWeights.containsKey(state)) {
				sum += (int) Math.round(stateWeights.get(state));
			} else {
				statesNotIncluded.add(state);
			}
		}
		for (String state : statesNotIncluded) {
			stateWeights.put(state, (100 - sum) / statesNotIncluded.size());
		}
	}
	
	/**
	 * Creates a cell element for a given state and rule at a location taken
	 * from the stack
	 * 
	 * @param state
	 *            A cell state
	 * @param rule
	 *            The type of the rule for the current simulation
	 * @return An cell DOM element
	 */
	public Element getCoordinates(String state, String rule) {
		String coordinates = myStack.pop();
		int[] myCoordinates = new int[2];
		String[] inputData = coordinates.split(",");
		Element myCell;
		myCoordinates[0] = Integer.parseInt(inputData[0]);
		myCoordinates[1] = Integer.parseInt(inputData[1]);
		if (state == null) {
			Random myRandom = new Random();
			String statesString = getRulesResource(rule);
			String[] states = statesString.split(",");
			myCell = makeCellEntry(myCoordinates[0], myCoordinates[1], states[myRandom.nextInt(states.length)]);
		} else {
			myCell = makeCellEntry(myCoordinates[0], myCoordinates[1], state);
		}
		return myCell;
	}
	
}
