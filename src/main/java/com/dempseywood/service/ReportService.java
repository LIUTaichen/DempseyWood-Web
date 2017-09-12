package com.dempseywood.service;

import com.dempseywood.model.*;
import com.dempseywood.repository.CostScheduleRepository;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.webservice.geofence.TruckStatus;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


import static com.dempseywood.webservice.geofence.TruckStatus.UNLOADED;

@Service
public class ReportService {


    private Logger log = LoggerFactory.getLogger("ReportService");

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private CostScheduleRepository costScheduleRepository;


    private List<EquipmentStatus> getData() {
        Date time = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        List<EquipmentStatus> statusList = equipmentStatusRepository.findByTimestampBetweenOrderByEquipmentDescTimestamp(startOfDay, time);
        System.out.println(statusList.size());
        return statusList;
    }

    public Workbook writeReportForProject(Integer projectId) {
        List<EquipmentStatus > statusList = getData();
        List<CostSchedule> costScheduleList =costScheduleRepository.findByProjectId(projectId);
        Workbook report =  new SXSSFWorkbook();
        Map<String, HaulSummary> machineToHaulsMap = new HashMap<String, HaulSummary>();
        Map<String, HaulSummary> loadTypeToHaulsMap = new HashMap<String,HaulSummary>();

        new EventsSheetWriter("Full Details").writeSheet(report, statusList);
        List<Haul> haulList = convertEventsToHauls(statusList ,costScheduleList);
        List<HaulSummary> summaryList = getSummary(haulList);
        new HaulsSheetWriter("Hauls").writeHauls(report, haulList) ;
        new SummaryByMachineSheetWriter("Summary By Machine").writeSheet(report, summaryList) ;
        new SummaryByLoadTypeSheetWriter("Summary By Load Type").writeSheet(report, summaryList);
        int index = 0;
        report.setSheetOrder("Summary By Machine", index++);
        report.setSheetOrder("Summary By Load Type", index++);
        report.setSheetOrder("Hauls", index++);
        report.setSheetOrder("Full Details", index++);
        return report;
    }

    private  List<HaulSummary> getSummary(List<Haul> haulList) {

        Map<String, Map<String, HaulSummary>> combined = new HashMap<String, Map<String, HaulSummary>>();

        for(Haul haul: haulList){
            Map<String, HaulSummary> byLoadTypeMap = combined.get(haul.getEquipment());
            if(byLoadTypeMap == null){
                byLoadTypeMap = new HashMap<String, HaulSummary>();
                combined.put(haul.getEquipment(),byLoadTypeMap);
            }
            HaulSummary entry = byLoadTypeMap.get(haul.getLoadType());
            if(entry == null){
                entry = new HaulSummary();
                byLoadTypeMap.put(haul.getLoadType(),entry);
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

    private List<Haul> convertEventsToHauls(List<EquipmentStatus> statusList, List<CostSchedule>  costScheduleList ) {

        Map<String, Double> revenueScheule = new HashMap<String, Double>();
        for(CostSchedule cost: costScheduleList){
            revenueScheule.put(cost.getTask(),cost.getRevenue());
        }
        List<Haul> haulList = new ArrayList<>();

        EquipmentStatus loadingEvent = null;
        EquipmentStatus unloadingEvent = null;
        TruckStatus status = UNLOADED;
        String equipmentStringForCurrentHaul = "";
        for(EquipmentStatus event: statusList) {
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
                Equipment equipment = equipmentRepository.findByName(loadingEvent.getEquipment());
                haul.setLoadTime(loadingEvent.getTimestamp());
                haul.setUnloadTime(unloadingEvent.getTimestamp());
                int indexOfUnloadingEvent = statusList.indexOf(unloadingEvent);
                int indexOfNextEvent = indexOfUnloadingEvent +1;
                LocalDateTime haulStartTime = LocalDateTime.ofInstant(loadingEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                LocalDateTime haulFinishTime = LocalDateTime.ofInstant(unloadingEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                if(indexOfNextEvent < statusList.size()){
                    EquipmentStatus nextEvent = statusList.get(indexOfNextEvent);
                    if(nextEvent.getEquipment().equals(equipmentStringForCurrentHaul)){
                        haulFinishTime = LocalDateTime.ofInstant(nextEvent.getTimestamp().toInstant(), ZoneId.systemDefault());
                    }
                }
                long duration = Duration.between( haulStartTime ,haulFinishTime).toMinutes();
                haul.setDuration(duration);
                haul.setEquipment(equipment.getName());
                haul.setLoadType(loadingEvent.getTask());
                haul.setVolume(equipment.getCapacity());
                double cost =equipment.getCostPerHour() / 60 * duration;
                haul.setCost(cost);
                double revenue = equipment.getCapacity() * revenueScheule.get(loadingEvent.getTask());
                haul.setRevenue(revenue);
                haul.setProfit(revenue - cost);
                haulList.add(haul);
                loadingEvent = null;
                unloadingEvent = null;
            }
        }
        return haulList;
    }


}
