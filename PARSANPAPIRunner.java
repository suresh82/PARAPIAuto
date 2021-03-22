package com.cigna.parsanpapiautomation.testrunner;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cigna.parsanpapiautomation.reports.ExtentReportListner;
import com.cigna.parsanpapiautomation.reports.ReportHelper;
import com.cigna.parsanpapiautomation.utilities.PropertiesFileReader;
import com.cigna.parsanpapiautomation.utilities.ReadExcelTestData;
import com.cigna.parsanpapiautomation.utilities.ReadFilesFromFolder;
import com.codoid.products.exception.FilloException;

import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

@cucumber.api.CucumberOptions(features = "src/test/resources/Feature/",
glue = {"com.cigna.parsanpapiautomation.stepdefinition"},
monochrome = true,
plugin = { "pretty", "html:target/cucumber-reports/index.html", "json:target/cucumber-reports/CucumberTestReport.json", "rerun:target/cucumber-reports/rerun.txt", "com.epam.reportportal.cucumber.StepReporter"})


/*Created by: Suresh M 
Created date: 
Class Name: MRCAPIRunner
Class Description: This class is the runner class to glue the feature file and step definition file to execute the scenarios
*/
public class PARSANPAPIRunner {

	ReadExcelTestData readExcelTestData = new ReadExcelTestData();
	ReadFilesFromFolder readFilesFromFolder = new ReadFilesFromFolder();
	PropertiesFileReader obj = new PropertiesFileReader();
	private TestNGCucumberRunner testNGCucumberRunner;
	
	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: setUpClass
	Method Description: Method to perform initial setup for the cucumber runner before the execution starts
	Input Parameters:
	Return: Method does not return anything*/

	@BeforeClass(alwaysRun = true)
	public void setUpClass() throws Exception {
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: setTestData
	Method Description: Method to setup the test data before the execution starts
	Input Parameters:
	Return: Method does not return anything*/

	@BeforeClass
	public void setTestData() throws FilloException, IOException {

		readFilesFromFolder.readAllFiles();
		readExcelTestData.readExcel();

	}

	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: feature
	Method Description: Method to setup the feature using CucumberFeatureWrapper to be executed
	Input Parameters:CucumberFeatureWrapper
	Return: Method does not return anything
	@Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")*/

	@Test(dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) {

		testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	}

	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: features
	Method Description: Method to setup the feature files to be executed as data
	Input Parameters:
	Return: Method returns the feature files to be executed*/

	@DataProvider
	public Object[][] features() {
		return testNGCucumberRunner.provideFeatures();
	}

	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: writeExtentReport
	Method Description: Method to update/generate extent report after the execution completes
	Input Parameters:
	Return: Method does not return anything*/
	@AfterClass(alwaysRun = true)
	public void writeExtentReport() throws Exception {
		testNGCucumberRunner.finish();
	}

	/*Created by: Rakhi Bisht
	Created date: 
	Method Name: deleteExamples
	Method Description: Method to delete the input given to feature file to prepare for next run
	Input Parameters:
	Return: Method does not return anything*/
	@AfterClass
	public void deleteExamples() throws IOException {
		readExcelTestData.deleteExamples();
	}

	/*Created by: Rakhi Bisht
	Created date: 
	Method Name: deleteTest
	Method Description: Method to delete the input given to testdata excel to prepare for next run
	Input Parameters:
	Return: Method does not return anything*/
	@AfterClass
	public void deleteTest() throws IOException {
		readFilesFromFolder.deleteDataFromExcel();
	}



	/*Created by: Rajalakshmi Ramesh
	Created date: 
	Method Name: generateHTMLReports
	Method Description: Method to update/generate cucumber report after the execution completes
	Input Parameters:
	Return: Method does not return anything*/
	@AfterSuite(alwaysRun = true)
	public void generateHTMLReports() {
		ReportHelper.generateCucumberReport();
	}
} 



