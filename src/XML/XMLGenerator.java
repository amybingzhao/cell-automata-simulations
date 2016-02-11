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

	public XMLGenerator() {
		myRulesResources = ResourceBundle.getBundle(RULES_PROPERTIES);
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			myDocument = myBuilder.newDocument();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public XMLGenerator(HashMap<String, Double> myWeights) {
		this();
		stateWeights = myWeights;
		weighted = true;
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
	public void generateFile(int rows, int cols, String rules, String fileName) {

		try {
			myDocument = myBuilder.newDocument();
			Element myRoot = myDocument.createElement("Simulation");
			myDocument.appendChild(myRoot);
			myRoot.appendChild(getConfig(rows, cols));
			myRoot.appendChild(getRules(rules, promptForParameters(rules)));
			if (weighted) {
				myRoot.appendChild(createWeightedRandomCells(rows, cols, rules + "States"));
			} else {
				myRoot.appendChild(createRandomCells(rows, cols, rules + "States"));
			}
			createFile(new File("data/" + fileName));
		} catch (Exception e) {
			System.out.println("OOPS");
			e.printStackTrace();
		}
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
	public Element getConfig(int rows, int cols) {

		Element configElement = myDocument.createElement("Config");
		Element myRows = myDocument.createElement("Rows");
		myRows.appendChild(myDocument.createTextNode("" + rows));
		Element myCols = myDocument.createElement("Columns");
		myCols.appendChild(myDocument.createTextNode("" + cols));
		configElement.appendChild(myRows);
		configElement.appendChild(myCols);

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
	public Element createRandomCells(int rows, int cols, String rule) {
		String statesString = myRulesResources.getString(rule);
		String[] states = statesString.split(",");
		Element myCells = myDocument.createElement("Cells");
		Random myRandom = new Random();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Element myCell = makeCellEntry(row, col, states[(myRandom.nextInt(states.length))]);
				myCells.appendChild(myCell);
			}
		}
		return myCells;
	}

	public void fillMap(String rule){
		String statesString = myRulesResources.getString(rule);
		String[] states = statesString.split(",");
		double sum = 0;
		ArrayList<String> statesNotIncluded = new ArrayList<String>();
		for (String state : states){
			if (stateWeights.containsKey(state)){
				sum += (int) Math.round(stateWeights.get(state));
			}
			else{
				statesNotIncluded.add(state);
			}
		}
		for (String state : statesNotIncluded){
			stateWeights.put(state, (100 - sum) / statesNotIncluded.size());
		}
	}
	
	public void fillStack(int rows, int columns){
		myStack = new Stack<String>();
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < columns; col++){
				myStack.push(row +"," + col);
			}
		}
		Collections.shuffle(myStack);
	}
	
	public Element createWeightedRandomCells(int rows, int cols, String rule) {
		fillMap(rule);
		fillStack(rows, cols);
		Element myCells = myDocument.createElement("Cells");
		for (String state : stateWeights.keySet()){
			int numCells = (int) Math.round(((stateWeights.get(state)/100) * (rows*cols)));
			for (int i = 0; i < numCells && !myStack.isEmpty(); i++){
				String[] coordinates = myStack.pop().split(",");
				int row = Integer.parseInt(coordinates[0]);
				int col = Integer.parseInt(coordinates[1]);
				Element myCell = makeCellEntry(row, col, state);
				myCells.appendChild(myCell);
			}
		}
//		if (myStack.isEmpty()) return myCells;
//		Random myRandom = new Random();
//		String statesString = myRulesResources.getString(rule);
//		String[] states = statesString.split(",");
//		while(!myStack.isEmpty()){
//			String[] coordinates = myStack.pop().split(",");
//			int row = Integer.parseInt(coordinates[0]);
//			int col = Integer.parseInt(coordinates[1]);
//			Element myCell = makeCellEntry(row, col, states[myRandom.nextInt(states.length)]);
//			myCells.appendChild(myCell);
//		}
//			
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
	public void save(String rulesType, int rows, int cols, Cell[][] gameGrid, ArrayList<String> params, File myFile) {
		myDocument = myBuilder.newDocument();
		Element saveConfig = getConfig(rows, cols);
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
			// TODO Auto-generated catch block
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
	public ArrayList<String> promptForParameters(String rule) {
		ArrayList<String> parameters = new ArrayList<String>();
		String[] resourcesParams = myRulesResources.getString(rule + "Parameters").split(",");
		Scanner myScanner = new Scanner(System.in);
		for (String param : resourcesParams) {
			if (param.equals("NONE")) break;
			System.out.println("Choose the value of " + param);
			int value = myScanner.nextInt();
			parameters.add(param + ":" + value);
		}
		myScanner.close();
		return parameters;
	}

	public static void main(String[] args) {
		HashMap<String, Double> myMap = new HashMap<String, Double>();
		myMap.put("ALIVE", 40.0);
//		myMap.put("BURNING", .5);
		XMLGenerator myGenerator = new XMLGenerator(myMap);
		myGenerator.generateFile(80, 80, "GameOfLife", "GOL.xml");
	}

}
