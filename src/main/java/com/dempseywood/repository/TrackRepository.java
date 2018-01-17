package com.dempseywood.repository;

import com.dempseywood.model.locationbased.Track;
import org.springframework.data.repository.CrudRepository;

public interface TrackRepository  extends CrudRepository<Track, Long> {

}
