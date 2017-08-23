package com.dempseywood.webservice.geofence;


import com.dempseywood.navixy.tracker.Geofence;
import com.dempseywood.navixy.tracker.LatLng;
import com.dempseywood.navixy.tracker.Reading;
import org.springframework.stereotype.Service;

import static  com.dempseywood.util.PolyUtil.*;


import java.util.List;

@Service
public class GeofenceService {

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

    public boolean isIn(Reading reading, Geofence fence){

        return containsLocation(reading.getLat(), reading.getLng(), fence.getVertices(), false );
    }

    public boolean isIn(LatLng point, Geofence fence){

        return containsLocation(point.getLatitude(), point.getLongitude(), fence.getVertices(), false );
    }
}


