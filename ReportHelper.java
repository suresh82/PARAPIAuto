package com.cigna.parsanpapiautomation.reports;

import java.io.File;
import java.util.ArrayList;



import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

/*Created by: Rakhi Bisht 
Created date: November 2019
Class Name: ReportHelper
Class Description: This class includes methods to perform the initial settings for Cucumber report
*/
public class ReportHelper {
	
	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: generateCucumberReport
	Method Description: Method to arrange the features and settings for generating cucumber report
	Input Parameters: 
	Return: Method does not return anything*/
	public static void generateCucumberReport() {
        File reportOutputDirectory = new File("target");
        ArrayList<String> jsonFiles = new ArrayList<String>();
        jsonFiles.add("target/cucumber-reports/CucumberTestReport.json");

        String projectName = "testng-cucumber";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("Browser", "Chrome");
        configuration.addClassifications("Branch", "release/1.0");

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
 
    }


}

