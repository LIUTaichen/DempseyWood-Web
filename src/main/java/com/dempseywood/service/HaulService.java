package com.dempseywood.service;

import com.dempseywood.model.Haul;
import com.dempseywood.model.Task;
import com.dempseywood.model.UpdateTaskRequest;
import com.dempseywood.model.dto.FinishHaulRequest;
import com.dempseywood.model.dto.HaulDTO;
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

    public List<HaulDTO> batchProcess(List<HaulDTO> hauls){
        List<HaulDTO>  returnList = new ArrayList<>();
        for(HaulDTO haulDTO: hauls){
            Haul existingHaul = haulRepository.findOneByUuid(haulDTO.getUuid());
            if(existingHaul == null){
                existingHaul = haulRepository.save(getHaulFromDto(haulDTO));
                returnList.add(getDTOFromHaul(existingHaul));
            }else{
                existingHaul.setUnloadTime(haulDTO.getUnloadTime());
                existingHaul.setUnloadLatitude(haulDTO.getUnloadLatitude());
                existingHaul.setUnloadLongitude(haulDTO.getUnloadLongitude());
                existingHaul = haulRepository.save(existingHaul);
                returnList.add(getDTOFromHaul(existingHaul));
            }
        }
        return returnList;
    }

    public HaulDTO getDTOFromHaul(Haul haul){
        HaulDTO dto = new HaulDTO();

        dto.setEquipment(haul.getEquipment().getName());
        dto.setImei(haul.getImei());
        dto.setTask(haul.getTask().getName());
        dto.setOperator(haul.getOperator());
        dto.setUuid(haul.getUuid());
        dto.setLoadTime(haul.getLoadTime());
        dto.setLoadLatitude(haul.getLoadLatitude());
        dto.setLoadLongitude(haul.getLoadLongitude());
        dto.setUnloadTime(haul.getUnloadTime());
        dto.setUnloadLatitude(haul.getUnloadLatitude());
        dto.setUnloadLongitude(haul.getUnloadLongitude());
        dto.setId(haul.getId());
        return dto;
    }

    public Haul getHaulFromDto(HaulDTO haulDTO){
        Haul haul = new Haul();

        haul.setEquipment(equipmentRepository.findByName(haulDTO.getEquipment()));
        haul.setImei(haulDTO.getImei());
        haul.setTask(taskRepository.findByName(haulDTO.getTask()));
        haul.setOperator(haulDTO.getOperator());
        haul.setUuid(haulDTO.getUuid());
        haul.setLoadTime(haulDTO.getLoadTime());
        haul.setLoadLatitude(haulDTO.getLoadLatitude());
        haul.setLoadLongitude(haulDTO.getLoadLongitude());
        haul.setUnloadTime(haulDTO.getUnloadTime());
        haul.setUnloadLatitude(haulDTO.getUnloadLatitude());
        haul.setUnloadLongitude(haulDTO.getUnloadLongitude());
        haul.setId(haulDTO.getId());
        return haul;
    }
}
