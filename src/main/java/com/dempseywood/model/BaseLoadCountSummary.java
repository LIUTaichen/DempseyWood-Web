package com.dempseywood.model;

import java.util.ArrayList;
import java.util.Comparator;
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
            this.setEquipment(entry.getEquipment());
            this.setLoadType(entry.getLoadType());
            this.setLoadCount(this.getLoadCount() + entry.getLoadCount());
            this.setVolume(this.getVolume() + entry.getVolume());
            this.setDuration(this.getDuration() + entry.getDuration());
            this.setCost(this.getCost() + entry.getCost());
            this.setRevenue(this.getRevenue() + entry.getRevenue());
            this.setProfit(this.getProfit() + entry.getProfit());
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
