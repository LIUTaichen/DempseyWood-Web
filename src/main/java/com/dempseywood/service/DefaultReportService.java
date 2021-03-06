package com.dempseywood.service;

import com.dempseywood.email.EmailService;
import com.dempseywood.model.*;
import com.dempseywood.model.report.BaseLoadCountSummary;
import com.dempseywood.model.report.HaulReportEntry;
import com.dempseywood.model.report.HaulSummary;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.repository.ProjectRepository;
import com.dempseywood.repository.RevenueScheduleRepository;
import com.dempseywood.util.DateTimeUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultReportService implements ReportService {


    public static final int MILLISECONDS_IN_HOUR = 3600000;
    private Logger log = LoggerFactory.getLogger("DefaultReportService");

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HaulRepository haulRepository;

    @Autowired
    private RevenueScheduleRepository revenueScheduleRepository;



    @Override
    public Workbook writeReportForProject(List<HaulReportEntry> haulReportEntryList, List<HaulSummary> summaryList) {

        Workbook report = new SXSSFWorkbook();

        new HaulsSheetWriter("Hauls").writeHauls(report, haulReportEntryList);
        new SummaryByMachineSheetWriter("Summary By Machine").writeSheet(report, summaryList);
        new SummaryByLoadTypeSheetWriter("Summary By Haul Type").writeSheet(report, summaryList);
        int index = 0;
        report.setSheetOrder("Summary By Machine", index++);
        report.setSheetOrder("Summary By Haul Type", index++);
        report.setSheetOrder("Hauls", index++);
        return report;
    }
    @Override
    public List<HaulSummary> getSummaryFromHauls(List<HaulReportEntry> haulReportEntryList) {
        Map<String, Map<String, HaulSummary>> combined = new HashMap<String, Map<String, HaulSummary>>();
        for (HaulReportEntry haulReportEntry : haulReportEntryList) {
            Map<String, HaulSummary> byLoadTypeMap = combined.get(haulReportEntry.getEquipment());
            if (byLoadTypeMap == null) {
                byLoadTypeMap = new HashMap<String, HaulSummary>();
                combined.put(haulReportEntry.getEquipment(), byLoadTypeMap);
            }
            HaulSummary entry = byLoadTypeMap.get(haulReportEntry.getLoadType());
            if (entry == null) {
                entry = new HaulSummary();
                byLoadTypeMap.put(haulReportEntry.getLoadType(), entry);
                entry.setEquipment(haulReportEntry.getEquipment());
                entry.setLoadType(haulReportEntry.getLoadType());
                entry.setLoadCount(0);
            }
            entry.setLoadCount(entry.getLoadCount() + 1);
            entry.setVolume(entry.getVolume() + haulReportEntry.getVolume());
            entry.setDuration(entry.getDuration() + haulReportEntry.getDuration());
            entry.setCost(entry.getCost() + haulReportEntry.getCost());
            entry.setRevenue(entry.getRevenue() + haulReportEntry.getRevenue());
            entry.setProfit(entry.getProfit() + haulReportEntry.getProfit());

        }
        List<HaulSummary> summaryList = new ArrayList<>();
        combined.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(entry -> summaryList.add(entry.getValue()));
        });

        return summaryList;
    }


    @Override
    public List<HaulSummary> getSummaryList(Integer projectId) {
        Date time = new Date();
        Date startOfDay = DateTimeUtil.getInstance().getTimeOfBeginningOfToday(time);
        List<Haul> hauls = this.findByProjectIdAndLoadTimeAfterAndUnloadTimeBefore(projectId,startOfDay,  time);
        List<HaulReportEntry> haulReportEntryList = getHaulReportFromHauls(hauls);
        List<HaulSummary> summaryList = getSummaryFromHauls(haulReportEntryList);
        return summaryList;
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatusForTodayByProjectId(Integer projectId) {
        Date time = new Date();
        Date startOfDay = DateTimeUtil.getInstance().getTimeOfBeginningOfToday(time);
        return getEquipmentStatusByProjectIdAndTimestamp(projectId, startOfDay, time );
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatusByProjectIdAndTimestamp(Integer projectId, Date startTime, Date endTime ) {
        log.debug("retrieving all equipmentStatus for the project with projectId: [" + projectId + "]");
        List<EquipmentStatus> resultList = new ArrayList<>();
        Project project = projectRepository.findOne(projectId);
        if(project == null){
            log.error("::getNameToEquipmentMapForProject Unable to find any project with projectId: [" + projectId + "]");
            return resultList;
        }
        List<Equipment> equipmentList = projectService.getEquipmentsForProject(projectId);
        List<String> equipmentNames = equipmentList.stream().map(equipment -> equipment.getName()).collect(Collectors.toList());

        resultList = equipmentStatusRepository.findByTimestampBetweenAndEquipmentInOrderByEquipmentDescTimestamp(startTime, endTime, equipmentNames);
        log.info("Number of entries retrieved: " + resultList.size());
        return resultList;
    }



    @Override
    public String buildEmailContentFromSummary(Map<String, Object> variableMap, String template) {
        Context context = new Context();
        context.setVariables(variableMap);
        return templateEngine.process(template, context);

    }


    @Override
    public Map<String, Object> getLoadCountVariableMap(List<HaulSummary> summaryList){
        Map<String, Object> variableMap = new HashMap<String, Object>();

        List<HaulSummary> summaryByMachine = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getEquipment).thenComparing(HaulSummary::getLoadType)).collect(Collectors.toList());
        List<HaulSummary> summaryByLoadType = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getLoadType).thenComparing(HaulSummary::getEquipment)).collect(Collectors.toList());
        Map<String, BaseLoadCountSummary> byMachineMap = new HashMap<String, BaseLoadCountSummary>();

        for(HaulSummary summary : summaryByMachine){
            BaseLoadCountSummary group = byMachineMap.get(summary.getEquipment());
            if(group == null){
                group = new BaseLoadCountSummary();
                byMachineMap.put(summary.getEquipment(), group);
            }
            group.add(summary);
        }
        BaseLoadCountSummary byMachineTable = new BaseLoadCountSummary();
        byMachineMap.entrySet().forEach(entry -> {
            entry.getValue().compute();
            byMachineTable.add(entry.getValue());
        });
        byMachineTable.compute();
        byMachineTable.getEntries().sort(Comparator.comparing(HaulSummary::getEquipment));


        Map<String, BaseLoadCountSummary> byLoadTypeMap = new HashMap<String, BaseLoadCountSummary>();

        for(HaulSummary summary : summaryByLoadType){
            BaseLoadCountSummary group = byLoadTypeMap.get(summary.getLoadType());
            if(group == null){
                group = new BaseLoadCountSummary();
                byLoadTypeMap.put(summary.getLoadType(), group);
            }
            group.add(summary);
        }
        BaseLoadCountSummary byLoadTypeTable = new BaseLoadCountSummary();
        byLoadTypeMap.entrySet().forEach(entry -> {
            entry.getValue().compute();
            byLoadTypeTable.add(entry.getValue());
        });
        byLoadTypeTable.compute();
        byLoadTypeTable.getEntries().sort(Comparator.comparing(HaulSummary::getLoadType));

        variableMap.put("byMachineTable", byMachineTable);
        variableMap.put("byLoadTypeTable", byLoadTypeTable);
        return  variableMap;
    }

    @Override
    @Transactional
    public void sendReportForProject(Integer projectId, Date startTime, Date endTime) {
        Project project = projectRepository.findOne(projectId);
        List<Haul> hauls = this.findByProjectIdAndLoadTimeAfterAndUnloadTimeBefore(projectId,startTime,  endTime);
        List<HaulReportEntry> haulReportEntryList = this.getHaulReportFromHauls(hauls);
        List<HaulSummary> summaryList = this.getSummaryFromHauls(haulReportEntryList);
        Workbook workbook = this.writeReportForProject(haulReportEntryList,  summaryList);
        Map<String, Object> variableMap = this.getLoadCountVariableMap(summaryList);
        variableMap.put("projectName",project.getName() );
        String content = this.buildEmailContentFromSummary(variableMap, "loadCountSummary");
        List<String> emailList = projectService.getEmailOfProjectManagers(projectId);
        String[] emails = new String[emailList.size()];
        emails = emailList.toArray(emails);
        emailService.send(workbook, content,  emails,startTime, project.getName());
    }

    private List<Haul> findByProjectIdAndLoadTimeAfterAndUnloadTimeBefore(Integer projectId, Date startTime, Date endTime) {
        log.debug("retrieving all hauls for the project with projectId: [" + projectId + "]");
        List<Haul> resultList = new ArrayList<>();
        Project project = projectRepository.findOne(projectId);
        if(project == null){
            log.error("::getNameToEquipmentMapForProject Unable to find any project with projectId: [" + projectId + "]");
            return resultList;
        }
        List<Equipment> equipmentList = projectService.getEquipmentsForProject(projectId);
        List<Integer> equipmentIds = equipmentList.stream().map(equipment -> equipment.getId()).collect(Collectors.toList());

        resultList = haulRepository.findByLoadTimeAfterAndUnloadTimeBeforeAndEquipmentIdInOrderByEquipmentDescLoadTime(startTime, endTime, equipmentIds);
        log.info("Number of entries retrieved: " + resultList.size());
        return resultList;
    }

    private List<HaulReportEntry> getHaulReportFromHauls(List<Haul> hauls) {

        List<HaulReportEntry> haulReportEntryList = new ArrayList<>();
        List<Haul> validHauls = hauls.stream()
                .filter(record -> isValidHaul(record))
                .collect(Collectors.toList());
        for (int i = 0; i < validHauls.size(); i ++) {
            HaulReportEntry haulReportEntry = new HaulReportEntry();
            Haul haul = validHauls.get(i);
            LocalDateTime haulStartTime = LocalDateTime.ofInstant(haul.getLoadTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime haulFinishTime = LocalDateTime.ofInstant(haul.getUnloadTime().toInstant(), ZoneId.systemDefault());
            //more in list
            if(i +1 < validHauls.size()){
                Haul nextHaul = validHauls.get(i+1);
                if(nextHaul.getEquipment().getId() == haul.getEquipment().getId()){
                    haulFinishTime = LocalDateTime.ofInstant(nextHaul.getUnloadTime().toInstant(), ZoneId.systemDefault());
                }
            }

            long duration = Duration.between(haulStartTime, haulFinishTime).toMinutes();
            haulReportEntry.setDuration(duration);
            Equipment equipment = haul.getEquipment();
            RevenueSchedule revenueSchedule = revenueScheduleRepository.findOne(haul.getTask().getId());
            haulReportEntry.setLoadTime(haul.getLoadTime());
            haulReportEntry.setUnloadTime(haul.getUnloadTime());

            haulReportEntry.setEquipment(equipment.getName());
            haulReportEntry.setLoadType(revenueSchedule.getName());
            if(equipment.getCapacity() == null){
                haulReportEntry.setVolume(0);
            }else {
                haulReportEntry.setVolume(equipment.getCapacity());
            }
            double costPerHour = 0;
            if(equipment.getCostPerHour() != null){
                costPerHour = equipment.getCostPerHour();
            }
            double cost = costPerHour / MILLISECONDS_IN_HOUR * Duration.between(haulStartTime, haulFinishTime).toMillis();
            haulReportEntry.setCost(cost);
            double revenue = haulReportEntry.getVolume() * revenueSchedule.getRevenue();
            haulReportEntry.setRevenue(revenue);
            double profit = revenue - cost;
            haulReportEntry.setProfit(profit);

            haulReportEntryList.add(haulReportEntry);
        }

        return haulReportEntryList;
    }

    private boolean isValidHaul(Haul haul){
        if(haul.getEquipment() == null){
            return false;
        }
        if(haul.getTask() == null){
            return false;
        }
        if(haul.getLoadTime() == null){
            return false;
        }
        if(haul.getUnloadTime() == null){
            return false;
        }
        return true;
    }



}
