package com.dempseywood.repository;

import com.dempseywood.model.Geofence;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeofenceRepository extends CrudRepository<Geofence, Integer> {
    public List<Geofence> findAll();

    public List<Geofence> findByProjectId(Integer integer);
}
