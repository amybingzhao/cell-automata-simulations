/**
 * @author Blake Kaplan
 * Parses simulation XML files that define the initial simulation setup
 */

package XML;

import org.w3c.dom.*;

import Rules.FireRules;
import Rules.GameOfLifeRules;
import Rules.PredatorPreyRules;
import Rules.Rules;
import Rules.SegregationRules;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private String[][] cellGrid;
	private int rows;
	private int cols;
	private String gridType;
	private Rules myRule;

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
						List<String> myConfig = extract(entryElement);
						rows = Integer.parseInt(splitEntry(myConfig.get(0))[1]);
						cols = Integer.parseInt(splitEntry(myConfig.get(1))[1]);
						gridType = splitEntry(myConfig.get(2))[1];
						cellGrid = new String[rows][cols];
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
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		default:
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
		for (int i = 0; i < dataList.getLength(); i++) {
			Node dataNode = dataList.item(i);
			if (dataNode instanceof Element) {
				Element dataElement = (Element) dataNode;
				List<String> extractedData = extract(dataElement);
				int x = Integer.parseInt(splitEntry(extractedData.get(0))[1]);
				int y = Integer.parseInt(splitEntry(extractedData.get(1))[1]);
				String state = splitEntry(extractedData.get(2))[1];
				cellGrid[x][y] = state;
			}
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
	
	public String getGridType(){
		return gridType;
	}
}
