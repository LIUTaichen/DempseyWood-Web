package com.dempseywood.entity.repository;

import com.dempseywood.entity.LatLng;
import org.springframework.data.repository.CrudRepository;

public interface LatLngRepository extends CrudRepository<LatLng, Integer>{
    public void deleteByGeofenceId(Integer id);
}
