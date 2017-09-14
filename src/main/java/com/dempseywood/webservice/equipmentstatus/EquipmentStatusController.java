package com.dempseywood.webservice.equipmentstatus;

import com.dempseywood.email.EmailService;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.service.ProjectService;
import com.dempseywood.service.ReportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody String addNewEquipmentStatus(@RequestBody Iterable<EquipmentStatus> statusList) {
    	for(EquipmentStatus status: statusList){
    		status.setId(null);
    	}
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









}


