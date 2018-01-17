package com.dempseywood.service;

import com.dempseywood.model.report.HaulReportEntry;
import com.dempseywood.util.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HaulsSheetWriter extends SheetWriter{

    public HaulsSheetWriter(String sheetName){
        this.sheetName = sheetName;
    }


    private Logger log = LoggerFactory.getLogger("HaulsSheetWriter");

    public void writeHauls(Workbook workbook, List<HaulReportEntry> haulReportEntryList){


        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(20);

        // create style for header cells
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        headerStyle.setFont(font);

        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("hh:mm:ss "));
        CellStyle doubleCellStyle = workbook.createCellStyle();
        doubleCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));


        // create header row
        int currentRow = 0;
        int currentColumn = 0;
        Row header = sheet.createRow(currentRow++);
        String[] columnNames = {"Haul Time", "Unload Time", "Duration (minutes)", "Machine","Haul Type", "Volume","Cost", "Revenue" ,"Profit" };
        for(int i = 0; i<columnNames.length; i ++){
            header.createCell(currentColumn).setCellValue(columnNames[i]);
            header.getCell(currentColumn).setCellStyle(headerStyle);
            currentColumn++;
        }

        currentColumn = 0;
        for(HaulReportEntry haulReportEntry : haulReportEntryList){
            try {

                Row row = sheet.createRow(currentRow++);
                currentColumn = 0;

                Cell timeCell = row.createCell(currentColumn++);
                try {
                    Date convertedDate = DateTimeUtil.getInstance().convertToClientLocalDate(haulReportEntry.getLoadTime());
                    timeCell.setCellValue(DateUtil.getExcelDate(convertedDate));
                }catch(Exception e){
                    e.printStackTrace();
                }

                timeCell.setCellStyle(dateCellStyle);
                timeCell = row.createCell(currentColumn++);
                try {
                    Date convertedDate = DateTimeUtil.getInstance().convertToClientLocalDate(haulReportEntry.getUnloadTime());
                    timeCell.setCellValue(DateUtil.getExcelDate(convertedDate));
                }catch(Exception e){
                    e.printStackTrace();
                }
                timeCell.setCellStyle(dateCellStyle);
                row.createCell(currentColumn++).setCellValue(haulReportEntry.getDuration());
                row.createCell(currentColumn++).setCellValue(haulReportEntry.getEquipment());
                row.createCell(currentColumn++).setCellValue(haulReportEntry.getLoadType());
                row.createCell(currentColumn++).setCellValue(haulReportEntry.getVolume());
                Cell costCell = row.createCell(currentColumn++);
                costCell.setCellValue(haulReportEntry.getCost());
                costCell.setCellStyle(doubleCellStyle);
                Cell revenueCell = row.createCell(currentColumn++);
                revenueCell.setCellValue(haulReportEntry.getRevenue());
                revenueCell.setCellStyle(doubleCellStyle);
                Cell profitCell = row.createCell(currentColumn++);
                profitCell.setCellValue(haulReportEntry.getProfit());
                profitCell.setCellStyle(doubleCellStyle);
            }
            catch (Exception e){
                log.error("error writing haulReportEntry list", e);
            }
        }


    }




}
