package com.dempseywood.webservice.geofence;

import com.dempseywood.greetings.Greeting;
import com.dempseywood.navixy.tracker.Geofence;
import com.dempseywood.navixy.tracker.Reading;
import com.dempseywood.navixy.tracker.ReadingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/geofence")
public class GeofenceController {

    private Logger log = LoggerFactory.getLogger(GeofenceController.class);


    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private GeofenceService service;

    @Autowired
    private ReadingRepository readingRepo;


    @RequestMapping(path="/data", method = RequestMethod.POST,  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Greeting addNewEquipmentStatus(@RequestBody GeofencesTO geofences) {
        System.out.println("loading area: ");
        geofences.getLoad().getVertices().forEach(latlng  -> System.out.println(latlng));
        Geofence loadingArea = new Geofence();
        loadingArea.setVertices(geofences.getLoad().getVertices());
        System.out.println("dumping area: ");
        geofences.getDump().getVertices().forEach(latlng  -> System.out.println(latlng));
        Geofence dumpingArea = new Geofence();
        dumpingArea.setVertices(geofences.getDump().getVertices());
        Integer count = service.getNumberOfTrips(loadingArea, dumpingArea, readingRepo.findByTrackerId(9));
        Greeting greeting = new Greeting(0, count.toString());
        return greeting;
    }

    @RequestMapping("/loadfromnavixy")
    public String load(Map<String, Object> model){

        model.put("message","Welcome to Dempsey Wood Load Counting Demo");
        Date startOfWeek = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        log.debug(cal.toString());

        model.put("startOfWeek", cal.getTime());

        List<Reading > readingList =       readingRepository.findByTrackerId(9);
        Double[][] readings = new Double[readingList.size()][2];
        for(int i = 0; i<readingList.size() ; i++){
            readings[i] = new Double[]{readingList.get(i).getLat(), readingList.get(i).getLng()};
        }
        model.put("coordinates", readings);

        return "welcome";
    }

    @RequestMapping(method = RequestMethod.POST,  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Greeting getLoadCount(@RequestBody List<GeofencesTO> geofences) {
        System.out.println("loading area: ");
        /*geofences.getLoad().getVertices().forEach(latlng  -> System.out.println(latlng));
        Geofence loadingArea = new Geofence();
        loadingArea.setVertices(geofences.getLoad().getVertices());
        System.out.println("dumping area: ");
        geofences.getDump().getVertices().forEach(latlng  -> System.out.println(latlng));
        Geofence dumpingArea = new Geofence();
        dumpingArea.setVertices(geofences.getDump().getVertices());*/
        //Integer count = service.getNumberOfTrips(loadingArea, dumpingArea, readingRepo.findByTrackerId(9));
        Greeting greeting = new Greeting(0, "3");
        return greeting;
    }
}
