package com.dempseywood.repository;

import com.dempseywood.model.LatLng;
import org.springframework.data.repository.CrudRepository;

public interface LatLngRepository extends CrudRepository<LatLng, Integer>{
    public void deleteByGeofenceId(Integer id);
}
