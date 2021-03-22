package com.cigna.parsanpapiautomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cigna.parsanpapiautomation.reports.ExtentReportListner;


/**
 * @author M20589
 * ReadFilesFromFolder class contains methods for reading the xml file count from the input folder
 */
public class ReadFilesFromFolder extends ExtentReportListner {


	int fileCount;
	PropertiesFileReader propertiesFileReader = new PropertiesFileReader();
	Properties propertiesObject;


	/**
	 * @author M20589
	 * readAllFiles() reads the all files that resides in location "./resources/DataFiles/SANPXmls/"
	 * @return void
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readAllFiles() throws FileNotFoundException, IOException {

		String filePath = properties.getProperty("XML_File_Path");
		String testDataPath = properties.getProperty("Test_Data_File_PATH");
		File folder = new File(filePath);
		String[] files = folder.list();

		for (int count=1;count<=files.length;count++) {
			
			File file = new File(testDataPath);
			FileInputStream inputStream = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int  x = sheet.getLastRowNum();
			int y = ++x;

			Row newRow = sheet.createRow(y);
			int col = 0;

			Cell cell = newRow.createCell(col);
			cell.setCellValue(count);

			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.close();

		}
		fileCount = files.length;
	}


	/**
	 * @author M20589
	 * deleteDataFromExcel() method deletes the data from TestData excel
	 * @return void
	 * @throws IOException
	 */
	public void deleteDataFromExcel() throws IOException {
		String testDataPath = properties.getProperty("Test_Data_File_PATH");
		File file = new File(testDataPath);
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int lastRow = sheet.getLastRowNum();
		for(int row = 1; row <= lastRow; row++) {
			sheet.removeRow(sheet.getRow(row));
		}
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);
	}

}
