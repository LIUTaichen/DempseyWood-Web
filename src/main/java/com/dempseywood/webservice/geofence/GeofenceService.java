package com.dempseywood.webservice.geofence;


import com.dempseywood.entity.Geofence;
import com.dempseywood.entity.LatLng;
import com.dempseywood.entity.Reading;
import org.springframework.stereotype.Service;

import static  com.dempseywood.util.PolyUtil.*;


import java.util.ArrayList;
import java.util.List;

@Service
public class GeofenceService {

    public static final String LOADING_ZONE = "Loading zone";
    private final Integer UNLOADED = 0;
    private final Integer LOADING = 1;
    private final Integer LOADED = 2;
    private final Integer UNLOADING = 3;


    public Integer getNumberOfTrips(Geofence loadingArea, Geofence dumpingArea, List<Reading> track) {
        Integer tripCount = 0;
        Integer status = UNLOADED;

        for (Reading reading : track) {
            if (status == UNLOADED) {
                if (isIn(reading, loadingArea)) {
                    status = LOADING;
                }
            } else if (status == LOADING) {
                if (!isIn(reading, loadingArea)) {
                    status = LOADED;
                }
            } else if (status == LOADED) {
                if (isIn(reading, dumpingArea)) {
                    status = UNLOADING;
                    tripCount++;
                }
            } else if (status == UNLOADING) {
                if (!isIn(reading, dumpingArea)) {
                    status = UNLOADED;
                }
            }


        }

        return tripCount;

    }

    public List<LoadCount> getLoadCounts(List<GeofencesTO> geofences, List<Reading> track) {
        List<LoadCount> loadCounts = new ArrayList<LoadCount>();
        TruckStatus status = TruckStatus.UNLOADED;
        GeofencesTO currentLoadingZone = new GeofencesTO();
        GeofencesTO currentDumpingZone = new GeofencesTO();

        List<GeofencesTO> loadingZones = new ArrayList<GeofencesTO>();
        List<GeofencesTO> dumpingZones = new ArrayList<GeofencesTO>();
        geofences.forEach(geofence -> {
            if(geofence.getZoneType().equals(LOADING_ZONE)){
                loadingZones.add(geofence);
            }else{
                dumpingZones.add(geofence);
            }
        });
        List<GeofencesTO> activeDumpingZones = new ArrayList<GeofencesTO>();
        /*
        * assumptions : truck is unloaded at the first reading
        * truck is loaded when entering the first loading zone
        * when truck is loaded, entering other loading zones are ignored
        * zones do not overlap each other
        *
        *
        *
        * */
        for (Reading reading : track) {

            switch(status){
                case UNLOADED:
                    for(GeofencesTO zone : loadingZones){
                        if(isIn(reading, zone)) {
                            status = TruckStatus.LOADING;
                            currentLoadingZone = zone;
                            activeDumpingZones.clear();
                            for(GeofencesTO dumpingZone : dumpingZones){
                                if(dumpingZone.getMaterial().equals(currentLoadingZone.getMaterial())){
                                    activeDumpingZones.add(dumpingZone);
                                }
                            }
                            break;
                        }
                    }
                    break;
                case LOADING:
                    if (!isIn(reading, currentLoadingZone)) {
                        status = TruckStatus.LOADED;
                    }
                    break;
                case LOADED:
                    for(GeofencesTO zone : activeDumpingZones){
                        if (isIn(reading, zone)) {
                            status = TruckStatus.UNLOADING;
                            currentDumpingZone = zone;
                            loadCounts.add(new LoadCount(currentLoadingZone, currentDumpingZone, reading.getTime(), reading.getTrackerId().toString() ));
                            break;
                        }
                    }
                    break;
                case UNLOADING:
                    if (!isIn(reading, currentDumpingZone)) {
                        status = TruckStatus.UNLOADED;
                    }
                    break;
            }

        }
        return loadCounts;
    }


    public boolean isIn(Reading reading, Geofence fence){

        return containsLocation(reading.getLat(), reading.getLng(), fence.getVertices(), false );
    }

    public boolean isIn(LatLng point, Geofence fence){

        return containsLocation(point.getLatitude(), point.getLongitude(), fence.getVertices(), false );
    }

    public boolean isIn(Reading reading, GeofencesTO fence){
        return containsLocation(reading.getLat(), reading.getLng(), fence.getLatlngs(), false );
    }
}


