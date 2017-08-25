package com.dempseywood.webservice.geofence;

import com.dempseywood.entity.Geofence;
import com.dempseywood.entity.LatLng;
import com.dempseywood.entity.Reading;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GeofenceServiceTest {


    private GeofenceService service = new GeofenceService();
    private Geofence loadingArea;
    private Geofence dumpingArea;


    private List<LatLng> loadingPoints = new ArrayList<LatLng>();
    private List<LatLng> dumpingPoints = new ArrayList<LatLng>();
    private List<LatLng> pointsInsideLoading = new ArrayList<LatLng>();
    private List<LatLng> pointsInsideDumping  = new ArrayList<LatLng>();
    private List<LatLng> pointsOutsideBoth = new ArrayList<LatLng>();

    private List<Reading> readings = new ArrayList<Reading>();
    private List<Reading> readings5Trips = new ArrayList<Reading>();
    private List<Reading> readings10Trips = new ArrayList<Reading>();



    @Before
    public void setUp() throws Exception {
        Reading reading = new Reading();
        reading.setLat(-37.18493748465265);
        reading.setLng(174.86560821533206);


        LatLng point = new LatLng(-37.18493748465265, 174.86560821533206);
        LatLng point2 = new LatLng(-37.22868822817218, 174.93083953857422);
        LatLng point3 = new LatLng(-37.235795328042364, 174.845695495605472);
        loadingPoints.add(point);
        loadingPoints.add(point2);
        loadingPoints.add(point3);
        loadingArea = new Geofence();
        loadingArea.setVertices(loadingPoints);
        LatLng point4 = new LatLng(-37.115978644999785, 174.87453460693362);
        LatLng point5 = new LatLng(-37.13295054092013, 174.9789047241211);
        LatLng point6 = new LatLng(-37.09078752906291, 174.98027801513672);
        dumpingPoints.add(point4);
        dumpingPoints.add(point5);
        dumpingPoints.add(point6);
        dumpingArea = new Geofence();
        dumpingArea.setVertices(dumpingPoints);


        pointsInsideLoading.add(new LatLng(-37.21119097450984, 174.87882614135745 ));
        pointsInsideLoading.add(new LatLng(-37.20462845803211, 174.88191604614258 ));
        pointsInsideLoading.add(new LatLng(-37.213378353177895, 174.89118576049808 ));

        pointsInsideDumping.add(new LatLng(-37.104753204243295, 174.94062423706055));
        pointsInsideDumping.add(new LatLng(-37.10119357072204, 174.94749069213867));
        pointsInsideDumping.add(new LatLng(-37.1074912700486, 174.94955062866214));

        pointsOutsideBoth.add(new LatLng(-37.16688291460904			, 	175.02010345458984        ));
        pointsOutsideBoth.add(new LatLng( -37.19970619616173			,	 175.03108978271487       ));
        pointsOutsideBoth.add(new LatLng(-37.191501713067			, 		175.0613021850586         ));
        pointsOutsideBoth.add(new LatLng(-37.173448712009574		, 	175.08464813232425            ));
        pointsOutsideBoth.add(new LatLng(-37.140614020656514				,	 175.08327484130862   ));
        pointsOutsideBoth.add(new LatLng(-37.11816878088743			, 	175.04207611083987        ));

        pointsOutsideBoth.add(new LatLng(-37.250554087013754				,	 175.06679534912112   ));
        pointsOutsideBoth.add(new LatLng(-37.24946094472433				, 	175.08464813232425    ));

        pointsInsideLoading.forEach(p -> readings.add(new Reading(p)) );
        pointsOutsideBoth.forEach(p -> readings.add(new Reading(p)) );
        pointsInsideDumping.forEach(p -> readings.add(new Reading(p)) );

        for(int i = 0; i < 5; i++){
            readings5Trips.addAll(readings);
        }

        for(int i = 0; i < 10; i++){
            readings10Trips.addAll(readings);
        }
    }

    @Test
    public void getNumberOfTrips() throws Exception {

        assertEquals((Integer)1 , service.getNumberOfTrips(loadingArea, dumpingArea, readings ));
        assertEquals((Integer)5 , service.getNumberOfTrips(loadingArea, dumpingArea, readings5Trips ));
        assertEquals((Integer)10 , service.getNumberOfTrips(loadingArea, dumpingArea, readings10Trips ));

    }

    @Test
    public void isIn() throws Exception {
        loadingPoints.forEach(point -> assertTrue(service.isIn(point, loadingArea)));
        pointsInsideLoading.forEach(point -> assertTrue(service.isIn(point, loadingArea)));

        dumpingPoints.forEach(point -> assertTrue(service.isIn(point, dumpingArea)));
        pointsInsideDumping.forEach(point -> assertTrue(service.isIn(point, dumpingArea)));


        pointsOutsideBoth.forEach(point -> assertFalse(service.isIn(point, loadingArea)));
        pointsOutsideBoth.forEach(point -> assertFalse(service.isIn(point, dumpingArea)));
    }

}