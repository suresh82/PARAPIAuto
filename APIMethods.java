
package com.cigna.parsanpapiautomation.apimethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.eclipse.jetty.io.ssl.ALPNProcessor.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jayway.jsonpath.JsonPath;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.cigna.parsanpapiautomation.reports.ExtentReportListner;
import com.cigna.parsanpapiautomation.utilities.PropertiesFileReader;
import com.cigna.parsanpapiautomation.utilities.ReadExcelForClaimData;
import com.cigna.parsanpapiautomation.utilities.XmlParserUtility;
import com.codoid.products.exception.FilloException;

import io.restassured.RestAssured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.config.ConnectionConfig;

/**
 * @author M20589
 * APIMethods class contains methods for post SANP Xml request to the endpoint and validates claim pricing response
 */
@SuppressWarnings("unused")
public class APIMethods extends ExtentReportListner{
	ExtentTest logInfo = null;

	@SuppressWarnings("static-access")
	public APIMethods(ExtentTest lInfo) {
		this.logInfo = lInfo;
	}

	PropertiesFileReader propertiesFileReader = new PropertiesFileReader();
	public static String sanpResponseXml;
	XmlParserUtility xmlParserUtility;
	String[] temporaryArray;

	ArrayList<String> claimResponseActualData=new ArrayList<String>();
	ArrayList<String> claimResponseTerms = new ArrayList<String>();

	ReadExcelForClaimData claimExpectedData = new ReadExcelForClaimData();
	ArrayList<String> claimRequestData = new ArrayList<String>();


	/**
	 * @author M20589
	 * InvokePOSTwebservice() Method invokes MRC SANP rest service and post the SANP xml request to the end point
	 * And receives the response as string
	 * @param testScenarioID
	 * @param logInfo
	 * @return void
	 * @throws IOException
	 */
	public void invokePOSTwebservice(String testScenarioID,ExtentTest logInfo) throws IOException {
		properties = propertiesFileReader.getProperty();

		try {
			HashMap<String,String> params = new HashMap<String,String>(); 

			/*given().when().get(properties.getProperty("URL")).then().assertThat().body(arg0, arg1);*/
			RestAssured.baseURI = properties.getProperty("URL");
			params.put("X-TZ-NwxRole", "PRICER");
			params.put("X-TZ-NwxUserName", "SELECTEAI");
			params.put("X-TZ-NwxOwnerId", "52");
			params.put("Content-Type","application/xml");

			RequestSpecification httpRequest= RestAssured.given().headers(params);
			claimRequestData=claimExpectedData.claimExpResultData(Integer.parseInt(testScenarioID));

			// This line of code does the job posting the request to rest endpoint and gets the response as string from rest service
			sanpResponseXml = httpRequest.body(new File(claimRequestData.get(0))).when().post(properties.getProperty("Resource")).then().extract().asString(); 


		} catch (Exception e) {
			logInfo.fail("Excpetion Occured: "+e.getMessage());
		}

	}


	/**
	 * @author M20589
	 * ValidateWebServicePostResponse() Method verifies the pricing detail validation of MRC SANP Claim
	 * @param logInfo
	 * @return void
	 * @throws IOException
	 * @throws ParseException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws FilloException
	 */
	public void validateWebServicePostResponse(ExtentTest logInfo)
			throws IOException, ParseException, XPathExpressionException, ParserConfigurationException, SAXException, FilloException {

		try {

			xmlParserUtility = new XmlParserUtility(logInfo);

			claimResponseActualData=xmlParserUtility.getXMLValueWithTag("ns2:NetworXClaim", sanpResponseXml); // "ns2:NetworXClaim" is the root node/tag in the response xml

			if(claimResponseActualData.get(0).equalsIgnoreCase("1005")) { // Claim status 1005 means claim priced without any errors

				claimResponseTerms=xmlParserUtility.getXMLValueWithTag("PricingDetails", sanpResponseXml);

				logInfo.pass("Claim Status: "+claimResponseActualData.get(0));
				logInfo.pass("NetworxClaimNumber: "+claimResponseActualData.get(1));

				assertEquals(claimResponseActualData.get(2), claimRequestData.get(1)); //Total Charges Validation
				logInfo.pass("Actual Total charge in response: "+claimResponseActualData.get(2)+ "  Matched with Expected Total Charge: "+claimRequestData.get(1));

				assertEquals(claimResponseActualData.get(3), claimRequestData.get(2));//allowed amount validation
				logInfo.pass("Actual Allowed Amount in response: "+claimResponseActualData.get(3)+ "  Matched with Expected Allowed Amount: "+claimRequestData.get(2));

				assertEquals(claimResponseActualData.get(4), claimRequestData.get(3)); // Savings or discount validation
				logInfo.pass("Actual Discount in response: "+claimResponseActualData.get(4)+ "  Matched with Expected Discount: "+claimRequestData.get(3));


				temporaryArray =null;
				temporaryArray=claimRequestData.get(4).split("\\n");

				TreeSet<String> requestTreeSet = new TreeSet<String>();
				TreeSet<String> responseTreeSet = new TreeSet<String>();

				// reqSet and respSet contains the expected terms to be hit and actual terms are being hit respectively
				requestTreeSet.addAll(Arrays.asList(temporaryArray));
				responseTreeSet.addAll(claimResponseTerms);

				assertEquals(requestTreeSet, responseTreeSet); // Terms validation
				logInfo.pass("Actual Tetms hit in claim response: "+requestTreeSet.toString()+ "  Matched with Expected Terms to be hit: "+ responseTreeSet.toString());

			} 
			else if(claimResponseActualData.get(0).equalsIgnoreCase("1003")) { // Claim status 1003 means claim pricing got error-ed out

				logInfo.fail("Claim Status: "+claimResponseActualData.get(0));
				logInfo.fail("NetworxClaimNumber: "+claimResponseActualData.get(1));
				logInfo.fail("Claim got errored out and error description: "+claimResponseActualData.get(2));
			}

		}catch(Exception e) {
			logInfo.fail("Exception occured: "+e.getMessage());
		}

	}

}
