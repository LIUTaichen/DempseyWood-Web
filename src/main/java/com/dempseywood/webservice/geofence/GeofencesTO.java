package com.dempseywood.webservice.geofence;

import com.dempseywood.navixy.tracker.Geofence;
import com.dempseywood.navixy.tracker.LatLng;

import java.util.List;

public class GeofencesTO {
    private Integer size;
    private List<LatLng> latlngs;
    private String material;
    private String zoneTye;
    private String zoneName;



    private Geofence load;
    private Geofence dump;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Geofence getLoad() {
        return load;
    }

    public void setLoad(Geofence load) {
        this.load = load;
    }

    public Geofence getDump() {
        return dump;
    }

    public void setDump(Geofence dump) {
        this.dump = dump;
    }


    public List<LatLng> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<LatLng> latlngs) {
        this.latlngs = latlngs;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getZoneTye() {
        return zoneTye;
    }

    public void setZoneTye(String zoneTye) {
        this.zoneTye = zoneTye;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
