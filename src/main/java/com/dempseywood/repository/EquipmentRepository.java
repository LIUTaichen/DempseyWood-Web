package com.dempseywood.repository;

import com.dempseywood.model.Equipment;
import org.springframework.data.repository.CrudRepository;

public interface EquipmentRepository extends CrudRepository<Equipment, Integer> {
    Equipment findByName(String equipment);
}
