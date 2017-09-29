package com.dempseywood.service;

import com.dempseywood.model.CostSchedule;
import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.model.Haul;
import com.dempseywood.util.DateTimeUtil;
import com.dempseywood.webservice.geofence.TruckStatus;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.dempseywood.webservice.geofence.TruckStatus.UNLOADED;

public class HaulsSheetWriter extends SheetWriter{

    public HaulsSheetWriter(String sheetName){
        this.sheetName = sheetName;
    }


    private Logger log = LoggerFactory.getLogger("HaulsSheetWriter");

    public void writeHauls(Workbook workbook, List<Haul> haulList){


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
        String[] columnNames = {"Load Time", "Unload Time", "Duration (minutes)", "Machine","Load Type", "Volume","Cost", "Revenue" ,"Profit" };
        for(int i = 0; i<columnNames.length; i ++){
            header.createCell(currentColumn).setCellValue(columnNames[i]);
            header.getCell(currentColumn).setCellStyle(headerStyle);
            currentColumn++;
        }

        currentColumn = 0;
        for(Haul haul: haulList){
            try {

                Row row = sheet.createRow(currentRow++);
                currentColumn = 0;

                Cell timeCell = row.createCell(currentColumn++);
                try {
                    Date convertedDate = DateTimeUtil.getInstance().convertToClientLocalDate(haul.getLoadTime());
                    timeCell.setCellValue(DateUtil.getExcelDate(convertedDate));
                }catch(Exception e){
                    e.printStackTrace();
                }

                timeCell.setCellStyle(dateCellStyle);
                timeCell = row.createCell(currentColumn++);
                try {
                    Date convertedDate = DateTimeUtil.getInstance().convertToClientLocalDate(haul.getUnloadTime());
                    timeCell.setCellValue(DateUtil.getExcelDate(convertedDate));
                }catch(Exception e){
                    e.printStackTrace();
                }
                timeCell.setCellStyle(dateCellStyle);
                row.createCell(currentColumn++).setCellValue(haul.getDuration());
                row.createCell(currentColumn++).setCellValue(haul.getEquipment());
                row.createCell(currentColumn++).setCellValue(haul.getLoadType());
                row.createCell(currentColumn++).setCellValue(haul.getVolume());
                Cell costCell = row.createCell(currentColumn++);
                costCell.setCellValue(haul.getCost());
                costCell.setCellStyle(doubleCellStyle);
                Cell revenueCell = row.createCell(currentColumn++);
                revenueCell.setCellValue(haul.getRevenue());
                revenueCell.setCellStyle(doubleCellStyle);
                Cell profitCell = row.createCell(currentColumn++);
                profitCell.setCellValue(haul.getProfit());
                profitCell.setCellStyle(doubleCellStyle);
            }
            catch (Exception e){
                log.error("error writing haul list", e);
            }
        }


    }




}
