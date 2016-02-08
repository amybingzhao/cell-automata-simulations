/**
 * @author Blake Kaplan
 * Generates XML files to be used to initialize simulations
 */

package XML;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import Model.Cell;

public class XMLGenerator {

	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private Document myDocument;

	public XMLGenerator() {
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			myDocument = myBuilder.newDocument();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
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
	public void generateFile(int rows, int cols, String rules, List<String> parameters, List<String> states, String filename) {

		try {

			Element myRoot = myDocument.createElement("Simulation");
			myDocument.appendChild(myRoot);

			myRoot.appendChild(getConfig(rows, cols));
			myRoot.appendChild(getRules(rules, parameters));
			myRoot.appendChild(createRandomCells(rows, cols, states));

			TransformerFactory myTransformerFactory = TransformerFactory.newInstance();
			Transformer myTransformer = myTransformerFactory.newTransformer();
			myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			myTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			myTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource mySource = new DOMSource(myDocument);
			StreamResult myResult = new StreamResult(new File("data/" + filename));
			myTransformer.transform(mySource, myResult);

		} catch (Exception e) {
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
	public Element createRandomCells(int rows, int cols, List<String> states) {
		Element myCells = myDocument.createElement("Cells");
		Random myRandom = new Random();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++){
				Element myCell = makeCellEntry(row, col, states.get(myRandom.nextInt(states.size())));
				myCells.appendChild(myCell);
			}
		}
		return myCells;
	}
	
	public void save(String rulesType, int rows, int cols, Cell[][] gameGrid, ArrayList<String> params){
		myDocument = myBuilder.newDocument();
		Element saveConfig = getConfig(rows,cols);
		Element saveRules = getRules(rulesType, params);
		Element saveCells = myDocument.createElement("Cells");
		for (int row = 0; row < gameGrid.length; row++){
			for (int col = 0; col < gameGrid[row].length; col++){
				Element myCell = makeCellEntry(row, col, gameGrid[row][col].getCurState());
				saveCells.appendChild(myCell);
			}
		}
		Element myRoot = myDocument.createElement("Simulation");
		myDocument.appendChild(myRoot);

		myRoot.appendChild(saveConfig);
		myRoot.appendChild(saveRules);
		myRoot.appendChild(saveCells);

		TransformerFactory myTransformerFactory = TransformerFactory.newInstance();
		Transformer myTransformer;
		try {
			
			myTransformer = myTransformerFactory.newTransformer();
			myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			myTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			myTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource mySource = new DOMSource(myDocument);
			StreamResult myResult = new StreamResult(new File("data/SavedGame.xml"));
			myTransformer.transform(mySource, myResult);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Element makeCellEntry(int row, int col, String state){
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

	public static void main(String[] args) {
		XMLGenerator myGenerator = new XMLGenerator();
		ArrayList<String> parameters = new ArrayList<String>();
		ArrayList<String> states = new ArrayList<String>();
		
		//Uncomment sections below according to desired simulation type
		
		//GAME OF LIFE
		states.add("ALIVE");
		states.add("DEAD");
		
		//FIRE
//		states.add("EMPTY");
//		states.add("TREE");
//		states.add("BURNING");
//		parameters.add("ProbCatch:10");
		
		//Segregation
//		states.add("RED");
//		states.add("BLUE");
//		states.add("EMPTY");
//		parameters.add("Threshold:30");
		
		//Predator Prey
//		states.add("WATER");
//		states.add("FISH");
//		states.add("SHARK");
//		parameters.add("InitialSharkEngery:15");
//		parameters.add("SharkReproductionTime:10");
//		parameters.add("FishReproductionTime:10");
		
		myGenerator.generateFile(20, 20, "GameOfLife", parameters, states, "GameOfLife4.xml");
	}

}
