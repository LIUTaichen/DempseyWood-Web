package com.dempseywood.service;

import com.dempseywood.model.HaulSummary;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SummaryByMachineSheetWriter extends SheetWriter {

    public SummaryByMachineSheetWriter(String sheetName){
        this.sheetName = sheetName;
    }

    public void writeSheet(Workbook workbook, List<HaulSummary> summaryList){
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(20);

        List<HaulSummary> sortedList = getSortedHaulSummaries(summaryList);
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
        CellStyle doubleCellStyle = workbook.createCellStyle();
        doubleCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));;

        // create header row
        int currentRow = 0;
        int currentColumn = 0;
        Row header = sheet.createRow(currentRow++);

        String[] columnNames = {"Machine", "Load Type","Load Count","Volume", "Time" ,"Cost", "Revenue" ,"Profit" };
        for(int i = 0; i<columnNames.length; i ++){
            header.createCell(currentColumn).setCellValue(columnNames[i]);
            header.getCell(currentColumn).setCellStyle(headerStyle);
            currentColumn++;
        }

       
        currentColumn = 0;
        
        for(HaulSummary entry: sortedList){
            currentColumn =0;
            Row row = sheet.createRow(currentRow++);
            
            row.createCell(currentColumn++).setCellValue(entry.getEquipment());

            row.createCell(currentColumn++).setCellValue(entry.getLoadType());
            row.createCell(currentColumn++).setCellValue(entry.getLoadCount());
            row.createCell(currentColumn++).setCellValue(entry.getVolume());
            row.createCell(currentColumn++).setCellValue(entry.getDuration());

            Cell costCell = row.createCell(currentColumn++);
            costCell.setCellValue(entry.getCost());
            costCell.setCellStyle(doubleCellStyle);
            Cell revenueCell = row.createCell(currentColumn++);
            revenueCell.setCellValue(entry.getRevenue());
            revenueCell.setCellStyle(doubleCellStyle);
            Cell profitCell = row.createCell(currentColumn++);
            profitCell.setCellValue(entry.getProfit());
            profitCell.setCellStyle(doubleCellStyle);

        }
    }

    private List<HaulSummary> getSortedHaulSummaries(List<HaulSummary> summaryList) {
        return summaryList.stream().sorted(Comparator.comparing(HaulSummary::getEquipment).thenComparing(HaulSummary::getLoadType)).collect(Collectors.toList());
    }
}
