package com.dempseywood.webservice.equipmentstatus;

import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;


public interface EquipmentStatusRepository extends CrudRepository<EquipmentStatus, Long> {

    List<EquipmentStatus> findByTimestampBetweenOrderByOperatorDescTimestamp(Date startOfDay, Date time);
}
