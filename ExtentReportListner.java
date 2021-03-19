package com.cigna.parsanpapiautomation.reports;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.cigna.parsanpapiautomation.utilities.PropertiesFileReader;

/*Created by: Rakhi Bisht 
Created date: January 2020
Class Name: ExtentReportListner
Class Description: This class includes methods to perform the initial settings for Extent report
 */

public class ExtentReportListner  {
	public static ExtentHtmlReporter extentHTMLReporter = null;
	public static ExtentReports extentReports = null;
	public static ExtentTest extentTest = null;
	public static Properties properties;
	public static PropertiesFileReader propertiesFileReader = new PropertiesFileReader();


	
	public ExtentReportListner() {

		try {
			properties = propertiesFileReader.getProperty();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: setup()
	Method Description: Method to do the general settings for Extent report
	Input Parameters: 
	Return: Method returns extent report with the settings performed in the method and details of scenario executed */
	public static ExtentReports setup() {

		// Archieve the old extent reports file to ArchieveReports Folder
		archieveExtentReports();
		try {
			
			String location = properties.getProperty("EXTENT_REPORTS_PATH") + getcurrentdateandtime() + ".html";
			extentHTMLReporter = new ExtentHtmlReporter(location);
			extentHTMLReporter.config().setDocumentTitle("Execute Automation Report");
			extentHTMLReporter.config().setReportName("Execute Automation Report");
			extentHTMLReporter.config().setTheme(Theme.STANDARD);

			extentHTMLReporter.start();

			extentReports = new ExtentReports();
			extentReports.attachReporter(extentHTMLReporter);
			extentReports.setSystemInfo("Application", "Execute Automation");
			extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
			extentReports.setSystemInfo("User", System.getProperty("user.name"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return extentReports;
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: testStepHandle
	Method Description: Method to handle the test step status and corresponding settings
	Input Parameters: Teststatus(Status of the step executed), WebDriver,ExtentTest,Throwable(Exception)
	Return: Method does not return anything */
	public static void testStepHandle(String teststatus,ExtentTest extenttest, Throwable throwable) {
		switch (teststatus) {
		case "FAIL":
			extenttest.fail(MarkupHelper.createLabel("Test Case is Failed :", ExtentColor.RED));
			extenttest.error(throwable.fillInStackTrace());                  

			break;

		case "PASS":
			extenttest.pass(MarkupHelper.createLabel("Test Case is Passed :", ExtentColor.GREEN));
			break;

		default:
			break;
		}

	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: getCurrentDateandTime
	Method Description: Method to return current date and time
	Input Parameters:
	Return: Method returns current date and time as string */
	private static String getcurrentdateandtime() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss");
			Date date = new Date();
			str = dateFormat.format(date);
			str = str.replace("", "").replaceAll("/", "").replaceAll(":", "");
		} catch (Exception e) {

		}
		return str;
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: archieveExtentReports
	Method Description: Method to Archive the old extent reports file to ArchieveReports Folder
	Input Parameters:
	Return: Method returns boolean value as true if the older extent reports are archived successfully*/
	private static boolean archieveExtentReports() {
		boolean isArchieved = false;

		try {

			String archieveReportsSourcePath = properties.getProperty("EXTENT_REPORTS_PATH");
			String archieveReportsDestinationPath = properties.getProperty("EXTENT_REPORTS_ARCHIEVED_PATH");
			String[] listOfReports = new File(archieveReportsSourcePath).list();// listFiles();
			for (String reportName : listOfReports) {
				if (reportName.contains(".html")) {
					Path sourcePath = Paths.get(archieveReportsSourcePath + "/" + reportName);
					Path destinationPath = Paths.get(archieveReportsDestinationPath + "/" + reportName);
					Files.move(sourcePath, destinationPath);
					isArchieved = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isArchieved = false;
		}
		return isArchieved;
	}

}
