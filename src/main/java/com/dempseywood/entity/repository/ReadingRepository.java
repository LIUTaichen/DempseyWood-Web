package com.dempseywood.entity.repository;

import com.dempseywood.entity.Reading;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReadingRepository extends CrudRepository<Reading, Long> {


    public List<Reading> findByTrackerId(int i);

    public List<Reading> findByTrackerIdAndTimeBetween(int i, Date startDate, Date endDate);

    public List<Reading> findByTrackerIdAndTimeBetweenOrderByTime(int i, Date time, Date date);
}
