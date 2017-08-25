package com.dempseywood.webservice.geofence;

import com.dempseywood.entity.Geofence;
import com.dempseywood.entity.Reading;
import com.dempseywood.entity.Track;
import com.dempseywood.entity.repository.ReadingRepository;
import com.dempseywood.entity.repository.TrackRepository;
import com.dempseywood.greetings.Greeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/geofence")
public class GeofenceController {

    private Logger log = LoggerFactory.getLogger(GeofenceController.class);


    @Autowired
    private GeofenceService service;

    @Autowired
    private ReadingRepository readingRepo;

    @Autowired
    private TrackRepository trackRepository;


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

        List<Reading> readingList =       readingRepo.findByTrackerIdAndTimeBetweenOrderByTime(9,cal.getTime(), new Date() );
        List<Track> trackList = new ArrayList<Track>();

        Double[][] readings = new Double[readingList.size()][2];
        Track trackOfReading = null;
        for(int i = 0; i<readingList.size() ; i++){
            trackOfReading = null;
            Reading reading = readingList.get(i);
            for(Track track : trackList){
                if(track.getId() == reading.getTrackId()){
                    trackOfReading = track;
                }
            }
            if(trackOfReading == null){
                trackOfReading = new Track();
                trackOfReading.setId(reading.getTrackId());
                trackList.add(trackOfReading);
            }
            trackOfReading.getReadings().add(reading);
        }
        model.put("tracks", trackList);
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
        List<LoadCount> count = service.getLoadCounts(geofences, readingRepo.findByTrackerId(9));
        Greeting greeting = new Greeting(0, count.size() + "");
        return greeting;
    }

    @RequestMapping(path="/test", method = RequestMethod.POST,  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Greeting createTestData(@RequestBody List<GeofencesTO> geofences) {
        Calendar calendar = Calendar.getInstance();
        geofences.forEach(geofence -> geofence.getLatlngs().forEach(latlng-> {
            Reading reading = new Reading(latlng);
            reading.setTrackerId(9);
            reading.setTime(calendar.getTime());
            calendar.add(Calendar.SECOND, 10);
            readingRepo.save(reading);

        }));


        Greeting greeting = new Greeting(0,  "okkkk");
        return greeting;
    }

    @RequestMapping(path="/tracks", method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    List<Track> getTracks(@RequestParam String startDateString,  @RequestParam String endDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.HOUR, 24);
            calendar.add(Calendar.MILLISECOND, -1);
            endDate = calendar.getTime();

        List<Reading > readingList =       readingRepo.findByTrackerIdAndTimeBetweenOrderByTime(9,startDate, endDate );
        List<Track> trackList = new ArrayList<Track>();

        Double[][] readings = new Double[readingList.size()][2];
        Track trackOfReading = null;
        for(int i = 0; i<readingList.size() ; i++){
            trackOfReading = null;
            Reading reading = readingList.get(i);
            for(Track track : trackList){
                if(track.getId() == reading.getTrackId()){
                    trackOfReading = track;
                }
            }
            if(trackOfReading == null){
                trackOfReading = new Track();
                trackOfReading.setId(reading.getTrackId());
                trackList.add(trackOfReading);
            }
            trackOfReading.getReadings().add(reading);
        }

        return trackList;}
        catch(ParseException e){
            log.error("error", e);

            return null;
        }

    }


}
