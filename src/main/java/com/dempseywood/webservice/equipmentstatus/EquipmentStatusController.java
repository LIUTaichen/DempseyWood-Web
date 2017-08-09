package com.dempseywood.webservice.equipmentstatus;

import com.dempseywood.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/status")
public class EquipmentStatusController {

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;
    @Autowired
    private DailyExcelReportView report;

    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewEquipmentStatus(@RequestBody EquipmentStatus status) {
/*        EquipmentStatus newStatus = new EquipmentStatus();
        newStatus.setEquipment(status.getEquipment());
        newStatus.setOperator(status.getOperator());
        newStatus.setStatus(status.getStatus());
        newStatus.setTimestamp(status.getTimestamp());*/
        equipmentStatusRepository.save(status);

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

    @RequestMapping(path = "/report", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String generateReport() {
        report.writeReport();
        //ModelAndView view = new ModelAndView("dailyExcelReportView", "listBooks", null);
        return "/report";
    }

    @RequestMapping(path="/email", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String sendEmail(){
        emailService.send();
        return "email sent";
    }


}


