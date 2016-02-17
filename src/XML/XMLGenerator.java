// This entire file is part of my masterpiece.
// Blake Kaplan

//Functions exhibit generalized functionality to eliminate duplicated code
//Functionality adapted for many purposes

/**
 * @author Blake Kaplan
 * Generates XML files to be used to initialize simulations
 */

package XML;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import Controller.Simulation;
import Model.Cell;
import javafx.stage.FileChooser;

public class XMLGenerator {

	private static final String STATES = "States";
	private static final String PARAMETERS = "Parameters";
	private static final String NAME = "Name";
	private static final String GAME = "Game";
	private static final String CELLS = "Cells";
	private static final String CELL = "Cell";
	private static final String OUT_OF_BOUNDS = "OutOfBounds";
	private static final String CONFIG = "Config";
	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private Document myDocument;
	private static final String RULES_PROPERTIES = "Rules/Rules";
	private static final String XML_PROPERTIES = "XML/XML";
	private ResourceBundle myRulesResources;
	private Map<String, Double> stateWeights;
	private boolean weighted;
	private Stack<String> myStack;
	private Simulation mySimulation;
	private ResourceBundle XMLResources;

	public XMLGenerator() {
		myRulesResources = ResourceBundle.getBundle(RULES_PROPERTIES);
		XMLResources = ResourceBundle.getBundle(XML_PROPERTIES);
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			myDocument = myBuilder.newDocument();

		} catch (ParserConfigurationException e) {
			System.out.println("Document Builder Error");
		}
	}

	public XMLGenerator(Map<String, Double> myWeights) {
		this();
		stateWeights = myWeights;
		weighted = true;
	}

	public XMLGenerator(Map<String, Double> myWeights, Simulation simulation) {
		this(myWeights);
		mySimulation = simulation;
	}

	/**
	 * Generates an XML file containing a randomly determined starting state for
	 * a simulation
	 * 
	 * @param rows
	 *            Number of rows in the grid
	 * @param cols
	 *            Number of columns in the grid
	 * @param gameName
	 *            The name of the game in the simulation
	 * @param parameters
	 *            An arraylist of necessary game parameters
	 * @param states
	 *            An arraylist of potential game states
	 * @param numCells
	 *            The number of cells to be randomly generated
	 */
	public void generateFile(int row, int col, String rules, String gridType, List<String> parameters, File file) {

		myDocument = myBuilder.newDocument();
		Element myRoot = myDocument.createElement("Simulation");
		myDocument.appendChild(myRoot);
		myRoot.appendChild(getConfig(row, col, gridType));
		myRoot.appendChild(getRules(rules, parameters));
		if (weighted) {
			myRoot.appendChild(createWeightedRandomCells(row, col, rules + STATES));
		} else {
			myRoot.appendChild(createRandomCells(row, col, rules + STATES));
		}
		createFile(file);

	}

	public Element makeElement(int row, int col, String item, String key) {
		List<String> input = new ArrayList<String>();
		input.addAll(Arrays.asList("" + row, "" + col, item));
		String[] params = XMLResources.getString(key).split(",");
		Element myElement = createElement(key, params, input);
		return myElement;
	}

	/**
	 * Creates the data for the Config section of the XML file
	 * 
	 * @param rows
	 *            The number of rows in the grid
	 * @param cols
	 *            The number of columns in the grid
	 * @return An element containing the data to be put in the file
	 */
	public Element getConfig(int row, int col, String gridType) {
		return makeElement(row, col, gridType, CONFIG);
	}

	/**
	 * Creates a DOM element from provided date
	 * 
	 * @param parent
	 *            The name of the element to be made
	 * @param childrenElements
	 *            The titles for the name of each element child
	 * @param inputs
	 *            The values for each DOM entry
	 * @return A DOM element containing the provided data
	 */
	public Element createElement(String parent, String[] childrenElements, List<String> inputs) {
		Element myElement = myDocument.createElement(parent);
		try {
			for (int i = 0; i < childrenElements.length; i++) {
				Element newElement = myDocument.createElement(childrenElements[i]);
				newElement.appendChild(myDocument.createTextNode(inputs.get(i)));
				myElement.appendChild(newElement);
			}
		} catch (Exception e) {
			mySimulation.displayAlert(OUT_OF_BOUNDS);
		}
		return myElement;
	}

	/**
	 * Creates the data for the Game section of the XML file
	 * 
	 * @param game
	 *            The name of the game to be simulated
	 * @param params
	 *            An arraylist of provided simulation parameters
	 * @return An element containing the data to be put into the file
	 */
	public Element getRules(String rules, List<String> params) {

		Element gameElement = myDocument.createElement(GAME);
		Element myName = myDocument.createElement(NAME);
		myName.appendChild(myDocument.createTextNode(rules));
		Element myParams = parametersAsElement(params);
		gameElement.appendChild(myName);
		gameElement.appendChild(myParams);
		return gameElement;

	}

	/**
	 * Converts an arraylist of string parameters into a Java Element containing
	 * the parameters
	 * 
	 * @param parameters
	 *            An arraylist of simulation parameters
	 * @return An Element to be included in the XML file containing the
	 *         parameters
	 */
	public Element parametersAsElement(List<String> parameters) {
		Element paramElement = myDocument.createElement(PARAMETERS);
		for (String param : parameters) {
			String[] splitParam = param.split(":");
			Element paramType = myDocument.createElement(splitParam[0]);
			paramType.appendChild(myDocument.createTextNode(splitParam[1]));
			paramElement.appendChild(paramType);
		}
		return paramElement;
	}

	/**
	 * Randomly generates a provided number of cells based on the provided
	 * states and grid size
	 * 
	 * @param numCells
	 *            The desired number of generated cells
	 * @param rows
	 *            The number of rows in the grid
	 * @param cols
	 *            The number of columns in the grid
	 * @param states
	 *            An arraylist of potential cell states
	 * @return An Element to be included in the XML file
	 */
	public Element createRandomCells(int rows, int cols, String rule) {
		String[] states = getStates(rule);
		Element myCells = myDocument.createElement(CELLS);
		Random myRandom = new Random();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Element myCell = makeCellEntry(row, col, states[(myRandom.nextInt(states.length))]);
				myCells.appendChild(myCell);
			}
		}
		return myCells;
	}

	/**
	 * Returns a list of the states for a particular type of rules
	 * 
	 * @param rule
	 *            The type of rules to get the states for
	 * @return A string array of containing the states for the rule type
	 */
	private String[] getStates(String rule) {
		String statesString = myRulesResources.getString(rule);
		String[] states = statesString.split(",");
		return states;
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
			String statesString = myRulesResources.getString(rule);
			String[] states = statesString.split(",");
			myCell = makeCellEntry(myCoordinates[0], myCoordinates[1], states[myRandom.nextInt(states.length)]);
		} else {
			myCell = makeCellEntry(myCoordinates[0], myCoordinates[1], state);
		}
		return myCell;
	}

	/**
	 * Saves the current simulation into an XML file that can be loaded later
	 * 
	 * @param rulesType
	 *            The string representing which set of rules are being used
	 * @param rows
	 *            The number of rows in the grid
	 * @param cols
	 *            The number of columns in the grid
	 * @param gameGrid
	 *            The grid of Cell objects containing the current states of each
	 *            cell
	 * @param params
	 *            An ArrayList of the current simulation parameters
	 * @param myFile
	 *            The file to be saved to
	 */
	public void save(String rulesType, int rows, int cols, Cell[][] gameGrid, List<String> params, File myFile,
			String gridType) {
		myDocument = myBuilder.newDocument();
		Element saveConfig = getConfig(rows, cols, gridType);
		Element saveRules = getRules(rulesType, params);
		Element saveCells = myDocument.createElement(CELLS);
		for (int myRow = 0; myRow < rows; myRow++) {
			for (int myCol = 0; myCol < cols; myCol++) {
				Element myCell = makeCellEntry(myRow, myCol, gameGrid[myRow][myCol].getCurState());
				saveCells.appendChild(myCell);
			}
		}
		Element myRoot = myDocument.createElement("Simulation");
		myDocument.appendChild(myRoot);
		myRoot.appendChild(saveConfig);
		myRoot.appendChild(saveRules);
		myRoot.appendChild(saveCells);
		createFile(myFile);
	}

	/**
	 * Uses the data currently contained in the Document to create a new file as
	 * desired
	 * 
	 * @param myFile
	 *            The file to be written to
	 */
	public void createFile(File myFile) {
		TransformerFactory myTransformerFactory = TransformerFactory.newInstance();
		Transformer myTransformer;
		try {
			myTransformer = myTransformerFactory.newTransformer();
			myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			myTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			myTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource mySource = new DOMSource(myDocument);
			StreamResult myResult = new StreamResult(myFile);
			myTransformer.transform(mySource, myResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Given data about a particular cell, creates an XML element for the cell
	 * 
	 * @param row
	 *            The cell's row
	 * @param col
	 *            The cell's column
	 * @param state
	 *            The cell's state
	 * @return The cell XML element
	 */
	Element makeCellEntry(int row, int col, String state) {
		return makeElement(row, col, state, CELL);
	}

	/**
	 * Asks the user to define the values of simulation parameters when building
	 * an XML file
	 * 
	 * @param rule
	 *            The set of rules to be applied to the XML file
	 * @return An ArrayList containing the simulation parameters
	 */
	public List<String> promptForParameters(String rule) {
		ArrayList<String> parameters = new ArrayList<String>();
		String[] resourcesParams = myRulesResources.getString(rule + PARAMETERS).split(",");
		Scanner myScanner = new Scanner(System.in);
		for (String param : resourcesParams) {
			if (param.equals("NONE"))
				break;
			System.out.println("Choose the value of " + param);
			int value = myScanner.nextInt();
			parameters.add(param + ":" + value);
		}
		myScanner.close();
		return parameters;
	}
}
