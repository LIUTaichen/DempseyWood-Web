package com.dempseywood.repository;

import com.dempseywood.model.CostSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CostScheduleRepository extends CrudRepository<CostSchedule, Integer>{
    List<CostSchedule> findByProjectId(Integer projectId);
}
