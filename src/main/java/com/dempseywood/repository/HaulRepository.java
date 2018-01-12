package com.dempseywood.repository;

import com.dempseywood.model.Haul;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface HaulRepository extends CrudRepository<Haul, Integer>,JpaSpecificationExecutor<Haul> {
    public Haul findOneByUuid(String uuid);



    //public List<Haul> findByImeiAndLoadTimeAfterAndUnloadTimeBefore(String imei, Date fromDate, Date toDate);
}
