package cellsociety_team21;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XMLGenerator {

	private DocumentBuilderFactory myFactory;
	private DocumentBuilder myBuilder;
	private Document myDocument;
	private List<Integer> xLocations;
	private List<Integer> yLocations;

	public XMLGenerator() {
		try {
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			myDocument = myBuilder.newDocument();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		xLocations = new ArrayList<Integer>();
		yLocations = new ArrayList<Integer>();
	}

	public void generateFile(int rows, int cols, String gameName, List<String> parameters, List<String> states, int numCells) {

		if (numCells > rows * cols){
			System.out.println("Too Many Cells");
			return;
		}
		
		try {

			Element myRoot = myDocument.createElement("Simulation");
			myDocument.appendChild(myRoot);

			myRoot.appendChild(getConfig(rows, cols));
			myRoot.appendChild(getGameType(gameName, parameters));
			myRoot.appendChild(createRandomCells(numCells, rows, cols, states));

			TransformerFactory myTransformerFactory = TransformerFactory.newInstance();
			Transformer myTransformer = myTransformerFactory.newTransformer();
			myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			myTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			myTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource mySource = new DOMSource(myDocument);
			StreamResult myResult = new StreamResult(new File("data/simulation.txt"));
			myTransformer.transform(mySource, myResult);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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

	public Element getGameType(String game, List<String> params){
		
		Element gameElement = myDocument.createElement("Game");
		Element myName = myDocument.createElement("Name");
		myName.appendChild(myDocument.createTextNode(game));
		Element myParams = parametersAsElement(params);
		gameElement.appendChild(myName);
		gameElement.appendChild(myParams);
		return gameElement;
		
	}
	
	public Element parametersAsElement(List<String> parameters){
		
		Element paramElement = myDocument.createElement("Parameters");
		for (String param : parameters){
			String[] splitParam = param.split(":");
			Element paramType = myDocument.createElement(splitParam[0]);
			paramType.appendChild(myDocument.createTextNode(splitParam[1]));
			paramElement.appendChild(paramType);
		}	
		return paramElement;
	}
	
	public Element createRandomCells(int numCells, int rows, int cols, List<String> states){	
		Element myCells = myDocument.createElement("Cells");
		Random myRandom = new Random();
		for (int i = 0; i < numCells; i++){		
			Element myCell = myDocument.createElement("Cell");
			Element xElement = myDocument.createElement("X");
			int x;
			int y;
			while (true){
				x = myRandom.nextInt(rows);
				if (!xLocations.contains(x)) break;
			}
			while (true){
				y = myRandom.nextInt(cols);
				if (!yLocations.contains(y)) break;
			}
			xElement.appendChild(myDocument.createTextNode("" + x));
			Element yElement = myDocument.createElement("Y");
			yElement.appendChild(myDocument.createTextNode("" + y));
			Element stateElement = myDocument.createElement("State");
			stateElement.appendChild(myDocument.createTextNode(states.get(myRandom.nextInt(states.size()))));
			myCell.appendChild(xElement);
			myCell.appendChild(yElement);
			myCell.appendChild(stateElement);
			myCells.appendChild(myCell);
		}
		return myCells;	
	}

	public static void main(String[] args) {
		XMLGenerator myGenerator = new XMLGenerator();
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("Threshold:40");
		ArrayList<String> states = new ArrayList<String>();
		states.add("Black");
		states.add("White");
		myGenerator.generateFile(50, 50, "Segregation", parameters, states, 10);
	}

}
