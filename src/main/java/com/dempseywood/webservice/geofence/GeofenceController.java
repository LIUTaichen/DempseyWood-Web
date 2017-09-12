package com.dempseywood.webservice.geofence;

import com.dempseywood.model.Geofence;
import com.dempseywood.model.GeofencesTO;
import com.dempseywood.model.Reading;
import com.dempseywood.model.Track;
import com.dempseywood.repository.GeofenceRepository;
import com.dempseywood.repository.LatLngRepository;
import com.dempseywood.repository.ReadingRepository;
import com.dempseywood.repository.TrackRepository;
import com.dempseywood.greetings.Greeting;
import com.dempseywood.service.ProjectService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

	@Autowired
	private GeofenceRepository geofenceRepository;

	@Autowired
	private LatLngRepository latLngRepository;

	@Autowired
	private ProjectService projectService;


	

	@RequestMapping(path = "/test", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public @ResponseBody Greeting createTestData(@RequestBody List<GeofencesTO> geofences) {
		Calendar calendar = Calendar.getInstance();
		geofences.forEach(geofence -> geofence.getLatlngs().forEach(latlng -> {
			Reading reading = new Reading(latlng);
			reading.setTrackerId(9);
			reading.setTime(calendar.getTime());
			calendar.add(Calendar.SECOND, 10);
			readingRepo.save(reading);

		}));

		Greeting greeting = new Greeting(0, "okkkk");
		return greeting;
	}

	@RequestMapping(path = "/tracks", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public @ResponseBody List<Track> getTracks(@RequestParam String startDateString,
			@RequestParam String endDateString) {
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

			return trackList;
		} catch (ParseException e) {
			log.error("error", e);

			return null;
		}

	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public @ResponseBody List<Geofence> getAllGeofences(Principal principal) {
		String email = principal.getName();
		Integer projectId = projectService.getProjectIdFromUserEmail(email);
		log.debug("calling getAllGeofences");
		List<Geofence> geofences = geofenceRepository.findByProjectId(projectId);
		return geofences;

	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public @ResponseBody Geofence createGeofence(@RequestBody Geofence geofence, Principal principal) {
		String email = principal.getName();
		Integer projectId = projectService.getProjectIdFromUserEmail(email);
		log.debug("calling createGeofence");
		log.info(geofence.getVertices().size() + "");
		geofence.setProjectId(projectId);
		Geofence savedGeofence = geofenceRepository.save(geofence);
		return savedGeofence;

	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Transactional
	public @ResponseBody void updateGeofences(@RequestBody List<Geofence> geofences, Principal principal) {
		log.debug("calling updateGeofences");
		String email = principal.getName();
		Integer projectId = projectService.getProjectIdFromUserEmail(email);
		geofences.forEach(geofence -> {
			latLngRepository.deleteByGeofenceId(geofence.getId());
			geofence.setProjectId(projectId);
		});
		geofenceRepository.save(geofences);

	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public @ResponseBody void deleteGeofences(@RequestBody List<Geofence> geofences) {
		log.debug("calling deleteGeofences");
		geofenceRepository.delete(geofences);
	}

}
