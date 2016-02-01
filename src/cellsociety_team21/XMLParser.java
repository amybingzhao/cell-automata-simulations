package cellsociety_team21;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

	DocumentBuilderFactory myFactory;
	DocumentBuilder myBuilder;
	String[][] cellGrid;

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
					switch(entryElement.getNodeName()){
						case "Config":
							ArrayList<String> myConfig = extract(entryElement);
							System.out.println(myConfig);
							break;
						case "Game":
							ArrayList<String> myGame = extractGame(entryElement);
							System.out.println(myGame);
							break;
						default:
							System.out.println("Not Config");
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<String> extract(Element data) {

		ArrayList<String> myConfig = new ArrayList<String>();
		NodeList dataList = data.getChildNodes();
		for (int i = 0; i < dataList.getLength(); i++) {
			Node dataNode = dataList.item(i);
			if (dataNode instanceof Element){
				Element dataElement = (Element) dataNode;
				myConfig.add(dataElement.getNodeName() + ":" + dataElement.getTextContent());
			}
		}
		return myConfig;
	}
	
	public ArrayList<String> extractGame(Element data){
		
		ArrayList<String> myGame = new ArrayList<String>();
		NodeList dataList = data.getChildNodes();
		for (int i = 0; i < dataList.getLength(); i++){
			Node dataNode = dataList.item(i);
			if (dataNode instanceof Element){
				Element dataElement = (Element) dataNode;
				if (dataElement.getNodeName() == "Parameters"){ 
					ArrayList<String> extractedData = extract(dataElement);
					for (String entry : extractedData){
						myGame.add(entry);
					}
				}
				else{
					myGame.add(dataElement.getNodeName() + ":" + dataElement.getTextContent());
				}
			}
		}
		return myGame;
	}

	public static void main(String[] args) {
		XMLParser myParser = new XMLParser();
		myParser.parse(new File("data/simulation.txt"));
	}

}
