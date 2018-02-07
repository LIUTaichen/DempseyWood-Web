package com.dempseywood.webservice;

import com.dempseywood.model.RevenueSchedule;
import com.dempseywood.model.Task;
import com.dempseywood.repository.RevenueScheduleRepository;
import com.dempseywood.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    List<Task> getAllTasks(@RequestParam(required = false)  Integer projectId) {
        log.debug("calling get all tasks");
        List<Task> allTasks = new ArrayList<Task>();
        if(projectId != null){
            taskRepository.findByProjectId(projectId).forEach(task -> allTasks.add(task));
        }else {
            taskRepository.findAll().forEach(task -> allTasks.add(task));
        }
        return allTasks;

    }
}
