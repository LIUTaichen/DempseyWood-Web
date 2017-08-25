package com.dempseywood.webservice.geofence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoadCount {
    private String vehicle;
    private GeofencesTO loadingZone;
    private GeofencesTO dumpingZone;
    private Date time;
    public LoadCount() {

    }
    public LoadCount(GeofencesTO loadingZone, GeofencesTO dumpingZone, Date time, String vehicle) {
        this.loadingZone = loadingZone;
        this.dumpingZone = dumpingZone;
        this.time = time;
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public GeofencesTO getLoadingZone() {
        return loadingZone;
    }

    public void setLoadingZone(GeofencesTO loadingZone) {
        this.loadingZone = loadingZone;
    }

    public GeofencesTO getDumpingZone() {
        return dumpingZone;
    }

    public void setDumpingZone(GeofencesTO dumpingZone) {
        this.dumpingZone = dumpingZone;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
