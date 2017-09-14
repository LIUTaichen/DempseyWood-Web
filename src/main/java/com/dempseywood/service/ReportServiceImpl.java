package com.dempseywood.service;

import com.dempseywood.model.*;
import com.dempseywood.repository.CostScheduleRepository;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.repository.ProjectRepository;
import com.dempseywood.webservice.geofence.TruckStatus;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


import static com.dempseywood.webservice.geofence.TruckStatus.UNLOADED;

@Service
public class ReportServiceImpl implements ReportService {


    public static final int MILLISECONDS_IN_HOUR = 3600000;
    private Logger log = LoggerFactory.getLogger("ReportServiceImpl");

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private CostScheduleRepository costScheduleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TemplateEngine templateEngine;


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

    @Override
    public List<Haul> convertEventsToHauls(List<EquipmentStatus> statusList, Map<String, Double> revenueScheule, Map<String, Equipment> equipmentMap) {
        List<Haul> haulList = new ArrayList<>();

        EquipmentStatus loadingEvent = null;
        EquipmentStatus unloadingEvent = null;
        String equipmentStringForCurrentHaul = "";
        for (EquipmentStatus event : statusList) {
            //next entry is for another equipment
            if (!event.getEquipment().equals(equipmentStringForCurrentHaul)) {
                equipmentStringForCurrentHaul = event.getEquipment();
                loadingEvent = null;
                unloadingEvent = null;
            }
            if (event.getStatus().equals("Loaded")) {
                loadingEvent = event;
            } else if (event.getStatus().equals("Unloaded")) {
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
                haul.setVolume(equipment.getCapacity());
                double cost = equipment.getCostPerHour() / MILLISECONDS_IN_HOUR * Duration.between(haulStartTime, haulFinishTime).toMillis();
                haul.setCost(cost);
                double revenue = equipment.getCapacity() * revenueScheule.get(loadingEvent.getTask());
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
    public Map<String, Double> getTaskRevenueMapForProject(Integer projectId) {
        List<CostSchedule> costScheduleList = costScheduleRepository.findByProjectId(projectId);
        Map<String, Double> revenueScheule = new HashMap<String, Double>();
        for (CostSchedule cost : costScheduleList) {
            revenueScheule.put(cost.getTask(), cost.getRevenue());
        }
        return revenueScheule;
    }


    @Override
    public List<HaulSummary> getSummaryList(Integer projectId) {
        List<EquipmentStatus> statusList = getEquipmentStatusForTodayByProjectId(projectId);
        Map<String, Equipment> equipmentMap = getEquipmentsForProject(projectId).stream().collect(Collectors.toMap(Equipment::getName, p -> p));
        Map<String, Double> taskToRevenueMap = getTaskRevenueMapForProject(projectId);
        List<Haul> haulList = convertEventsToHauls(statusList,taskToRevenueMap,equipmentMap);
        List<HaulSummary> summaryList = getSummaryFromHauls(haulList);
        return summaryList;
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatusForTodayByProjectId(Integer projectId) {
        Date time = new Date();
        return getEquipmentStatusForDayByProjectId(time, projectId);
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatusForDayByProjectId(Date date, Integer projectId) {
        log.debug("retrieving all equipmentStatus for the project with projectId: [" + projectId + "]");
        List<EquipmentStatus> resultList = new ArrayList<>();
        Project project = projectRepository.findOne(projectId);
        if(project == null){
            log.error("::getNameToEquipmentMapForProject Unable to find any project with projectId: [" + projectId + "]");
            return resultList;
        }
        List<Equipment> equipmentList = this.getEquipmentsForProject(projectId);
        List<String> equipmentNames = equipmentList.stream().map(equipment -> equipment.getName()).collect(Collectors.toList());
        Date startOfDay = getTimeOfBeginningOfToday(date);
        resultList = equipmentStatusRepository.findByTimestampBetweenAndEquipmentInOrderByEquipmentDescTimestamp(startOfDay, date, equipmentNames);
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
        List<HaulSummary> summaryByMachine = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getEquipment).thenComparing(HaulSummary::getLoadType)).collect(Collectors.toList());
        List<HaulSummary> summaryByLoadType = summaryList.stream().sorted(Comparator.comparing(HaulSummary::getLoadType).thenComparing(HaulSummary::getEquipment)).collect(Collectors.toList());
        Context context = new Context();
        context.setVariable("summaryByMachine", summaryByMachine);
        context.setVariable("summaryByLoadType", summaryByLoadType);
        return templateEngine.process(template, context);

    }

    @Override
    public List<Equipment> getEquipmentsForProject(Integer projectId) {
        Project project = projectRepository.findOne(projectId);
        List<Equipment> equipmentList = new ArrayList<>();
        if (project != null) {
            project.getEquipments().forEach(equipment -> {
                equipmentList.add(equipment);
            });
        } else {
            log.error("::getNameToEquipmentMapForProject Unable to find any project with projectId: [" + projectId + "]");
        }
        return equipmentList;
    }

}
