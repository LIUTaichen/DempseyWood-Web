package com.dempseywood.repository;

import com.dempseywood.model.EquipmentStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


public interface EquipmentStatusRepository extends CrudRepository<EquipmentStatus, Long> {

    List<EquipmentStatus> findByTimestampBetweenOrderByOperatorDescTimestamp(Date startTime, Date endtTime);

    List<EquipmentStatus> findByTimestampBetweenOrderByEquipmentDescTimestamp(Date startTime, Date endtTime);

    List<EquipmentStatus> findByTimestampBetweenAndEquipmentInOrderByEquipmentDescTimestamp(Date startOfDay, Date time, List<String> equipmentNames);

    List<EquipmentStatus> findByTimestampAfterAndImeiOrderByTimestampDesc(Date startOfDay, String imei);

    EquipmentStatus findTopByImeiOrderByTimestampDesc(String imei);

    EquipmentStatus findTopByImeiAndTimestamp(String imei, Date timestamp);

}
