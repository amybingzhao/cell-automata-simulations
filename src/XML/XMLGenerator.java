/**
 * @author Blake Kaplan
 * Generates XML files to be used to initialize simulations
 */

package XML;

import java.io.*;
import java.util.ArrayList;
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

public class XMLGenerator {

	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private Document myDocument;
	private static final String RULES_PROPERTIES = "Rules/Rules";
	private ResourceBundle myRulesResources;
	private Map<String, Double> stateWeights;
	private boolean weighted;
	private Stack<String> myStack;
	private Simulation mySimulation;

	public XMLGenerator() {
		myRulesResources = ResourceBundle.getBundle(RULES_PROPERTIES);
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
	public void generateFile(int sideLength, String rules, String fileName, String gridType) {

		myDocument = myBuilder.newDocument();
		Element myRoot = myDocument.createElement("Simulation");
		myDocument.appendChild(myRoot);
		myRoot.appendChild(getConfig(sideLength, gridType));
		myRoot.appendChild(getRules(rules, promptForParameters(rules)));
		if (weighted) {
			myRoot.appendChild(createWeightedRandomCells(sideLength, rules + "States"));
		} else {
			myRoot.appendChild(createRandomCells(sideLength, rules + "States"));
		}
		createFile(new File("data/" + fileName));

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
	public Element getConfig(int sideLength, String gridType) {

		Element configElement = myDocument.createElement("Config");
		Element myRows = myDocument.createElement("Rows");
		myRows.appendChild(myDocument.createTextNode("" + sideLength));
		Element myCols = myDocument.createElement("Columns");
		myCols.appendChild(myDocument.createTextNode("" + sideLength));
		Element myGrid = myDocument.createElement("GridType");
		myGrid.appendChild(myDocument.createTextNode("" + gridType));
		configElement.appendChild(myRows);
		configElement.appendChild(myCols);
		configElement.appendChild(myGrid);

		return configElement;
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

		Element gameElement = myDocument.createElement("Game");
		Element myName = myDocument.createElement("Name");
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
		Element paramElement = myDocument.createElement("Parameters");
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
	public Element createRandomCells(int sideLength, String rule) {
		String statesString = myRulesResources.getString(rule);
		String[] states = statesString.split(",");
		Element myCells = myDocument.createElement("Cells");
		Random myRandom = new Random();
		for (int row = 0; row < sideLength; row++) {
			for (int col = 0; col < sideLength; col++) {
				Element myCell = makeCellEntry(row, col, states[(myRandom.nextInt(states.length))]);
				myCells.appendChild(myCell);
			}
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
		String statesString = myRulesResources.getString(rule);
		String[] states = statesString.split(",");
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
	public void fillStack(int sideLength) {
		myStack = new Stack<String>();
		for (int row = 0; row < sideLength; row++) {
			for (int col = 0; col < sideLength; col++) {
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
	public Element createWeightedRandomCells(int sideLength, String rule) {
		fillMap(rule);
		fillStack(sideLength);
		Element myCells = myDocument.createElement("Cells");
		for (String state : stateWeights.keySet()) {
			int numCells = (int) Math.round(((stateWeights.get(state) / 100) * (sideLength * sideLength)));
			for (int i = 0; i < numCells && !myStack.isEmpty(); i++) {
				String[] coordinates = myStack.pop().split(",");
				int row = Integer.parseInt(coordinates[0]);
				int col = Integer.parseInt(coordinates[1]);
				Element myCell = makeCellEntry(row, col, state);
				myCells.appendChild(myCell);
			}
		}
		return myCells;
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

	public void save(String rulesType, int sideLength, Cell[][] gameGrid, ArrayList<String> params, File myFile,
			String gridType) {
		myDocument = myBuilder.newDocument();
		Element saveConfig = getConfig(sideLength, gridType);
		Element saveRules = getRules(rulesType, params);
		Element saveCells = myDocument.createElement("Cells");
		for (int row = 0; row < gameGrid.length; row++) {
			for (int col = 0; col < gameGrid[row].length; col++) {
				Element myCell = makeCellEntry(row, col, gameGrid[row][col].getCurState());
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
			System.out.println("DONE");
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
		Element myCell = myDocument.createElement("Cell");
		Element xElement = myDocument.createElement("X");
		xElement.appendChild(myDocument.createTextNode("" + row));
		Element yElement = myDocument.createElement("Y");
		yElement.appendChild(myDocument.createTextNode("" + col));
		Element stateElement = myDocument.createElement("State");
		stateElement.appendChild(myDocument.createTextNode(state));
		myCell.appendChild(xElement);
		myCell.appendChild(yElement);
		myCell.appendChild(stateElement);
		return myCell;
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
		String[] resourcesParams = myRulesResources.getString(rule + "Parameters").split(",");
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

	public static void main(String[] args) {
		HashMap<String, Double> myMap = new HashMap<String, Double>();
		myMap.put("TREE", 90.0);
		myMap.put("BURNING", .25);
		XMLGenerator myGenerator = new XMLGenerator(myMap);
		myGenerator.generateFile(80, "Fire", "FireSt80.xml", "Standard");
	}
}
