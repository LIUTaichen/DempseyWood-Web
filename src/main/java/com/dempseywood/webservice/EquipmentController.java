package com.dempseywood.webservice;

import com.dempseywood.model.Equipment;
import com.dempseywood.repository.EquipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private Logger log = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentRepository equipmentRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    List<Equipment> getAllEquipments() {
        log.debug("calling getAllEquipments");
        List<Equipment> allEquipments = new ArrayList<Equipment>();
        equipmentRepository.findAll().forEach(equipment -> allEquipments.add(equipment));
        return allEquipments;

    }

}
