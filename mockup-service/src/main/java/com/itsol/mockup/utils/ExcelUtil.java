package com.itsol.mockup.utils;

import com.itsol.mockup.entity.SubTaskEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.web.dto.project.ProjectDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import com.itsol.mockup.web.rest.file.FileController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    protected Workbook workbook;

    public ExcelUtil(){
        this.workbook = new XSSFWorkbook();
    }

    public Sheet sheetCreate(String name, Object o) {
        Sheet sheet;
        try{
            sheet = workbook.createSheet(name);
        }catch (Exception e){
            sheet = workbook.getSheet(name);
        }
        int columnWidth = 6500;
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle style = createStyleHeader(workbook);

        Field[] fields = o.getClass().getDeclaredFields();
        int i = 1;
        createHeader(header,style,0,"Index");
        for(Field f : fields){
//            logger.info("a\t"+f.getName());
            sheet.setColumnWidth(i, columnWidth);
//            createHeader(header,headerStyle,i,f.getName());
            createHeader(header,style,i,f.getName());
            i++;
        }

        return sheet;
    }

    public void sheetWriteListData(Sheet sheet, List<?> list){
        List<Object> values;
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        int k = 1;
        CellStyle style;
        for(Object dto : list){
            style = getCellStyle(workbook, k);
            values = new ArrayList<>();
            for (Field field : fields) {
                String attribute = field.getName();
                String methodName = "get" + capitalize(attribute);
                try {
                    Method method = list.get(0).getClass().getMethod(methodName);
                    values.add(method.invoke(dto));
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
            Row row = sheet.createRow(k);
            createCell(row, style, 0, k);
            int i;
            for(i = 1; i<=values.size(); i++){
                Object value = values.get(i-1);
                if(value!=null){
                    createCell(row, style, i, value);
                }else{
                    createCell(row, style, i, "none");
                }
            }
            k++;
        }
    }

    public void sheetWriteSingleData(Sheet sheet, Object obj){
        List<Object> values;
        Field[] fields = obj.getClass().getDeclaredFields();
        logger.info("class {}", obj.getClass());
        values = new ArrayList<>();
        for (Field field : fields) {
            String attribute = field.getName();
            String methodName = "get" + capitalize(attribute);
//            logger.info("method\t"+methodName);
            try {
                Method method = obj.getClass().getMethod(methodName);
                values.add(method.invoke(obj));
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }

        CellStyle style = workbook.createCellStyle();
        Row row = sheet.createRow(1);
        createCell(row, style, 0, 1);
        int i;
        for(i = 1; i<=values.size(); i++){
            Object value = values.get(i-1);
            if(value!=null){
                createCell(row, style, i, value);
            }else{
                createCell(row, style, i, "none");
            }
        }

    }

    public void out(String fileOutputName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fileOutputName+".xlsx");
        workbook.write(outputStream);
//        workbook.close();
    }

    private void createCell(Row row, CellStyle style, int i, Object value){
        switch (value.getClass().getSimpleName()) {
            case "Double":
                createCell(row, style, i, (double) value);
                break;
            case "Long":
                createCell(row, style, i, (long) value);
                break;
            case "Integer":
                createCell(row, style, i, (int) value);
                break;
            case "Timestamp":
            case "String":
                createCell(row, style, i, value.toString());
                break;
            case "UsersEntity":
                createCell(row, style, i, ((UsersEntity) value).getUserName());
                break;
            case "UsersDTO":
                createCell(row, style, i, ((UsersDTO) value).getUserName());
                break;
            case "ProjectDTO":
                createCell(row, style, i, ((ProjectDTO) value).getProjectName());
                break;
            case "ArrayList":
                try {
                    createCell(row, style, i, getSubTask((List<SubTaskEntity>) value));
                }catch (Exception e){
                    createCell(row, style, i, "**");
                }
                break;
            case "String[]":
                String v = String.join(" ", (String[]) value);
                v = v.replace("null","").replace("  "," ");
                createCell(row, style, i, v);
                break;
            default:
                createCell(row, style, i, "*");
                break;
        }

    }

    private String getSubTask(List<SubTaskEntity> subTask){
        StringBuilder res = new StringBuilder();

        for(SubTaskEntity s : subTask){
            res.append(s.getTaskName()).append(", ");
        }

        return res.toString().trim().substring(0, res.length() - 2);
    }

    private void createCell(Row row, CellStyle style, int r, String value){
        Cell cell = row.createCell(r);
        try{
            cell.setCellValue(value);
        }catch (Exception e) {
            logger.info(e.getMessage());
            cell.setCellValue(" ");
        }
        cell.setCellStyle(style);
    }

    private void createCell(Row row, CellStyle style, int r, double value){
        Cell cell = row.createCell(r);
        try{
            cell.setCellValue(value);
        }catch (Exception e) {
            logger.info(e.getMessage());
            cell.setCellValue(0);
        }
        cell.setCellStyle(style);
    }

    private void createHeader(Row header, CellStyle headerStyle, int i, String colName){
        Cell headerCell = header.createCell(i);
        headerCell.setCellValue(colName);
        headerCell.setCellStyle(headerStyle);
    }

    private Font createFontHeader(Workbook workbook) {
        Font hSSFFontHeader = workbook.createFont();
        hSSFFontHeader.setFontName("Arial");
        hSSFFontHeader.setFontHeightInPoints((short) 10);
        hSSFFontHeader.setBold(true);
        return hSSFFontHeader;
    }

    private CellStyle createStyleHeader(Workbook workbook) {
        CellStyle cellStyleHeader = createCellStyleHeader(workbook);
        Font hSSFFontHeader = createFontHeader(workbook);
        hSSFFontHeader.setColor(IndexedColors.BLACK1.index);
        cellStyleHeader.setFont(hSSFFontHeader);
        return cellStyleHeader;
    }

    private CellStyle createCellStyleHeader(Workbook workbook) {
        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleHeader.setBorderRight(BorderStyle.THIN);
        cellStyleHeader.setBorderTop(BorderStyle.THIN);
        cellStyleHeader.setFillForegroundColor(IndexedColors.AQUA.index);
        cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleHeader.setWrapText(true);
        return cellStyleHeader;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private CellStyle getCellStyle(Workbook workbook, int k) {
        CellStyle cellStyleCenter = workbook.createCellStyle();
        cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
        cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleCenter.setBorderLeft(BorderStyle.THIN);
        cellStyleCenter.setBorderBottom(BorderStyle.THIN);
        cellStyleCenter.setBorderRight(BorderStyle.THIN);
        cellStyleCenter.setBorderTop(BorderStyle.THIN);
        cellStyleCenter.setWrapText(false);
        cellStyleCenter.setDataFormat((short) BuiltinFormats.getBuiltinFormat("@"));
        cellStyleCenter.setWrapText(true);
        cellStyleCenter.setFillForegroundColor(IndexedColors.WHITE.index);
        if(k%2==0){
            cellStyleCenter.setFillForegroundColor(IndexedColors.AQUA.index);
        }
        cellStyleCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyleCenter;
    }

}
