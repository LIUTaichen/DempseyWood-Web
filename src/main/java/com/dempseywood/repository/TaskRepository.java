package com.dempseywood.repository;

import com.dempseywood.model.RevenueSchedule;
import com.dempseywood.model.Task;
import org.springframework.data.repository.CrudRepository;
public interface TaskRepository extends CrudRepository<Task, Integer> {


    public Iterable<Task> findByProjectId(Integer projectId);

    Task findByName(String name);
}
