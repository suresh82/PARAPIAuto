package com.cigna.parsanpapiautomation.reports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;

/*Created by: Rakhi Bisht 
Created date: November 2019
Class Name: TestListenerImpl
Class Description: This class includes methods to initiate execution and display the execution result on console
*/
public class TestListenerImpl extends ExtentReportListner implements ITestListener {
	public static ExtentReports extentReports;
	
	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestStart
	Method Description: Method to initiate execution and extent report
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onTestStart(ITestResult result) {
		System.out.println("On Test Start");
			
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestSuccess
	Method Description: Method for test cases which are passed
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onTestSuccess(ITestResult result) {
		System.out.println("PASS");
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestFailure
	Method Description: Method for test cases which are failed
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onTestFailure(ITestResult result) {

		System.out.println("FAIL");
	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestSkipped
	Method Description: Method for test cases which are skipped
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onTestSkipped(ITestResult result) {

		System.out.println("SKIP");

	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestFailure
	Method Description: Method for test cases which are failed
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	
	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onStart
	Method Description: Method to initialize the extent report
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onStart(ITestContext context) {

		System.out.println("On start...");
		extentReports = setup();

	}

	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onFinish
	Method Description: Method to erase any previous data on the report and create a new report
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		System.out.println("Execution completed on SYS env...");
		extentReports.flush();

	}
	
	/*Created by: Rakhi Bisht 
	Created date: 
	Method Name: onTestFailedButWithinSuccessPercentage
	Method Description: Method for test cases which are failed but are within the pass percentage
	Input Parameters: TestResult(to print the corresponding value on console)
	Return: Method does not return anything*/
	private void Finalcall()
	   {
		   String content = "";
		    try {
		        BufferedReader in = new BufferedReader(new FileReader("./Reports/ExtentReport.html"));
		        String str;
		        while ((str = in.readLine()) != null) {
		            content +=str;
		        }
		        in.close();
		        String content1 = content;
		        content1 = content1.replace("<span class='duration right label'>0h 0m 0s+0ms</span>", " ");
		        File file = new File("./Reports/ExtentReport.html");
	           FileWriter fileWriter = new FileWriter(file);
	           fileWriter.write(content1);
	          
	           fileWriter.flush();
	           fileWriter.close();
		    } catch (IOException e) {
		    }
	   }

}
