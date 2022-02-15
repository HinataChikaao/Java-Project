package lethbridge.core;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.ConstantText;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@RequestScoped
public class ExcelParsing implements Serializable {

    private LinkedHashMap<String, TreeMap<String, Object>> dataGraphs;
    //private Date date;

    public ExcelParsing() {
        dataGraphs = new LinkedHashMap<>();
    }

    public void readFromExcel(InputStream excelFileInputStream) throws Exception {

        String title = ConstantText.EMPTY;
        int index = 0;
        long xIndex = 0;
        long yIndex = 0;
        boolean x = false;
        boolean y = false;

        TreeMap<String, Object> dataGraph;
        LinkedHashMap<String, XSSFSheet> sheets = new LinkedHashMap<>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);

        int numberOfSheets = workbook.getNumberOfSheets();

        while (index < numberOfSheets) {
            sheets.put(workbook.getSheetName(index),
                    workbook.getSheetAt(index));
            index++;
        }

        for (Map.Entry<String, XSSFSheet> sheet : sheets.entrySet()) {
            dataGraph = new TreeMap<>();
            for (Row curRow : sheet.getValue()) {
                Iterator<Cell> cellIterator = curRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellTypeEnum()) {

                        case STRING: {
                            title = cell.getStringCellValue();

                            if (title.equalsIgnoreCase(ConstantText.X)) x = true;
                            if (title.equalsIgnoreCase(ConstantText.Y)) y = true;
                            break;
                        }

                        case NUMERIC: {

                            if (!x && !y) {
                                if (title.trim().length() == 0) throw new Exception("Invalid data...");
                                if (title.trim().equalsIgnoreCase(ConstantText.DATE)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.CANADA);
                                    dataGraph.put(title.trim().toLowerCase(), sdf.format(cell.getDateCellValue()));
                                } else {
                                    dataGraph.put(title.trim().toLowerCase(), cell.getNumericCellValue());
                                }
                            } else if (x && y) {
                                if (cell.getColumnIndex() == 0) {
                                    dataGraph.put(ConstantText.X + xIndex++, cell.getNumericCellValue());
                                } else if (cell.getColumnIndex() == 1) {
                                    dataGraph.put(ConstantText.Y + yIndex++, cell.getNumericCellValue());
                                }
                            }
                            title = ConstantText.EMPTY;
                            break;
                        }
                    }
                }
            }
            dataGraphs.put(sheet.getKey(), dataGraph);
            xIndex = 0;
            yIndex = 0;
            x = false;
            y = false;
        }
        workbook.close();
        excelFileInputStream.close();
    }

    public byte[] writeToExcel(Map<String, Object[]> graphData, String sheetName) throws Exception {
        int rowIndex = 0;
        Set<String> keySet = graphData.keySet();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(ConstantText.DATE_FORMAT));

        for (String key : keySet) {
            Row row = sheet.createRow(rowIndex++);
            Object[] dataRow = graphData.get(key);
            int cellIndex = 0;
            for (Object dataCell : dataRow) {
                Cell cell = row.createCell(cellIndex++);
                if (dataCell instanceof Date) {
                    cell.setCellValue((Date) dataCell);
                    cell.setCellStyle(cellStyle);
                } else if (dataCell instanceof String) {
                    cell.setCellValue((String) dataCell);
                } else if (dataCell instanceof Number) {
                    cell.setCellValue(((Number) dataCell).doubleValue());
                }
            }
        }

        File excelFile = new File(ConstantText.EXCEL_FILE_NAME);
        if (excelFile.exists()) {
            excelFile.delete();
        }

        FileOutputStream out = new FileOutputStream(excelFile);
        workbook.write(out);
        workbook.close();
        out.close();
        return Files.readAllBytes(excelFile.toPath());

    }

    public LinkedHashMap<String, TreeMap<String, Object>> getDataGraphs() {
        return dataGraphs;
    }

    /*public String getDate() {
        DateFormat df = new SimpleDateFormat(ConstantText.DATE_FORMAT);
        return df.format(date);
    }*/

}
