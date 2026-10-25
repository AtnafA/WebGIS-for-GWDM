package net.AAWSAgDB.fileupload.model;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CoordinateUpdate {
	private double latitude;
	private double longitude;
	private double easting;
	private double northing;

	    // Constructor, getters, and setters
	    public CoordinateUpdate(double latitude, double longitude, double easting,double northing){
	        this.latitude = latitude;
	        this.longitude = longitude;
	        this.easting = easting;
	        this.northing=northing;
	    }

	    public double getLatitude() { return latitude; }
	    public double getLongitude() { return longitude; }
	    public double getUpdateEastingValue() { return easting; }
	    public double getUpdateNorthingValue() { return northing; }
	 
	    public static void setCellValueByNamedRange(Workbook workbook, String namedRangeName, String valueToSet) {
	        // 1. Check if the named range exists in the workbook
	        Name namedRange = workbook.getName(namedRangeName);

	        if (namedRange != null) {
	            // 2a. If the named range exists, get the cell reference and set the value
	            String refersToFormula = namedRange.getRefersToFormula();
	            if (refersToFormula != null && !refersToFormula.isEmpty()) {
	                // The refersToFormula is usually in the format 'SheetName!$A$1'
	                CellReference cellRef = new CellReference(refersToFormula);
	                Sheet sheet = workbook.getSheet(cellRef.getSheetName());
	                if (sheet != null) {
	                    Row row = sheet.getRow(cellRef.getRow());
	                    if (row == null) {
	                        row = sheet.createRow(cellRef.getRow());
	                    }
	                    Cell cell = row.getCell(cellRef.getCol(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	                    cell.setCellValue(valueToSet);
	                    System.out.println("Value set for existing named range: " + namedRangeName);
	                }
	            }
	        } else {
	            // 2b. If the named range does not exist, log a message and assign a custom/default value elsewhere or skip
	            System.out.println("Named range '" + namedRangeName + "' does not exist. Assigning a default value to a specific cell instead.");
	            
	            // Example: Assign a default value to cell B2 on the first sheet
	            Sheet sheet = workbook.getSheetAt(0);
	            if (sheet == null) {
	                sheet = workbook.createSheet("Sheet1");
	            }
	            Row row = sheet.getRow(1); // 0-based index, row 2
	            if (row == null) {
	                row = sheet.createRow(1);
	            }
	            Cell cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // 0-based index, column B
	            cell.setCellValue("DEFAULT_VALUE"); // Set your custom value here
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        // Example usage with a sample workbook
	        // Make sure you have a test.xlsx file
	        try (InputStream is = new FileInputStream("test.xlsx");
	             Workbook workbook = new XSSFWorkbook(is)) {

	            setCellValueByNamedRange(workbook, "xx", "Some custom data"); // Tries to use the non-existent named range 'xx'
	            
	            // Save the changes
	            try (OutputStream fileOut = new FileOutputStream("test_output.xlsx")) {
	                workbook.write(fileOut);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    

}
