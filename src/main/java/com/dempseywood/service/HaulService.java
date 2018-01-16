package com.dempseywood.service;

import com.dempseywood.model.Haul;
import com.dempseywood.model.Task;
import com.dempseywood.model.UpdateTaskRequest;
import com.dempseywood.model.dto.FinishHaulRequest;
import com.dempseywood.model.dto.StartHaulRequest;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.repository.TaskRepository;
import com.dempseywood.repository.UpdateTaskRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HaulService {

    @Autowired
    private HaulRepository haulRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UpdateTaskRequestRepository updateTaskRequestRepository;

    public List<Haul> findHauls(Haul filter){


        List<Haul> result = new ArrayList<>();
        return result;
    }

    public Haul buildNewHaulFromInput(StartHaulRequest input){
            Haul haul = new Haul();
            haul.setUuid(input.getUuid());
            haul.setImei(input.getImei());
            haul.setOperator(input.getOperator());
            haul.setTask(taskRepository.findByName(input.getTask()));
            haul.setEquipment(equipmentRepository.findByName(input.getEquipment()));
            haul.setLoadLongitude(input.getLoadLongitude());
            haul.setLoadLatitude(input.getLoadLatitude());
            haul.setLoadTime(input.getLoadTime());
            haul = haulRepository.save(haul);
            return haul;

    }

    public boolean isValidHaul(StartHaulRequest input) {
        boolean isEquipmentValid =equipmentService.isEquipmentNameValid(input.getEquipment());
        boolean isTaskValid = taskService.isTaskNameValid(input.getTask());
        if(isEquipmentValid && isTaskValid){
            return true;
        }
        else{
            return false;
        }
    }

    public Haul updateHaulForUnload(Integer haulId, FinishHaulRequest input) {
        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul == null){
            throw new RuntimeException("Haul not found!  haulId: " + haulId);
        }
        existingHaul.setUnloadTime(input.getUnloadTime());
        existingHaul.setUnloadLatitude(input.getUnloadLatitude());
        existingHaul.setUnloadLongitude(input.getUnloadLongitude());
        return haulRepository.save(existingHaul);
    }

    public Haul updateTask(Integer haulId, UpdateTaskRequest input){
        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul == null){
            throw new RuntimeException("Haul not found!  haulId: " + haulId);
        }
        Task newTask = taskRepository.findByName(input.getTask());
        if(newTask == null){
            throw new RuntimeException("Task not found!  task: " + input.getTask());
        }
        existingHaul.setTask(newTask);
        updateTaskRequestRepository.save(input);
        return haulRepository.save(existingHaul);
    }
}
