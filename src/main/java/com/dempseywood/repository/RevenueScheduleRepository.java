package com.dempseywood.repository;

import com.dempseywood.model.RevenueSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RevenueScheduleRepository extends CrudRepository<RevenueSchedule, Integer>{
    List<RevenueSchedule> findByProjectId(Integer projectId);
}
