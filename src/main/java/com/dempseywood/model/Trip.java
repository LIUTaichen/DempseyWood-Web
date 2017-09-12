package com.dempseywood.model;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.Geofence;

import java.util.Date;

public class Trip {
    private Equipment vehicle;
    private Geofence loadingZone;
    private Geofence dumpingZone;
    private Date time;
    public Trip() {

    }
    public Trip(Geofence loadingZone, Geofence dumpingZone, Date time, Equipment vehicle) {
        this.loadingZone = loadingZone;
        this.dumpingZone = dumpingZone;
        this.time = time;
        this.vehicle = vehicle;
    }

    public Equipment getVehicle() {
        return vehicle;
    }

    public void setVehicle(Equipment vehicle) {
        this.vehicle = vehicle;
    }

    public Geofence getLoadingZone() {
        return loadingZone;
    }

    public void setLoadingZone(Geofence loadingZone) {
        this.loadingZone = loadingZone;
    }

    public Geofence getDumpingZone() {
        return dumpingZone;
    }

    public void setDumpingZone(Geofence dumpingZone) {
        this.dumpingZone = dumpingZone;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
