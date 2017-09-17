package com.dempseywood.webservice;

import com.dempseywood.email.EmailService;
import com.dempseywood.model.*;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.service.ProjectService;
import com.dempseywood.service.ReportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;


    @Autowired
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;


    @RequestMapping(path = "/api/status/report", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String generateReport(Principal principal, Map<String, Object> model) {

        //TODO: remove the hardcoded email
        String email = "jason.liu@dempseywood.co.nz";
        if(principal != null){
            email = principal.getName();
        }
        Integer projectId = projectService.getProjectIdFromUserEmail(email);

        List<HaulSummary> summaryList = reportService.getSummaryList(projectId);
        List<HaulSummary> summaryByMachine = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getEquipment).thenComparing(HaulSummary::getLoadType)).collect(Collectors.toList());
        List<HaulSummary> summaryByLoadType = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getLoadType).thenComparing(HaulSummary::getEquipment)).collect(Collectors.toList());
       Map<String, BaseLoadCountSummary> map = new HashMap<String, BaseLoadCountSummary>();

        for(HaulSummary summary : summaryByMachine){
            BaseLoadCountSummary group = map.get(summary.getEquipment());
            if(group == null){
                group = new BaseLoadCountSummary();
                map.put(summary.getEquipment(), group);
            }
            group.add(summary);
        }
        BaseLoadCountSummary byMachineTable = new BaseLoadCountSummary();
        map.entrySet().forEach(entry -> {
            entry.getValue().compute();
            byMachineTable.add(entry.getValue());
        });
        byMachineTable.compute();
        model.put("byMachineTable", byMachineTable);
        model.put("summaryByMachine", summaryByMachine);
        model.put("summaryByLoadType", summaryByLoadType);
        return "loadCountSummary";
    }

    @RequestMapping(path = "/api/status/generateTestData", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateTestData() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDateString = "2017-09-12 08:00:00";
        String toDateString = "2017-09-12 11:00:00";
        Date fromDate = sdf.parse(fromDateString);
        Date toDate = sdf.parse(toDateString);
        List<EquipmentStatus> statusList = equipmentStatusRepository.findByTimestampBetweenOrderByEquipmentDescTimestamp(fromDate,toDate );
        for(EquipmentStatus status: statusList){

            Calendar calendar = Calendar.getInstance();
            entityManager.detach(status);
            status.setId(null);
            Date timestamp = status.getTimestamp();
            Calendar oldTime = Calendar.getInstance();
            oldTime.setTime(timestamp);

            oldTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            oldTime.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
            oldTime.set(Calendar.HOUR, oldTime.get(Calendar.HOUR) - 1);
            status.setTimestamp(oldTime.getTime());

        }
        equipmentStatusRepository.save(statusList);

    }

    @RequestMapping(path="api/status/email", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String sendEmail(Principal principal){
        //TODO: remove the hardcoded email
        String email = "jason.liu@dempseywood.co.nz";
        if(principal != null){
            email = principal.getName();
        }
        Integer projectId = projectService.getProjectIdFromUserEmail(email);
        List<EquipmentStatus> statusList = reportService.getEquipmentStatusForTodayByProjectId(projectId);
        Map<String, Equipment> equipmentMap = projectService.getEquipmentsForProject(projectId).stream().collect(Collectors.toMap(Equipment::getName, p -> p));
        Map<String, Double> taskToRevenueMap = projectService.getTaskRevenueMapForProject(projectId);
        List<Haul> haulList = reportService.convertEventsToHauls(statusList,taskToRevenueMap,equipmentMap );
        List<HaulSummary> summaryList = reportService.getSummaryFromHauls(haulList);
        Workbook workbook = reportService.writeReportForProject(statusList, haulList,  summaryList);
        String content = reportService.buildEmailContentFromSummary(summaryList, "loadCountSummary");


        emailService.send(workbook, content );
        return "email sent";
    }


}
