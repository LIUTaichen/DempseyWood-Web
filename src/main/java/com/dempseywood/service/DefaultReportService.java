package com.dempseywood.service;

import com.dempseywood.model.*;
import com.dempseywood.repository.CostScheduleRepository;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.repository.ProjectRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ProjectService projectService;


    @Override
    public Workbook writeReportForProject(List<EquipmentStatus> statusList, List<Haul> haulList, List<HaulSummary> summaryList) {

        Workbook report = new SXSSFWorkbook();

        new EventsSheetWriter("Full Details").writeSheet(report, statusList);
        new HaulsSheetWriter("Hauls").writeHauls(report, haulList);
        new SummaryByMachineSheetWriter("Summary By Machine").writeSheet(report, summaryList);
        new SummaryByLoadTypeSheetWriter("Summary By Load Type").writeSheet(report, summaryList);
        int index = 0;
        report.setSheetOrder("Summary By Machine", index++);
        report.setSheetOrder("Summary By Load Type", index++);
        report.setSheetOrder("Hauls", index++);
        report.setSheetOrder("Full Details", index++);
        return report;
    }

    @Override
    public List<HaulSummary> getSummaryFromHauls(List<Haul> haulList) {
        Map<String, Map<String, HaulSummary>> combined = new HashMap<String, Map<String, HaulSummary>>();
        for (Haul haul : haulList) {
            Map<String, HaulSummary> byLoadTypeMap = combined.get(haul.getEquipment());
            if (byLoadTypeMap == null) {
                byLoadTypeMap = new HashMap<String, HaulSummary>();
                combined.put(haul.getEquipment(), byLoadTypeMap);
            }
            HaulSummary entry = byLoadTypeMap.get(haul.getLoadType());
            if (entry == null) {
                entry = new HaulSummary();
                byLoadTypeMap.put(haul.getLoadType(), entry);
                entry.setEquipment(haul.getEquipment());
                entry.setLoadType(haul.getLoadType());
                entry.setLoadCount(0);
            }
            entry.setLoadCount(entry.getLoadCount() + 1);
            entry.setVolume(entry.getVolume() + haul.getVolume());
            entry.setDuration(entry.getDuration() + haul.getDuration());
            entry.setCost(entry.getCost() + haul.getCost());
            entry.setRevenue(entry.getRevenue() + haul.getRevenue());
            entry.setProfit(entry.getProfit() + haul.getProfit());

        }
        List<HaulSummary> summaryList = new ArrayList<>();
        combined.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(entry -> summaryList.add(entry.getValue()));
        });

        return summaryList;
    }

    public boolean isMatchingPair(EquipmentStatus loadingEvent, EquipmentStatus unloadingEvent){
        if(!isValidEquipmentStatus(loadingEvent) || !isValidEquipmentStatus(unloadingEvent)){
            return false;
        }
        else if(!loadingEvent.getStatus().equals("Loaded") && unloadingEvent.getStatus().equals("Unloaded")) {
            return false;
        }else if(!loadingEvent.getEquipment().equals(unloadingEvent.getEquipment())) {
            return false;
        }else if(!loadingEvent.getTask().equals(unloadingEvent.getTask())){
            return false;
        }
        return true;
    }

    public boolean isValidEquipmentStatus(EquipmentStatus event){
        if(event == null){
            return false;
        }
        else if(event.getEquipment() == null || event.getStatus() == null || event.getTask() == null){
            return false;
        }else if(event.getEquipment().isEmpty()|| event.getStatus().isEmpty() || event.getTask().isEmpty()){
                return false;
            }
        return true;
    }



    @Override
    public List<Haul> convertEventsToHauls(List<EquipmentStatus> statusList, Map<String, Double> revenueSchedule, Map<String, Equipment> equipmentMap) {
        List<Haul> haulList = new ArrayList<>();

        EquipmentStatus loadingEvent = null;
        EquipmentStatus unloadingEvent = null;
        String equipmentStringForCurrentHaul = "";
        for (EquipmentStatus event: statusList) {
            if(!isValidEquipmentStatus(event )){
                log.info("error processing record: " + event.toString());
                log.info("invalid equipment status");
                continue;
            }else if( !revenueSchedule.containsKey(event.getTask())){
                log.info("error processing record: " + event.toString());
                log.info("invalid task " + event.getTask());
                continue;
            } else if (!equipmentMap.containsKey(event.getEquipment())) {
                log.info("error processing record: " + event.toString());
                log.info("invalid equipment " + event.getEquipment());
                continue;
            }
            //next entry is for another equipment
            if (!event.getEquipment().equals(equipmentStringForCurrentHaul)) {
                equipmentStringForCurrentHaul = event.getEquipment();
                loadingEvent = null;
                unloadingEvent = null;
            }
            if (event.getStatus().equals("Loaded") && unloadingEvent == null) {
                loadingEvent = event;
            } else if (isMatchingPair(loadingEvent, event)) {
                unloadingEvent = event;
            }
            if (loadingEvent != null && unloadingEvent != null) {
                Haul haul = new Haul();
                Equipment equipment = equipmentMap.get(loadingEvent.getEquipment());
                haul.setLoadTime(loadingEvent.getTimestamp());
                haul.setUnloadTime(unloadingEvent.getTimestamp());
                int indexOfUnloadingEvent = statusList.indexOf(unloadingEvent);
                int indexOfNextEvent = indexOfUnloadingEvent + 1;
                LocalDateTime haulStartTime = LocalDateTime.ofInstant(loadingEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                LocalDateTime haulFinishTime = LocalDateTime.ofInstant(unloadingEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                if (indexOfNextEvent < statusList.size()) {
                    EquipmentStatus nextEvent = statusList.get(indexOfNextEvent);
                    if (nextEvent.getEquipment().equals(equipmentStringForCurrentHaul)) {
                        haulFinishTime = LocalDateTime.ofInstant(nextEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                    }
                }
                long duration = Duration.between(haulStartTime, haulFinishTime).toMinutes();
                haul.setDuration(duration);
                haul.setEquipment(equipment.getName());
                haul.setLoadType(loadingEvent.getTask());
                if(equipment.getCapacity() == null){
                    haul.setVolume(0);
                }else {
                    haul.setVolume(equipment.getCapacity());
                }
                double costPerHour = 0;
                if(equipment.getCostPerHour() != null){
                    costPerHour = equipment.getCostPerHour();
                }
                double cost = costPerHour / MILLISECONDS_IN_HOUR * Duration.between(haulStartTime, haulFinishTime).toMillis();
                haul.setCost(cost);
                double revenue = haul.getVolume() * revenueSchedule.get(loadingEvent.getTask());
                haul.setRevenue(revenue);
                double profit = revenue - cost;
                haul.setProfit(profit);
                haulList.add(haul);
                loadingEvent = null;
                unloadingEvent = null;
            }
        }
        return haulList;
    }



    @Override
    public List<HaulSummary> getSummaryList(Integer projectId) {
        List<EquipmentStatus> statusList = getEquipmentStatusForTodayByProjectId(projectId);
        Map<String, Equipment> equipmentMap = projectService.getEquipmentsForProject(projectId).stream().collect(Collectors.toMap(Equipment::getName, p -> p));
        Map<String, Double> taskToRevenueMap = projectService.getTaskRevenueMapForProject(projectId);
        List<Haul> haulList = convertEventsToHauls(statusList,taskToRevenueMap,equipmentMap);
        List<HaulSummary> summaryList = getSummaryFromHauls(haulList);
        return summaryList;
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatusForTodayByProjectId(Integer projectId) {
        Date time = new Date();
        Date startOfDay = getTimeOfBeginningOfToday(time);
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

    private Date getTimeOfBeginningOfToday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public String buildEmailContentFromSummary(List<HaulSummary> summaryList, String template) {

        Context context = new Context();
        context.setVariables(getLoadCoundVariableMap(summaryList));
        return templateEngine.process(template, context);

    }

    @Override
    public Map<String, Object> getLoadCoundVariableMap(List<HaulSummary> summaryList){
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



}
