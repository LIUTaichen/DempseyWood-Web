package com.dempseywood.service;

import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.util.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class EventsSheetWriter extends SheetWriter{


   private String clientTimeZone = "NZ";

    public EventsSheetWriter(String sheetName){
        this.sheetName = sheetName;
    }

    public void writeSheet(Workbook workbook, List<EquipmentStatus> statusList){
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(25);

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
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("hh:mm:ss dddd dd/mm/yyyy"));

        // create header row
        int currentRow = 0;
        int currentColumn = 0;
        Row header = sheet.createRow(currentRow++);

        header.createCell(currentColumn).setCellValue("Operator Name");
        header.getCell(currentColumn).setCellStyle(headerStyle);
        currentColumn++;
        header.createCell(currentColumn).setCellValue("Machine");
        header.getCell(currentColumn).setCellStyle(headerStyle);
        currentColumn++;
        header.createCell(currentColumn).setCellValue("Task");
        header.getCell(currentColumn).setCellStyle(headerStyle);
        currentColumn++;
        header.createCell(currentColumn).setCellValue("Status");
        header.getCell(currentColumn).setCellStyle(headerStyle);
        currentColumn++;
        header.createCell(currentColumn).setCellValue("Time");
        header.getCell(currentColumn).setCellStyle(headerStyle);
        currentColumn = 0;
        for(EquipmentStatus status: statusList){
            Row row = sheet.createRow(currentRow++);
            row.createCell(currentColumn++).setCellValue(status.getOperator());
            row.createCell(currentColumn++).setCellValue(status.getEquipment());
            row.createCell(currentColumn++).setCellValue(status.getTask());
            row.createCell(currentColumn++).setCellValue(status.getStatus());
            Cell timeCell = row.createCell(currentColumn++);
            Date timestamp = status.getTimestamp();
            try {
                Date convertedDate = DateTimeUtil.getInstance().convertToClientLocalDate(timestamp);
                timeCell.setCellValue(DateUtil.getExcelDate(convertedDate));
            }catch(Exception e){
                e.printStackTrace();
            }

            timeCell.setCellStyle(dateCellStyle);
            currentColumn =0;
        }
    }
}
