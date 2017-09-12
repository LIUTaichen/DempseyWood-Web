package com.dempseywood.webservice.geofence;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dempseywood.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dempseywood.repository.GeofenceRepository;
import com.dempseywood.repository.LatLngRepository;
import com.dempseywood.repository.ReadingRepository;
import com.dempseywood.repository.TrackRepository;
import com.dempseywood.service.ProjectService;

@Controller
@RequestMapping
public class LoadcountController {

	private Logger log = LoggerFactory.getLogger(LoadcountController.class);

	@Autowired
	private GeofenceService service;

	@Autowired
	private ReadingRepository readingRepo;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private GeofenceRepository geofenceRepository;

	@Autowired
	private LatLngRepository latLngRepository;

	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = { "/" })
	public String load(Map<String, Object> model, Principal principal) {
		String email = principal.getName();
		Integer projectId = projectService.getProjectIdFromUserEmail(email);
		Date startOfWeek = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startOfWeek);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		log.debug(cal.toString());

		model.put("startOfWeek", cal.getTime());

		List<Reading> readingList = readingRepo.findByTrackerIdAndTimeBetweenOrderByTime(9, cal.getTime(), new Date());
		List<Track> trackList = new ArrayList<Track>();

		Double[][] readings = new Double[readingList.size()][2];
		Track trackOfReading = null;
		for (int i = 0; i < readingList.size(); i++) {
			trackOfReading = null;
			Reading reading = readingList.get(i);
			for (Track track : trackList) {
				if (track.getId() == reading.getTrackId()) {
					trackOfReading = track;
				}
			}
			if (trackOfReading == null) {
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

	@RequestMapping(path = "/loadcount", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public String getLoadCount(@RequestParam String startDateString, @RequestParam String endDateString,
			Principal principal, Map<String, Object> model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = sdf.parse(startDateString);
			Date endDate = sdf.parse(endDateString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.HOUR, 24);
			calendar.add(Calendar.MILLISECOND, -1);
			endDate = calendar.getTime();
			List<Reading> readingList = readingRepo.findByTrackerIdAndTimeBetweenOrderByTime(9, startDate, endDate);
			String email = principal.getName();
			Integer projectId = projectService.getProjectIdFromUserEmail(email);
			List<Geofence> geofences = geofenceRepository.findByProjectId(projectId);
			List<Trip> counts = service.getLoadCounts(geofences, readingList);
			LoadCounts countsEntry = null;
			if (!counts.isEmpty()) {
				countsEntry = new LoadCounts();
				countsEntry.setVehicle(counts.get(0).getVehicle());
				for (Trip trip : counts) {
					String material = trip.getDumpingZone().getMaterial();
					countsEntry.increment(material);
				}
			}
			model.put("loadcount", countsEntry);
		} catch (Exception e) {
			log.error("error", e);
		}
		return "loadcount::resultsTable";
	}
	
	@RequestMapping(value = { "/invalidSession" })
	public String load(Map<String, Object> model) {
		return "invalidSession";
	}

}
