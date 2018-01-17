package com.dempseywood.webservice;

import com.dempseywood.model.*;
import com.dempseywood.model.report.HaulSummary;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.service.ProjectService;
import com.dempseywood.service.ReportService;
import com.dempseywood.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;




    @PersistenceContext
    private EntityManager entityManager;

    private Logger log = LoggerFactory.getLogger(ReportController.class);


    @RequestMapping(path = "/api/status/report", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String generateReport(Principal principal, Map<String, Object> model) {

        //TODO: remove the hardcoded email
        String email = "jason.liu@dempseywood.co.nz";
        if(principal != null){
            email = principal.getName();
        }
        Project project = projectService.getProjectFromUserEmail(email);
        Integer projectId = project.getId();
        List<HaulSummary> summaryList = reportService.getSummaryList(projectId);
        model.putAll(reportService.getLoadCountVariableMap(summaryList));
        model.put("projectName",project.getName() );
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
    String sendEmail(Principal principal, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate ){
        //TODO: remove the hardcoded email
        String email = "jason.liu@dempseywood.co.nz";
            if(principal != null){
            email = principal.getName();
        }
        if(fromDate == null) {
            fromDate = new Date();
            toDate = new Date();
        } else if(toDate == null){
            toDate = fromDate;
        }
        Project project = projectService.getProjectFromUserEmail(email);
        Integer projectId = project.getId();
        Date startTime = DateTimeUtil.getInstance().getLastNZMidnight(fromDate);
        Date endTime = DateTimeUtil.getInstance().getNextNZMidNight(toDate);

        log.info("generating report for time between " + startTime + " and " + endTime );
        reportService.sendReportForProject(projectId, startTime, endTime);
        return "email sent";
    }


}
