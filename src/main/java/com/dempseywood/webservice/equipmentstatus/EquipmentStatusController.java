package com.dempseywood.webservice.equipmentstatus;

import com.dempseywood.greetings.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/status")
public class EquipmentStatusController {

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewEquipmentStatus(@RequestBody EquipmentStatus status) {
        EquipmentStatus newStatus = new EquipmentStatus();
        newStatus.setEquipment(status.getEquipment());
        newStatus.setOperator(status.getOperator());
        newStatus.setStatus(status.getStatus());
       // newStatus.setTimestamp(status.getTimestamp());
        equipmentStatusRepository.save(newStatus);

        return "Saved";
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody EquipmentStatus getEquipmentStatus() {
        EquipmentStatus newStatus = new EquipmentStatus();
        newStatus.setEquipment("Scraper JCZ234");
        newStatus.setOperator("John Smith");
        newStatus.setStatus("Loaded");
        return newStatus;
       // return equipmentStatusRepository.find
    }
}


