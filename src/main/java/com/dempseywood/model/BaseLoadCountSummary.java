package com.dempseywood.model;

import java.util.ArrayList;
import java.util.List;

public class BaseLoadCountSummary extends HaulSummary{

    private List<HaulSummary> entries;

    public BaseLoadCountSummary(){
        this.entries = new ArrayList<HaulSummary>();
    }

    public BaseLoadCountSummary(List<HaulSummary> entries) {
        this.entries = entries;
    }

    public void compute(){
        for(HaulSummary entry : entries){
            this.setLoadCount(this.getLoadCount() + entry.getLoadCount());
            this.setVolume(entry.getVolume() + entry.getVolume());
            this.setDuration(entry.getDuration() + entry.getDuration());
            this.setCost(entry.getCost() + entry.getCost());
            this.setRevenue(entry.getRevenue() + entry.getRevenue());
            this.setProfit(entry.getProfit() + entry.getProfit());
        }
    }

    public void add(HaulSummary entry){
        entries.add(entry);
    }

    public List<HaulSummary> getEntries() {
        return entries;
    }

    public void setEntries(List<HaulSummary> entries) {
        this.entries = entries;
    }
}
