package com.dempseywood.webservice.geofence;


import com.dempseywood.model.*;
import com.dempseywood.model.locationbased.*;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private TrackerRepository trackerRepository;


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

    public List<Trip> getLoadCounts(List<Geofence> geofences, List<Reading> track) {
        List<Trip> trips = new ArrayList<Trip>();
        TruckStatus status = TruckStatus.UNLOADED;
        Geofence currentLoadingZone = new Geofence();
        Geofence currentDumpingZone = new Geofence();

        List<Geofence> loadingZones = new ArrayList<Geofence>();
        List<Geofence> dumpingZones = new ArrayList<Geofence>();
        geofences.forEach(geofence -> {
            if(geofence.getZoneType().equals(LOADING_ZONE)){
                loadingZones.add(geofence);
            }else{
                dumpingZones.add(geofence);
            }
        });
        List<Geofence> activeDumpingZones = new ArrayList<Geofence>();
        Equipment vehicle = null;
        if(!track.isEmpty()){
            Integer trackerId = track.get(0).getTrackerId();
            Tracker tracker = trackerRepository.findOne(trackerId);
             vehicle = equipmentRepository.findOne(tracker.getEquipmentId());
        }
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
                    for(Geofence zone : loadingZones){
                        if(isIn(reading, zone)) {
                            status = TruckStatus.LOADING;
                            currentLoadingZone = zone;
                            activeDumpingZones.clear();
                            for(Geofence dumpingZone : dumpingZones){
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
                    for(Geofence zone : activeDumpingZones){
                        if (isIn(reading, zone)) {
                            status = TruckStatus.UNLOADING;
                            currentDumpingZone = zone;
                            trips.add(new Trip(currentLoadingZone, currentDumpingZone, reading.getTime(), vehicle));
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
        return trips;
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


