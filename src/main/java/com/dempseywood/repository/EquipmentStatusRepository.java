package com.dempseywood.repository;

import com.dempseywood.model.EquipmentStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


public interface EquipmentStatusRepository extends CrudRepository<EquipmentStatus, Long> {

    List<EquipmentStatus> findByTimestampBetweenOrderByOperatorDescTimestamp(Date startOfDay, Date time);

    List<EquipmentStatus> findByTimestampBetweenOrderByEquipmentDescTimestamp(Date startOfDay, Date time);
}
