package lethbridge.core;

import lethbridge.model.entities.CurNews;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/*
 * Type of the POI Class file to support different excel file format:
 * XSSFWorkbook xls
 * HSSFWorkbook xlsx
 * SXSSFWorkbook Streaming
 */

public class TradExcelParsing implements Serializable {

    private Logger univLogger;

    /* Point only to the first sheet of the excel file */
    private final XSSFSheet sheet;

    /* Number of companies */
    private int companyCount;

    /* Number of stories */
    private int storyCount;

    /* Number of rows in the excel file. */
    private int rowCount;

    /* Validation excel file fields */
    private int timeColNum;
    private int storyColNum;

    /* Point to the company name in the excel file by corresponding index column name. */
    private Map<String, Integer> companies;

    /* Point to the company restriction number to buy stocks. */
    private Map<String, Integer> restrictionNumber;

    /* Point to the story name in the excel file by corresponding index column name. */
    private Map<String, Integer> newses;

    /* Validation Message */
    private String message;

    /* X Axis of the graph */
    private double[] timeValues;

    /* Companies data (Y Axis) */
    private Map<String, double[]> companiesValues;

    /* Stories data (Y Axis) */
    private Map<String, List<String>> storiesValues;

    //private List<CurNews> curNews;

    public TradExcelParsing(InputStream excelFile) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        sheet = workbook.getSheetAt(0);

        timeColNum = 0;
        storyColNum = 0;
        companies = new LinkedHashMap<>();
        restrictionNumber = new LinkedHashMap<>();
        newses = new LinkedHashMap<>();
        univLogger = LoggerFactory.getLogger(TradExcelParsing.class.getName());
        //curNews = new ArrayList<>();
    }


    public boolean fieldValidate() {

        final String TIME = "TIME";
        final String COMPANY = "COMPANY_";
        final String STORY = "STORY_";

        String valid = "Valid excel file.";
        String invalid = "Invalid Field for excel file";
        String invalidType = "Invalid type of value for";
        String format = " Row Number: %d, Column Number %d, Cell Column Name: %s";

        int time = 0;
        int miscellaneous = 0;

        try {
            rowCount = sheet.getLastRowNum();
            XSSFRow row = sheet.getRow(0);

            /* Reading Title and validate the excel file. */
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                univLogger.info(String.format("Company: %s", cell.getStringCellValue()));
                switch (cell.getCellTypeEnum()) {
                    case STRING: {

                        String value = cell.getStringCellValue();

                        if (value.trim().toUpperCase().equals(TIME)) {
                            time++;
                            timeColNum = cell.getColumnIndex();
                        } else if (value.trim().toUpperCase().startsWith(STORY)) {
                            storyCount++;
                            newses.put(value.trim().replace(STORY, ""), cell.getColumnIndex());
                        } else if (value.trim().toUpperCase().startsWith(COMPANY)) {
                            companyCount++;
                            companies.put(value.trim().replace(COMPANY, "").split("_")[0],
                                    cell.getColumnIndex());
                            restrictionNumber.put(value.trim().replace(COMPANY, "").split("_")[0],
                                    Integer.parseInt(Optional.ofNullable(value.trim().replace(COMPANY, "")
                                            .split("_")[1]).map(String::trim).orElse("0")));
                        } else {
                            miscellaneous++;
                        }
                        break;
                    }
                    default: {
                        miscellaneous++;
                    }
                }
            }

            if ((time == 1) &&
                    (storyCount >= 1) &&
                    (companyCount >= 1) &&
                    (companies.size() == companyCount) &&
                    (storyCount == companyCount) &&
                    (miscellaneous == 0) &&
                    comparisonCompanyNamesAndStoryNames()) {

                Cell cell;
                for (Row r : sheet) {
                    if (r.getRowNum() != 0) {
                        /* -------------------------- */
                        cell = r.getCell(timeColNum);
                        if (cell != null) {
                            if (cell.getCellTypeEnum() != CellType.NUMERIC) {
                                message = String.format(invalidType + format,
                                        r.getRowNum() + 1, cell.getColumnIndex() + 1, TIME);
                                univLogger.info(String.format(invalidType + format,
                                        r.getRowNum() + 1, cell.getColumnIndex() + 1, TIME));
                                return false;
                            }
                        }
                        /* --------------------------*/
                        /*cell = r.getCell(storyColNum);
                        if (cell != null) {
                            if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                                message = String.format(invalidType + format,
                                        r.getRowNum() + 1, cell.getColumnIndex() + 1, STORY);
                                return false;
                            }
                        }*/
                        /* -------------------------- */
                        for (Map.Entry<String, Integer> map : companies.entrySet()) {
                            cell = r.getCell(map.getValue());
                            if (cell == null) {
                                message = String.format(invalidType + format,
                                        r.getRowNum() + 1, null, COMPANY + map.getKey());
                                univLogger.info(String.format(invalidType + format, r.getRowNum() + 1, null, COMPANY + map.getKey()));
                                return false;
                            }
                            if (cell.getCellTypeEnum() != CellType.NUMERIC) {
                                message = String.format(invalidType + format,
                                        r.getRowNum() + 1, cell.getColumnIndex() + 1, COMPANY + map.getKey());
                                univLogger.info(String.format(invalidType + format,
                                        r.getRowNum() + 1, cell.getColumnIndex() + 1, COMPANY + map.getKey()));
                                return false;
                            }
                        }
                    }
                }

            } else {
                message = invalid;
                univLogger.info("Excel file is invalid.(A)");
                return false;
            }
            message = valid;
            univLogger.info("Excel file is passed validation.");
            return true;
        } catch (Exception ex) {
            message = invalid;
            univLogger.info("Excel file is invalid.(B)");
            return false;
        }
    }

    public void initialize() {

        int index = 0;
        timeValues = new double[rowCount];

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            timeValues[index] = row.getCell(timeColNum).getNumericCellValue();
            index++;
        }

        univLogger.info(String.format("Number of times is %d", index));

        /* ---------------------------- */

        companiesValues = new LinkedHashMap<>(companyCount);
        double[] companyPoints;

        for (Map.Entry<String, Integer> map : companies.entrySet()) {
            index = 0;
            companyPoints = new double[rowCount];
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                companyPoints[index] = row.getCell(map.getValue()).getNumericCellValue();
                index++;
            }
            companiesValues.put(map.getKey(), companyPoints);
        }

        /* ---------------------------- */

        storiesValues = new LinkedHashMap<>(storyCount);
        List<String> storyPoints = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();

        for (Map.Entry<String, Integer> map : newses.entrySet()) {
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                storyPoints.add(row.getCell(map.getValue()).getStringCellValue());
                if (row.getRowNum() == lastRowNum) {
                    storiesValues.put(map.getKey(), storyPoints);
                    storyPoints = new ArrayList<>();
                }
            }
        }
    }

    private boolean comparisonCompanyNamesAndStoryNames() {
        for (Map.Entry<String, Integer> cmp : companies.entrySet()) {
            if (!newses.containsKey(cmp.getKey())) {
                return false;
            }
        }
        return true;
    }

    private String getHeadLine(String companyName, int index) {
        if (index >= storiesValues.get(companyName).size()) {
            index = storiesValues.get(companyName).size() - 1;
        }
        String result = storiesValues.get(companyName).get(index);
        int i = result.indexOf(":");
        if (i != -1) {
            return result.substring(0, i);
        } else {
            return "No Head Line";
        }
    }

    private String getStory(String companyName, int index) {
        if (index >= storiesValues.get(companyName).size()) {
            index = storiesValues.get(companyName).size() - 1;
        }
        String result = storiesValues.get(companyName).get(index);
        int i = result.indexOf(":");
        if (i != -1) {
            return result.substring(result.indexOf(":") + 1);
        } else {
            return result;
        }
    }

    public List<CurNews> getCurrentNews(String companyName, int tickIndex) {
        synchronized (this) {
            List<CurNews> curNews = new ArrayList<>(timeValues.length);
            if (tickIndex >= timeValues.length) {
                tickIndex = timeValues.length - 1;
            }
            for (int i = 0; i < tickIndex + 1; i++) {
                String story = getStory(companyName, i);
                String headLine = getHeadLine(companyName, i);
                if (!story.trim().toUpperCase().equals("NA") &&
                        !headLine.trim().toUpperCase().equals("NA")) {
                    curNews.add(new CurNews(i + 1, companyName, headLine, story));
                }
            }
            return curNews;
        }

    }

    public String getMessage() {
        return message;
    }

    public double[] getTimeValues() {
        return timeValues;
    }

    public Map<String, double[]> getCompaniesValues() {
        return companiesValues;
    }

    public Map<String, List<String>> getStoriesValues() {
        return storiesValues;
    }

    public double getMinValueTime() {
        double[] clone = getTimeValues().clone();
        Arrays.sort(clone);
        return clone[0];
    }

    public double getMaxValueTime() {
        double[] clone = getTimeValues().clone();
        Arrays.sort(clone);
        return clone[clone.length - 1];
    }

    public double getMinValueCompanyFor(String key) {
        double[] clone = getCompaniesValues().get(key).clone();
        Arrays.sort(clone);
        return clone[0];
    }

    public double getMaxValueCompanyFor(String key) {
        double[] clone = getCompaniesValues().get(key).clone();
        Arrays.sort(clone);
        return clone[clone.length - 1];
    }

    public String[] getCompaniesNames() {
        String[] companyNames = new String[companyCount];
        return getCompaniesValues().keySet().toArray(companyNames);
    }

    public Map<String, Integer> getRestrictionNumber() {
        return restrictionNumber;
    }

}
