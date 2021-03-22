package com.cigna.parsanpapiautomation.utilities;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.aventstack.extentreports.ExtentTest;
import com.cigna.parsanpapiautomation.reports.ExtentReportListner;

/**
 * @author M20589
 * XmlParserUtility class contains methods for parsing SANP xml response and get the values against input tags
 */
public class XmlParserUtility extends ExtentReportListner {

	ExtentTest logInfo = null;
	ArrayList<String> pricingDetailsList ;
	ArrayList<String> pricingTermsList ;

	public XmlParserUtility(ExtentTest lInfo) {

		this.logInfo = lInfo;
	}


	/**
	 * @author M20589
	 * getXMLValueWithTag() takes the response xml as input and parses xml and returns the Claim status,
	 * NetworxClaimNumber,Total Charge, Allowed Charge and Discount as ArrayList
	 * @param pTagName
	 * @param sanpXML
	 * @return ArrayList<String>
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 */
	public ArrayList<String> getXMLValueWithTag(String pTagName, String sanpXML)
			throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {

		pricingDetailsList = new ArrayList<String>();
		pricingTermsList= new ArrayList<String>();
		try {
			Document document = getXMLDocument(sanpXML, true);
			NodeList nodes = document.getElementsByTagName(pTagName);


			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					if(pTagName.equalsIgnoreCase("ns2:NetworXClaim")) {

						if(getValue("ClaimStatus", element).equalsIgnoreCase("1003")) {
							pricingDetailsList.add(getValue("ClaimStatus", element));
							pricingDetailsList.add(getValue("NetworxClaimNumber", element));
							pricingDetailsList.add(getValue("Description", element));
						}
						else if(getValue("ClaimStatus", element).equalsIgnoreCase("1005")) {
							pricingDetailsList.add(getValue("ClaimStatus", element));
							pricingDetailsList.add(getValue("NetworxClaimNumber", element));
							pricingDetailsList.add(getValue("TotalCharges", element));
							pricingDetailsList.add(getValue("AllowedCharges", element));
							pricingDetailsList.add(getValue("Savings", element));

						}
					}
					else if(pTagName.equalsIgnoreCase("PricingDetails"))
						pricingTermsList.add(getValue("ContractReference", element));

				}
			}
		}
		catch(Exception e) {
			logInfo.fail("Excpetion message: "+e.getMessage());
		}
		if(pTagName.equalsIgnoreCase("ns2:NetworXClaim"))
			return pricingDetailsList;
		else
			return pricingTermsList;
	}

	/*Created by: Rajalakshmi Ramesh  
	Created date: 
	Method Name: getXMLDocument
	Method Description: Method to return a xml document as a result for the given input xml string  
	Input Parameters: XML, Flag
	Return: Method returns a document created for the input XML string*/
	public Document getXMLDocument(String XML, boolean flag)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document;
		document = documentBuilder.parse(new InputSource(new StringReader(XML)));
		document.getDocumentElement().normalize();
		return document;
	}

	/*Created by: Rajalakshmi Ramesh  
	Created date: 
	Method Name: getValue
	Method Description: Method to return field value in the given tag of input string 
	Input Parameters: tag, element
	Return: Method returns a field value in the given tag of input input string*/
	static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

	/*Created by: Rajalakshmi Ramesh  
	Created date: 
	Method Name: getNodeListFromXPath
	Method Description: Method to return a Nodelist as a result for the given document and xpath  
	Input Parameters: Document, Xpath
	Return: Method returns a Nodelist as a result for the given document and xpath*/
	public NodeList getNodeListFromXPath(Document doc, String XPath) throws XPathExpressionException {

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile(XPath);
		NodeList tag = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return tag;
	}



	public static boolean stringContainsIgnoreCase(String str1, String str2) {

		if (str1.trim().toLowerCase().contains(str2.trim().toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean containsWords(String inputString, String[] items) {
		boolean found = true;
		for (String item : items) {
			if (!inputString.contains(item)) {
				found = false;
				break;
			}
		}
		return found;
	}
}
