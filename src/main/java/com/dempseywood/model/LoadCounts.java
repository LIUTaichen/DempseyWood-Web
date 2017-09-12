package com.dempseywood.model;

import java.util.ArrayList;
import java.util.List;

public class LoadCounts {

    private Equipment vehicle;
    private List<Count> counts = new ArrayList<Count>();

    public Equipment getVehicle() {
        return vehicle;
    }

    public void setVehicle(Equipment vehicle) {
        this.vehicle = vehicle;
    }

    public List<Count> getCounts() {
        return counts;
    }

    public void setCounts(List<Count> counts) {
        this.counts = counts;
    }

    public void increment(String material){
        Count countEntry = null;
        for(Count count : counts){
            if(count.getMaterial().equals(material)){
                countEntry = count;
            }
        }
        if(countEntry == null) {
            countEntry = new Count(material, 1);
            counts.add(countEntry);
        }else{
            countEntry.setCount(countEntry.getCount() + 1);
        }
    }
}
