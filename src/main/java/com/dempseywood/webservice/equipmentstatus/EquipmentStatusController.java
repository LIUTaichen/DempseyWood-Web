package com.dempseywood.webservice.equipmentstatus;

import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.service.ProjectService;
import com.dempseywood.service.ReportService;
import com.dempseywood.service.RepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/status")
public class EquipmentStatusController {

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;
    @Autowired
    private DailyExcelReportView report;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectService projectService;


    @Autowired
    private RepostService repostService;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody String addNewEquipmentStatus(@RequestBody Iterable<EquipmentStatus> statusList) {
    	for(EquipmentStatus status: statusList){
    		status.setId(null);
    	}
        repostService.repost(statusList);
        equipmentStatusRepository.save(statusList);


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


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public @ResponseBody String updateEquipmentStatus(@RequestBody Iterable<EquipmentStatus> statusList) {
        List<EquipmentStatus> statusToBeUpdated = new ArrayList<EquipmentStatus>();
        for(EquipmentStatus status: statusList){
            EquipmentStatus oldRecord = equipmentStatusRepository.findTopByImeiAndTimestamp(status.getImei(), status.getTimestamp());
            if(oldRecord != null){
                oldRecord.setTask(status.getTask());
                statusToBeUpdated.add(oldRecord);
            }

        }
        if(!statusToBeUpdated.isEmpty()){
            equipmentStatusRepository.save(statusToBeUpdated);
        }
        return "Updated";
    }






}


