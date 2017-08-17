package com.dempseywood.navixy.tracker;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingRepository extends CrudRepository<Reading, Long> {


    public List<Reading> findByTrackerId(int i);

}
