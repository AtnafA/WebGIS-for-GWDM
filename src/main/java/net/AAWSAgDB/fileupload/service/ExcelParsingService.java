package net.AAWSAgDB.fileupload.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelParsingService {

    @Autowired
    private TextSimilarityService similarityService;

    private static final String TARGET_SHEET = "2.DrillingPumpTestHistoryData";
    private static final String TARGET_OPERATIONS_SHEET = "3-DetaiOperations Well Inventor";
    
    private static final String[] SPECIAL_CASES = {
        "Trace", "Trac", "ND", "nd", "NULL", "nill", "nul", "nu", "NaN", "nan", "null", 
        "TR", "tr", "trace", "Nill", "Null", "NALL", "Nall", "nall", "Nil", "in", "IN", 
        "iN", "In", "-", "--", "_", "__", "Decimal", "Decimal ", " Decimal"
    };

    /**
     * Parses an uploaded multipart file stream safely.
     * Extracts row array structures and applies string sanitization rules.
     * 
     * @return List of arrays containing exactly 87 cleaned string elements per row
     */
    public List<String[]> parseDrillingLogsExcel(MultipartFile abanfile) throws Exception {
        List<String[]> parsedDataList = new ArrayList<>();
        
        if (abanfile == null || abanfile.isEmpty()) {
            return parsedDataList;
        }

        // 1. Leverage Try-with-resources to ensure memory streams release instantly upon execution exit
        try (InputStream fileStream = abanfile.getInputStream();
             Workbook workbook = new XSSFWorkbook(fileStream)) {

            Sheet matchingSheet = null;
            double highestSimilarity = 0.0;

            // Step A: Find the closest matching spreadsheet tab dynamically using the Text Similarity Service
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String currentSheetName = workbook.getSheetName(i);
                double similarity = similarityService.compute(TARGET_SHEET, currentSheetName);
                if (similarity > highestSimilarity) {
                    highestSimilarity = similarity;
                    matchingSheet = workbook.getSheetAt(i);
                }
            }

            // Step B: If matching sheet surfaces above the 80% threshold metric parameter boundary
            if (matchingSheet != null && highestSimilarity > 0.85) {
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Pattern decimalPattern = Pattern.compile("(\\.)");

                // Start loop at Row index 5 according to database template rules
                for (int rowIndex = 5; rowIndex <= matchingSheet.getLastRowNum(); rowIndex++) {
                    Row row = matchingSheet.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }

                    String[] excelRowData = new String[87];
                    int dataColumnIndex = 0;
                    boolean hasNonNullCell = false;

                    // Step C: Process cell data elements reliably across 87 fixed target columns
                    for (int colIndex = 0; colIndex < 87; colIndex++) {
                        Cell cell = row.getCell(colIndex);
                        String cleanValue = "0";

                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            hasNonNullCell = true;
                            cleanValue = extractAndSanitizeCellValue(cell, evaluator, decimalPattern);
                        }

                        excelRowData[dataColumnIndex++] = cleanValue;
                    }

                    // Only save row datasets if they contain actual data
                    if (hasNonNullCell) {
                        parsedDataList.add(excelRowData);
                    }
                }
            }
        }
        return parsedDataList;
    }
    
    /**
     * Parses an uploaded operational data file stream safely.
     * Reuses the centralized cell sanitizers to extract operational rows.
     * 
     * @return List of arrays containing exactly 113 cleaned string elements per row
     */
    public List<String[]> parseDetailedOperationsExcel(MultipartFile file) throws Exception {
        List<String[]> parsedDataList = new ArrayList<>();
        
        if (file == null || file.isEmpty()) {
            return parsedDataList;
        }

        try (InputStream fileStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(fileStream)) {

            Sheet matchingSheet = null;
            double highestSimilarity = 0.0;

            // Find the closest matching operational worksheet tab dynamically
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String currentSheetName = workbook.getSheetName(i);
                double similarity = similarityService.compute(TARGET_OPERATIONS_SHEET, currentSheetName);
                if (similarity > highestSimilarity) {
                    highestSimilarity = similarity;
                    matchingSheet = workbook.getSheetAt(i);
                }
            }

            // Execute parsing if the sheet matches above the 80% threshold contract
            if (matchingSheet != null && highestSimilarity > 0.85) {
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Pattern decimalPattern = Pattern.compile("(\\.)");

                // Start loop at Row index 5 matching your template rules
                for (int rowIndex = 5; rowIndex <= matchingSheet.getLastRowNum(); rowIndex++) {
                    Row row = matchingSheet.getRow(rowIndex);
                    if (row == null) continue;

                    String[] excelRowData = new String[113]; // Expanded to 113 columns for operations
                    int dataColumnIndex = 0;
                    boolean hasNonNullCell = false;

                    // Dynamically map and sanitize all 113 target fields per row
                    for (int colIndex = 0; colIndex < 113; colIndex++) {
                        Cell cell = row.getCell(colIndex);
                        String cleanValue = "0";

                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            hasNonNullCell = true;
                            // REUSING YOUR EXISTING SUB-METHOD CLEANING ENGINE PERFECTLY!
                            cleanValue = extractAndSanitizeCellValue(cell, evaluator, decimalPattern);
                        }
                        excelRowData[dataColumnIndex++] = cleanValue;
                    }

                    if (hasNonNullCell) {
                        parsedDataList.add(excelRowData);
                    }
                }
            }
        }
        return parsedDataList;
    }
    /**
     * Inner data sanitizer core. Formats cell values and corrects decimal anomalies.
     */
    private String extractAndSanitizeCellValue(Cell cell, FormulaEvaluator evaluator, Pattern decimalPattern) {
        CellType type = cell.getCellType();
        
        // Handle formula values dynamically
        if (type == CellType.FORMULA) {
            try {
                CellValue cellValue = evaluator.evaluate(cell);
                type = cellValue.getCellType();
                return getRawValueAsString(type, cell, cellValue,decimalPattern);
            } catch (Exception e) {
                return "0";
            }
        }
        
        return getRawValueAsString(type, cell, null,decimalPattern);
    }

    private String getRawValueAsString(CellType type, Cell cell, CellValue cellValue,Pattern DECIMAL_PATTERN) {
        String raw = "0";
        java.text.SimpleDateFormat ISO_DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd");
        
        switch (type) {
            case STRING:
                raw = (cellValue != null) ? cellValue.getStringValue() : cell.getStringCellValue();
                raw = raw.trim();
                
                for (String specCase : SPECIAL_CASES) {
                    if (specCase.equals(raw)) return "0";
                }
                
                Matcher matcher = DECIMAL_PATTERN.matcher(raw);
                if (matcher.find()) {
                    String beforeFirstDecimal = raw.substring(0, matcher.end());
                    String afterFirstDecimal = raw.substring(matcher.end());
                    raw = beforeFirstDecimal + afterFirstDecimal.replaceAll("\\.", "");
                }
                return raw;

            case NUMERIC:
                // FIXED: If it's a native Excel date, format it instantly as yyyy-MM-dd
                if (cellValue == null && DateUtil.isCellDateFormatted(cell)) {
                    try {
                        return ISO_DATE_FORMAT.format(cell.getDateCellValue());
                    } catch (Exception e) {
                        return "1971-01-01";
                    }
                }
                
                // If it's an evaluated date formula cell, handle its conversion safely
                if (cellValue != null && DateUtil.isCellDateFormatted(cell)) {
                    try {
                        return ISO_DATE_FORMAT.format(DateUtil.getJavaDate(cellValue.getNumberValue()));
                    } catch (Exception e) {
                        return "1971-01-01";
                    }
                }
                
                double num = (cellValue != null) ? cellValue.getNumberValue() : cell.getNumericCellValue();
                return String.valueOf(num).trim();

            case BOOLEAN:
                boolean bool = (cellValue != null) ? cellValue.getBooleanValue() : cell.getBooleanCellValue();
                return String.valueOf(bool).trim();

            default:
                return "0";
        }
    }
    
    public double[] extractStepDrawdownYields(String rawCell47Value) {
        if (rawCell47Value == null || rawCell47Value.trim().isEmpty() || "0".equals(rawCell47Value)) {
            return new double[] { 0.0 };
        }
        try {
            String[] tokens = rawCell47Value.split(",");
            double[] yields = new double[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                yields[i] = Double.parseDouble(tokens[i].trim());
            }
            return yields;
        } catch (NumberFormatException e) {
            return new double[] { 0.0 };
        }
    }

    /**
     * Helper to extract comma-separated string tags safely (e.g., Drilling Methods).
     */
    public String[] extractDrillingMethods(String rawCell36Value) {
        if (rawCell36Value == null || rawCell36Value.trim().isEmpty() || "0".equals(rawCell36Value)) {
            return new String[] { "" };
        }
        return rawCell36Value.split(",");
    }
}