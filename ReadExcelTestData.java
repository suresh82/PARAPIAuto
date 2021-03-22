package com.cigna.parsanpapiautomation.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.cigna.parsanpapiautomation.reports.ExtentReportListner;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

/**
 * @author M20589
 * ReadExcelTestData class contains methods for reading test data count from excel
 */
public class ReadExcelTestData extends ExtentReportListner {

	String sheetName;
	String dataToBePopulatedToFeatureFile;
	String placeHolder = "#@DataFeedPlaceHolder";
	String exampleContent = "";

	/**
	 * @author M20589
	 * readExcel() Reads the TestData Excel from resources folder
	 * @return void
	 * @throws FilloException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public void readExcel() throws FilloException, IOException {

		Fillo fillo = new Fillo();
		Recordset recordset = null;
		String rowContent, value;
		String testDataFilePath = properties.getProperty("Test_Data_File_PATH");
		sheetName = "PARSANPAPI";
		String query = String.format("SELECT request FROM %s", sheetName);
		Connection connection = fillo.getConnection(testDataFilePath);
		recordset = connection.executeQuery(query);
		rowContent = "";

		while (recordset.next()) {
			rowContent = "";

			ArrayList<String> f = new ArrayList<String>(recordset.getFieldNames());

			value = null;
			for (int cellNumber = 0; cellNumber < f.size(); cellNumber++) {
				rowContent += recordset.getField(f.get(cellNumber))+ "|";

			}
			rowContent = "|" + rowContent;
			exampleContent +=rowContent + "\n" ;
		}
		connection.close();

		dataToBePopulatedToFeatureFile = FileUtils.readFileToString(new File("src/test/resources/Feature/" + sheetName + ".feature") );
		dataToBePopulatedToFeatureFile = dataToBePopulatedToFeatureFile.replace(placeHolder, exampleContent);
		FileUtils.writeStringToFile(new File("src/test/resources/Feature/" + sheetName + ".feature"), dataToBePopulatedToFeatureFile, false);
	}

	/**
	 * @author M20589
	 * deleteExamples() method deletes the rows/elements from the feature file
	 * @return void
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public void deleteExamples() throws IOException {
		@SuppressWarnings("deprecation")
		String dataPresentInFeatureFile = FileUtils.readFileToString(new File("src/test/resources/Feature/" + sheetName + ".feature") );
		dataToBePopulatedToFeatureFile = dataPresentInFeatureFile.replace(exampleContent,placeHolder);
		FileUtils.writeStringToFile(new File("src/test/resources/Feature/" + sheetName + ".feature"), dataToBePopulatedToFeatureFile, false);

	}

}


