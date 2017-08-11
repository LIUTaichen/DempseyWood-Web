package com.dempseywood.webservice.equipmentstatus;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DailyExcelReportView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<EquipmentStatus> statusList = getData();

        Sheet sheet = workbook.createSheet("Java Books");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Book Title");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Author");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("ISBN");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Published Date");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Price");
        header.getCell(4).setCellStyle(style);
    }

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;


    public Workbook writeReport(){
        Workbook workbook =  new SXSSFWorkbook();
        List<EquipmentStatus> statusList = getData();

        Sheet sheet = workbook.createSheet("Java Books");
        sheet.setDefaultColumnWidth(30);

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
            timeCell.setCellValue(status.getTimestamp());

            timeCell.setCellStyle(dateCellStyle);
            currentColumn =0;
        }
        return workbook;



    }

    private   List<EquipmentStatus> getData() {
        Date time = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        List<EquipmentStatus> statusList = equipmentStatusRepository.findByTimestampBetweenOrderByOperatorDescTimestamp(startOfDay, time);
        System.out.println(statusList.size());
        return statusList;
    }
}
