package com.dempseywood.model;

import java.util.List;

public class LoadCountTable  extends HaulSummary{
    private List<HaulSummary> summaryList;

    public LoadCountTable(List<HaulSummary> summaryList) {
        this.summaryList = summaryList;
        this.compute();
    }

    private void compute(){
        for(HaulSummary entry : summaryList){
            this.setLoadCount(this.getLoadCount() + entry.getLoadCount());
            this.setVolume(entry.getVolume() + entry.getVolume());
            this.setDuration(entry.getDuration() + entry.getDuration());
            this.setCost(entry.getCost() + entry.getCost());
            this.setRevenue(entry.getRevenue() + entry.getRevenue());
            this.setProfit(entry.getProfit() + entry.getProfit());
        }
    }
}
