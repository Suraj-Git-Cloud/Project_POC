package com.robobank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.robobank.domain.CustomerStatement;
import com.robobank.exceptions.InvalidDataException;

@Repository
public class StatmentUtils {

	private static final Logger log = LoggerFactory.getLogger(StatmentUtils.class);

	public List<CustomerStatement> initializeDataFromFile(Path statementsfilePath) throws IOException {
		 log.info("In initializeDataFromFile() StatmentUtils ");
		 List<CustomerStatement> statements =  new ArrayList<>();
		 BufferedReader br = new BufferedReader(new FileReader(statementsfilePath.toString()));
		
		try {
			
			String line = null;		
			int count=0;
			while ((line = br.readLine()) != null) {
				count++;
				if(count==1) {
					continue;
				}
				
				String statement[] = line.trim().split(",");
				
				if(statement[0].trim().isEmpty() || statement[1].trim().isEmpty() || statement[2].trim().isEmpty() || statement[3].trim().isEmpty() || statement[4].trim().isEmpty() || statement[5].trim().isEmpty())
				{
					String msg = "";
					if(statement[0].trim().isEmpty())
					{
						msg ="Missing Reference field data in Entry || File Corrupt";
					}
					else
					{
						msg = "Missing Filed Data Entries in File || File Corrupt";
					}
					throw new InvalidDataException(msg);
				}
				
				CustomerStatement cust = new CustomerStatement();
				cust.setReference(Integer.parseInt(statement[0].trim()));
				cust.setAccount_Number(statement[1].trim());
				cust.setDescription(statement[2].trim());
				cust.setStart_Balance(Double.parseDouble(statement[3].trim()));
				cust.setMutation(Double.parseDouble(statement[4].trim()));
				cust.setEnd_Balance(Double.parseDouble(statement[5].trim()));
				statements.add(cust);
			}

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		finally {
			br.close();
		}
		return statements;
	}

	public List<CustomerStatement> initializeDataFile(Path statementsfilePath) throws Exception {
		log.info("In initializeDataFile() StatmentUtils ");
		List<CustomerStatement> statements = new ArrayList<>();
		File xmlFile = new File(statementsfilePath.toString());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			String xpathExpression = "";
			xpathExpression = "/records/record/@reference";
			XPathExpression expr = xpath.compile(xpathExpression);
			NodeList nodesList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodeList = doc.getElementsByTagName("record");	
			if(nodesList==null && nodeList==null ) {
				throw new InvalidDataException("Invalid Data");
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				CustomerStatement customerStatement = new CustomerStatement();
				String reference = nodesList.item(i).getNodeValue();
				if(reference == null || reference.isEmpty())
				{
					throw new InvalidDataException("Missing Reference field data in Entry || File Corrupt");
				}
				
				if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodeList.item(i);
				
					
					customerStatement.setReference(Integer.parseInt(reference));
					customerStatement.setAccount_Number((getTagValue("accountNumber", element)));
					customerStatement.setDescription((getTagValue("description", element)));
					customerStatement.setStart_Balance(Double.parseDouble(getTagValue("startBalance", element)));
					customerStatement.setMutation((Double.parseDouble(getTagValue("mutation", element))));
					customerStatement.setEnd_Balance(Double.parseDouble(getTagValue("endBalance", element)));
					/*
					 * if(customerStatement.getAccount_Number().isEmpty() ||
					 * customerStatement.getDescription().isEmpty()) { throw new
					 * InvalidDataException("Invalid Data"); }
					 */
				}			
				statements.add(customerStatement);
			}
		} catch (SAXException | ParserConfigurationException | IOException e1) {
			log.error(e1.getMessage());
		}

		return statements;
	}
	

	private String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		if(node.getNodeValue()==null || node.getNodeValue().isEmpty())
		{
			throw new InvalidDataException("Missing Filed Data Entries in File || File Corrupt");
		}
		return node.getNodeValue();
	}
}
