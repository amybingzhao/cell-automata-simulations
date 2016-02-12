/**
 * @author Blake Kaplan
 * Parses simulation XML files that define the initial simulation setup
 */

package XML;

import org.w3c.dom.*;

import Controller.Simulation;
import Rules.FireRules;
import Rules.GameOfLifeRules;
import Rules.PredatorPreyRules;
import Rules.Rules;
import Rules.SegregationRules;
import View.CSView;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class XMLParser {

	private static final String RULE_TYPES = "RuleTypes";
	private static final String STATE_TYPE = "StateType";
	private static final String STATES = "States";
	private static final String GRID_TYPES = "GridTypes";
	private static final String INVALID_GRID_TYPE = "InvalidGridType";
	private static final String FILE_TYPE = "FileType";
	private static final String OUT_OF_BOUNDS = "OutOfBounds";
	private static final String RULES_PROPERTIES = "Rules/Rules";
	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private String[][] cellGrid;
	private int rows;
	private int cols;
	private String gridType;
	private Rules myRule;
	private ResourceBundle myRules;
	private Simulation mySimulation;
	
	public XMLParser(Simulation sim){
		mySimulation = sim;
		myRules = ResourceBundle.getBundle(RULES_PROPERTIES);
	}

	/**
	 * Parses a provided XML file to extract the simulation data
	 * 
	 * @param myFile
	 *            A provided XML file containing the simulation data
	 */
	public void parse(File myFile) {
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			Document myDocument = myBuilder.parse(myFile);
			myDocument.getDocumentElement().normalize();
			NodeList categories = myDocument.getDocumentElement().getChildNodes();
			for (int i = 0; i < categories.getLength(); i++) {
				Node entry = categories.item(i);
				if (entry instanceof Element) {
					Element entryElement = (Element) entry;
					switch (entryElement.getNodeName()) {
					case "Config":
						if (!parseConfig(entryElement)) return;
						break;
					case "Cells":
						extractCells(entryElement);
						break;
					case "Game":
						List<String> data = extract(entryElement);
						initializeGame(data);
						break;
					}
				}
			}
		} catch (Exception IOException) {
			mySimulation.displayAlert(FILE_TYPE);
			return;
		}
	}
	
	public boolean parseConfig(Element entryElement){
		List<String> myConfig = extract(entryElement);
		rows = Integer.parseInt(splitEntry(myConfig.get(0))[1]);
		cols = Integer.parseInt(splitEntry(myConfig.get(1))[1]);
		gridType = splitEntry(myConfig.get(2))[1];
		if (!exists(gridType, GRID_TYPES)){
			mySimulation.displayAlert(INVALID_GRID_TYPE);
			return false;
		}
		cellGrid = new String[rows][cols];
		return true;
	}

	private boolean exists(String item, String category){
		ArrayList<String> types = new ArrayList<String>();
		String[] items = myRules.getString(category).split(",");
		for (String myItem : items){
			types.add(myItem);
		}
		if (types.contains(item)) return true;
		return false;
	}

	/**
	 * Extract data related to the game details
	 * 
	 * @param data
	 *            A Java element containing game data
	 */
	public List<String> extract(Element data) {
		ArrayList<String> myGame = new ArrayList<String>();
		NodeList dataList = data.getChildNodes();
		for (int i = 0; i < dataList.getLength(); i++) {
			Node dataNode = dataList.item(i);
			if (dataNode instanceof Element) {
				Element dataElement = (Element) dataNode;
				if (dataElement.getNodeName() == "Parameters") {
					List<String> extractedData = extract(dataElement);
					for (String entry : extractedData) {
						myGame.add(entry);
					}
				} else {
					myGame.add(dataElement.getNodeName() + ":" + dataElement.getTextContent());
				}
			}
		}
		return myGame;
	}

	/**
	 * Creates a Rules object specific to the game type with the proper
	 * parameters
	 * 
	 * @param data
	 *            A string arraylist containing the data to be interpreted
	 */
	public void initializeGame(List<String> data) {
		String game = splitEntry(data.get(0))[1];
		if (!exists(game, RULE_TYPES)){
			mySimulation.displayAlert("RuleType");
			return;
		}
		switch (game) {
		case "Segregation":
			double threshold = Double.parseDouble(splitEntry(data.get(1))[1]);
			myRule = new SegregationRules(threshold);
			break;
		case "PredatorPrey":
			int initialSharkEnergy = Integer.parseInt(splitEntry(data.get(1))[1]);
			int sharkReproductionTime = Integer.parseInt(splitEntry(data.get(2))[1]);
			int fishReproductionTime = Integer.parseInt(splitEntry(data.get(3))[1]);
			myRule = new PredatorPreyRules(initialSharkEnergy, sharkReproductionTime, fishReproductionTime);
			break;
		case "GameOfLife":
			myRule = new GameOfLifeRules();
			break;
		case "Fire":
			double probCatch = Double.parseDouble(splitEntry(data.get(1))[1]);
			myRule = new FireRules(probCatch);
			break;
		}
	}

	/**
	 * Extracts the information about the cells from the XML file and configures
	 * the cell grid accordingly
	 * 
	 * @param data
	 *            A Java element containing the data about the cells from the
	 *            XML
	 */
	public void extractCells(Element data) {
		NodeList dataList = data.getChildNodes();
		try {
			for (int i = 0; i < dataList.getLength(); i++) {
				Node dataNode = dataList.item(i);
				if (dataNode instanceof Element) {
					Element dataElement = (Element) dataNode;
					List<String> extractedData = extract(dataElement);
					int x = Integer.parseInt(splitEntry(extractedData.get(0))[1]);
					int y = Integer.parseInt(splitEntry(extractedData.get(1))[1]);
					String state = splitEntry(extractedData.get(2))[1];
					if (!exists(state, myRule.toString()+STATES)){
						mySimulation.displayAlert(STATE_TYPE);
						return;
					}
					cellGrid[x][y] = state;
				}
			}
		}
		
		catch (Exception OutOfBoundsException){
			mySimulation.displayAlert(OUT_OF_BOUNDS);
		}
	}

	/**
	 * Splits a string using the : delimiter
	 * 
	 * @param entry
	 *            A string to be split
	 * @return A string array containing the split string
	 */
	public String[] splitEntry(String entry) {
		String[] split = entry.split(":");
		return split;
	}

	/**
	 * Provides the number of rows
	 * 
	 * @return The number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Provides the number of columns
	 * 
	 * @return The number of columns
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Provides an array containing the cell states by location in a grid
	 * 
	 * @return The cell state grid
	 */
	public String[][] getGrid() {
		return cellGrid;
	}

	/**
	 * Provides a Rules object related to the specific game type specified in
	 * the XML
	 * 
	 * @return The Rules object
	 */
	public Rules getRules() {
		return myRule;
	}

	public String getGridType() {
		return gridType;
	}
}
