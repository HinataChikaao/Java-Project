package lethbridge.core;


import lethbridge.model.entities.UserEntity;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Named
@RequestScoped
public class LethUnReport implements Serializable {

    public static final int MODE_2003 = 2003;
    public static final int MODE_2007 = 2007;

    public byte[] generateExcelFile2003(Map<String, Object[]> data, UserEntity userEntity,
                                        String date, String reportName) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Student Account Info");
        sheet.setRightToLeft(false);
        sheet.setFitToPage(true);

        /* ------------------------------------------------------------------------- */
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/logo/uni2.png");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        int pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
        inputStream.close();
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(3);
        anchor.setRow1(0);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize(1, 4.3);
        /* ------------------------------------------------------------------------- */

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();


        font.setFontHeight((short) 300);
        font.setFontName("B Zar");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);

        //cellStyle.setDataFormat((short) 3);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        cellStyle.setShrinkToFit(true);
        cellStyle.setFillBackgroundColor((short) 10);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        /* --------------------------------- */
        HSSFCellStyle cellStyleTitr = workbook.createCellStyle();
        HSSFFont fontTitr = workbook.createFont();


        fontTitr.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());

        //fontTitr.setCharSet(FontCharset.ARABIC.ordinal());
        fontTitr.setBold(true);
        fontTitr.setFontHeight((short) 300);
        fontTitr.setFontName("Arial");

        cellStyleTitr.setFont(fontTitr);
        cellStyleTitr.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitr.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());

        HSSFCellStyle cellStyleInfo = workbook.createCellStyle();
        HSSFFont fontInfo = workbook.createFont();

        fontInfo.setBold(true);
        fontInfo.setFontHeight((short) 300);
        //fontInfo.setCharSet(FontCharset.ARABIC.getValue());
        fontInfo.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());

        cellStyleInfo.setFont(fontInfo);

        cellStyleInfo.setAlignment(HorizontalAlignment.RIGHT);

        HSSFRow row0 = sheet.createRow(0);
        HSSFCell usr = row0.createCell(1);
        HSSFCell txtUsr = row0.createCell(0);
        usr.setCellValue(userEntity.getUserID());
        txtUsr.setCellValue("Admin:");
        usr.setCellStyle(cellStyleInfo);
        txtUsr.setCellStyle(cellStyleInfo);

        HSSFRow row1 = sheet.createRow(1);
        HSSFCell dt = row1.createCell(1);
        HSSFCell txtDt = row1.createCell(0);
        dt.setCellValue(date);
        txtDt.setCellValue("Date:");
        dt.setCellStyle(cellStyleInfo);
        txtDt.setCellStyle(cellStyleInfo);

        HSSFRow row5 = sheet.createRow(5);
        HSSFCell title = row5.createCell(3);
        title.setCellValue(reportName);
        title.setCellStyle(cellStyleTitr);
        /* --------------------------------- */

        Set<String> keySet = data.keySet();
        int rowIndex = 7;
        for (String key : keySet) {
            HSSFRow row = sheet.createRow(rowIndex++);
            Object[] objects = data.get(key);
            int cellIndex = 0;
            for (Object obj : objects) {
                HSSFCell cell = row.createCell(cellIndex++);
                cell.setCellStyle(cellStyle);
                if (obj instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) obj).doubleValue());
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof String) {
                    cell.setCellValue(String.valueOf(obj).trim());
                }
            }
        }

        for (int i = 0; i < data.get("1").length; i++) {
            sheet.autoSizeColumn(i, true);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        baos.close();
        return baos.toByteArray();
    }


}




