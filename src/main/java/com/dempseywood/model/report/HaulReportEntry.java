package com.dempseywood.model.report;

import java.time.LocalDateTime;
import java.util.Date;

public class HaulReportEntry {
    private Date loadTime;
    private Date unloadTime;
    private String equipment;
    private long duration;
    private String loadType;
    private double volume;
    private double cost;
    private double revenue;
    private double profit;



    public HaulReportEntry(){

    }


    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public Date getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
