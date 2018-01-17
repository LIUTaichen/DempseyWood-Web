package com.dempseywood.service;


import com.dempseywood.model.Equipment;
import com.dempseywood.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public boolean isEquipmentNameValid(String name){

        Equipment equipment = equipmentRepository.findByName(name);
        if(equipment == null){
            return false;
        }else{
            return true;
        }

    }
}
