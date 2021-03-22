package com.cigna.parsanpapiautomation.stepdefinition;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.cigna.parsanpapiautomation.apimethods.APIMethods;
import com.cigna.parsanpapiautomation.reports.ExtentReportListner;
import com.cigna.parsanpapiautomation.utilities.PropertiesFileReader;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


/**
 * @author M20589
 *
 */
public class StepDefinition extends ExtentReportListner{

	APIMethods apiMethodsObject ;
	PropertiesFileReader propertiesFileReader = new PropertiesFileReader();
	ExtentReportListner extentReportListner = new ExtentReportListner();

	@Given("^User posts the xml Request \"([^\"]*)\" to the SANP restapi endpoint$")
	public void user_posts_the_xml_Request_to_the_SANP_restapi_endpoint(String testScenarioID) throws Throwable  {

		ExtentTest logInfo = null;
		try {		
			extentTest = extentReports.createTest(Feature.class,
					"Report for scenario ID: "+testScenarioID);
			extentTest = extentTest.createNode(Scenario.class,
					"Price and Validate PAR SANP Claims");

			logInfo = extentTest.createNode(new GherkinKeyword("Given"), "User Posts the Xml request to Endpoint and Gets the Response");

			apiMethodsObject = new APIMethods(logInfo);
			apiMethodsObject.invokePOSTwebservice(testScenarioID,logInfo);

			logInfo.pass("USER ACTION ---> Successfully Posts the request to rest endpoint and recieved the response.");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", logInfo, e);
		}
	}


	@Then("^User validates the response for given xml payload$")
	public void user_validates_the_response_for_given_xml_payload() throws Throwable {

		ExtentTest logInfo = null;
		try {
			logInfo = extentTest.createNode(new GherkinKeyword("Then"), "User validates the response for given xml payload");
			apiMethodsObject.validateWebServicePostResponse(logInfo);
		}catch (AssertionError | Exception e) {
			testStepHandle("FAIL", logInfo, e);
		}

	}

}
