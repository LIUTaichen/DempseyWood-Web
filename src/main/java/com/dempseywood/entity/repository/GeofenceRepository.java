package com.dempseywood.entity.repository;

import com.dempseywood.entity.Geofence;
import com.dempseywood.entity.Reading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeofenceRepository extends CrudRepository<Geofence, Integer> {
    public List<Geofence> findAll();

}
