package com.dempseywood.model.locationbased;

import com.dempseywood.model.locationbased.Geofence;
import com.dempseywood.model.locationbased.LatLng;

import java.util.List;

public class GeofencesTO {
    private Integer size;
    private List<LatLng> latlngs;




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


}
