package com.dempseywood.repository;

import com.dempseywood.model.Haul;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface HaulRepository extends CrudRepository<Haul, Integer>,JpaSpecificationExecutor<Haul> {
    public Haul findOneByUuid(String uuid);




    List<Haul> findByLoadTimeAfterAndUnloadTimeBeforeAndEquipmentIdInOrderByEquipmentDescLoadTime(Date startTime, Date endTime, List<Integer> equipmentIds);

}
