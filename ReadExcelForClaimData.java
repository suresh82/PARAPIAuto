package com.cigna.parsanpapiautomation.utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cigna.parsanpapiautomation.reports.ExtentReportListner;

/**
 * @author M20589
 * ReadExcelForClaimData class contains method for reading input claims data for SANP XMLs
 */
public class ReadExcelForClaimData extends ExtentReportListner{

	XSSFSheet sheet;
	int noOfColumns;
	int rows;

	@SuppressWarnings({ "unlikely-arg-type", "resource" })
	public ReadExcelForClaimData() {
		try {
			String claimDataPath = properties.getProperty("Claim_Data_File_PATH");
			FileInputStream file = new FileInputStream(claimDataPath); 
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			noOfColumns = sheet.getRow(0).getLastCellNum();
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	ArrayList<String> claimExptectedDataList;


	/**
	 * @author M20589
	 * claimExpResultData() reads the claimData excel and returns the data as an Array
	 * @param rowCnt
	 * @return ArrayList<String>
	 */
	public ArrayList<String> claimExpResultData(int rowCnt){

		claimExptectedDataList = new ArrayList<String>();
		String temp=null;
		rows=rowCnt;

		for (int col=0;col<noOfColumns;col++) {
			temp=sheet.getRow(rows).getCell(col).getStringCellValue();
			claimExptectedDataList.add(temp);
		}

		return claimExptectedDataList;

	}

}
