package cellsociety_team21;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
	
	DocumentBuilderFactory myFactory;
	DocumentBuilder myBuilder;
	
	public void parse(File myFile){
		
		try {
			
			myFactory = DocumentBuilderFactory.newInstance();
			myBuilder = myFactory.newDocumentBuilder();
			Document myDocument = myBuilder.parse(myFile);
			myDocument.getDocumentElement().normalize();
			System.out.println("Root element :" 
		            + myDocument.getDocumentElement().getNodeName());
		         NodeList nList = myDocument.getElementsByTagName("student");
		         System.out.println("----------------------------");
		         for (int temp = 0; temp < nList.getLength(); temp++) {
		            Node nNode = nList.item(temp);
		            System.out.println("\nCurrent Element :" 
		               + nNode.getNodeName());
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		               Element eElement = (Element) nNode;
		               System.out.println("Student roll no : " 
		                  + eElement.getAttribute("rollno"));
		               System.out.println("First Name : " 
		                  + eElement
		                  .getElementsByTagName("firstname")
		                  .item(0)
		                  .getTextContent());
		               System.out.println("Last Name : " 
		               + eElement
		                  .getElementsByTagName("lastname")
		                  .item(0)
		                  .getTextContent());
		               System.out.println("Nick Name : " 
		               + eElement
		                  .getElementsByTagName("nickname")
		                  .item(0)
		                  .getTextContent());
		               System.out.println("Marks : " 
		               + eElement
		                  .getElementsByTagName("marks")
		                  .item(0)
		                  .getTextContent());
		            }
		         }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Cell> getCells(){
	
		ArrayList<Cell> myCells = new ArrayList<Cell>();
		
		return myCells;
		
	}
	
	public static void main(String[] args){
		XMLParser myParser = new XMLParser();
		myParser.parse(new File("data/samplexml.txt"));
	}

}
