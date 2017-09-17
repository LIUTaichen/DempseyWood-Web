package com.dempseywood.model;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.List;

public class LoadCountSummaryByMachine extends BaseLoadCountSummary{

    public LoadCountSummaryByMachine(List<HaulSummary> entries){
        super(entries);
    }
    @Override
    public void compute() {

    }
}
