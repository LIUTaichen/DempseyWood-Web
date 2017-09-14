package com.dempseywood.service;

import org.apache.poi.ss.usermodel.Workbook;

public abstract class SheetWriter {
    protected String sheetName;
    protected Workbook workbook;

    //abstract public void  writeSheet();

    public SheetWriter(){

    }

    protected SheetWriter(String sheetName){
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
}
