package com.hils.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ExcelHandle {


    private ExcelHandle() {
    }




    public static XSSFWorkbook getWorkBook(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return new XSSFWorkbook(fis);
    }

    public static List<String> getListOldTcs(String path, String sheetName, int column) throws IOException {
        XSSFWorkbook workbook = getWorkBook(path);
        Sheet sheet = workbook.getSheet(sheetName);

        int lastRow = sheet.getLastRowNum();

        List<String> listOldTCS = new ArrayList<>();

        for (int i = 1; i <= lastRow; i++) {
            String cellValue = sheet.getRow(i).getCell(column).getStringCellValue();
            listOldTCS.add(cellValue);
        }

        return listOldTCS;
    }


    public static void writeToExcelDetectDeletedCase(List<String> data, String path, String sheetName, int column) throws IOException {
        XSSFWorkbook workbook = getWorkBook(path);
        XSSFSheet sheet = workbook.getSheet(sheetName);

        XSSFCellStyle cellStyleDeletedCase = sheet.getRow(5).getCell(8).getCellStyle();

        int lastRow = sheet.getLastRowNum() + 1;
        Cell cell;

        for (int i = 1; i < lastRow; i++) {

            cell = sheet.getRow(i).getCell(column);
            String value = cell.getStringCellValue();

            for (String item : data) {

                if (value.equals(item)) {
                    cell.setCellStyle(cellStyleDeletedCase);
                }
            }
        }
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();

//        openFile(path);
    }


    public static void writeToExcelAddNewCase(List<String> data, String path, String sheetName, int column) throws IOException {

        if(data.size() != 0) {

            XSSFWorkbook workbook = getWorkBook(path);;
            XSSFSheet sheet = workbook.getSheet(sheetName);

            XSSFCellStyle cellStyleInsert = sheet.getRow(2).getCell(8).getCellStyle();
//        XSSFFont fontInsert = cellStyleInsert.getFont();
//        cellStyleInsert.setFont(fontInsert);

            XSSFCellStyle cellStyleBlank = sheet.getRow(3).getCell(8).getCellStyle();
//        XSSFFont fontBlank = cellStyleBlank.getFont();
//        cellStyleInsert.setFont(fontBlank);

            XSSFCellStyle cellStyleNewCaseAdd = sheet.getRow(4).getCell(8).getCellStyle();



            int lastRow = sheet.getLastRowNum() + 1;

            Row row;
            Cell cell;

            for (int i = 1; i < lastRow; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(cellStyleBlank);
            }

            for (String item : data) {
                row = sheet.createRow(lastRow++);

                for (int i = 0; i < 8; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyleBlank);

                    if (i == 0) {
                        cell.setCellValue(lastRow-1);
                        cell.setCellStyle(cellStyleNewCaseAdd);
                    } else if (i == column) {
                        cell.setCellValue(item);
                        cell.setCellStyle(cellStyleInsert);
                    } else if (i == 2) {
                        cell.setCellValue(item.substring(item.lastIndexOf("\\") + 1));
                    }
                }
            }

            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);
            out.close();

        }

//        openFile(path);
    }


    public static void writeToExcel(Map<String, List<String>> data, String path) throws IOException {

        XSSFWorkbook workbook = getWorkBook(path);
        XSSFSheet sheet = workbook.createSheet("3.Scenario List");
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setColor(IndexedColors.WHITE.index);
        cellStyle.setFont(font);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Scenario Name");
        row.createCell(1).setCellValue("Step Number");
        row.createCell(2).setCellValue("TestCase Name");

        int rowNum = 1;

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {

            String key = entry.getKey();
            List<String> lists = entry.getValue();
            int listSize = lists.size();

            if (listSize != 0) {
                if (listSize > 1) {
                    for (int i = 1; i < listSize; i++) {
                        if (i > 1) {
                            row = sheet.createRow(rowNum++);
                            cell = row.createCell(0);
                            cell.setCellValue(key);
                            cell.setCellStyle(cellStyle);
                            row.createCell(1).setCellValue(i);
                            row.createCell(2).setCellValue(lists.get(i));
                        } else {
                            row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(key);
                            row.createCell(1).setCellValue(i);
                            row.createCell(2).setCellValue(lists.get(i));
                        }
                    }
                } else {
                    row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(key);
                    row.createCell(1).setCellValue(0);
                    row.createCell(2).setCellValue("");
                }
            }
        }



        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();

        openFile(path);

    }

    public static void openFile(String path) {

        try {
            //constructor of file class having file as argument
            File file = new File(path);
            if (!Desktop.isDesktopSupported())
            //check if Desktop is supported by Platform or not
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) //checks file exists or not
                desktop.open(file); //opens the specified file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

