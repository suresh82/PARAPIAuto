package com.cigna.parsanpapiautomation.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*Created by: Rajalakshmi Ramesh 
Created date: November 2019
Class Name: PropertiesFileReader
Class Description: This class includes methods to perform settings for the properties file to define input values
*/ 

public class PropertiesFileReader{

	
	/*Created by: Rajalakshmi Ramesh 
	Created date: 
	Method Name: getProperty
	Method Description: Method to define properties of input stream Return: Properties
	Input Parameters: 
	Return: Method returns the corresponding properties from the file*/
	@SuppressWarnings("unused")
	public Properties getProperty() throws IOException
	{
		FileInputStream inputStream=null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("resources/PropertyFiles/Prerequisites.properties"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	
	/*Created by: Rajalakshmi Ramesh 
	Created date: 
	Method Name: getProperty
	Method Description: Method to define details of input stream using input attribute(File Type)
	Input Parameters: Filetype
	Return: Method returns the corresponding properties from the file based on input filetype*/
	
	@SuppressWarnings("unused")
	public Properties getProperty(String strFileType) throws IOException
	{
		FileInputStream inputStream=null;
		Properties properties = new Properties();
		try {
			if(strFileType=="PREREQ")
			{
				properties.load(new FileInputStream("resources/PropertyFiles/Prerequisites.properties"));
			}
			else if(strFileType=="CONFIG")
			{
				properties.load(new FileInputStream("resources/PropertyFiles/config.properties"));
			}
			else if(strFileType=="LOG")
			{
				properties.load(new FileInputStream("resources/PropertyFiles/log4j.properties"));
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
