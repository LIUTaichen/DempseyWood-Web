package com.dempseywood.service;


import com.dempseywood.model.Task;
import com.dempseywood.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {


    @Autowired
    private TaskRepository taskRepository;

    public boolean isTaskNameValid(String name){
        Task task = taskRepository.findByName(name);
        if(task == null){
            return false;
        }else{
            return true;
        }
    }
}
