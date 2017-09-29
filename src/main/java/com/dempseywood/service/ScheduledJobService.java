package com.dempseywood.service;

import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScheduledJobService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledJobService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    @Autowired
    private ReportService reportService;

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;

    //@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron="1 0 0 * * 0,2,3,4,5,6", zone="NZ")
    public void generateDailyLoadCountReport() {
        Date currentTime = new Date();
        Date endTime = DateTimeUtil.getInstance().getLastNZMidnight(currentTime);
        Date startTime = DateTimeUtil.getInstance().getTimeOneDayBefore(endTime);
        updateRecords(startTime, endTime);
        log.info("generating report, retreiving records between  : " + dateFormat.format(startTime) + "  and " + dateFormat.format(endTime));
        reportService.sendReportForProject(2,startTime, endTime );
    }
    //TODO;remove method after testing
    @Scheduled(cron="0 * * 29 9 *", zone="NZ")
    public void testCron() {
        log.info("The time is now {}", dateFormat.format(new Date()));

        Date endTime = new Date();
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(endTime);
        startTimeCalendar.add(Calendar.HOUR_OF_DAY, -24);
        Date startTime = startTimeCalendar.getTime();
        log.info("testing between  : " + dateFormat.format(startTime) + "  and " + dateFormat.format(endTime));
    }
    //TODO;remove method after testing
    @Scheduled(cron="0 0,5,10,15,20,25,30,35,40,45,50,55 10,11,12,13,14 29 9 0,2,3,4,5,6", zone="NZ")
    public void testDailyLoadCountReport() {
        Date currentTime = new Date();
        Date endTime = DateTimeUtil.getInstance().getLastNZMidnight(currentTime);
        Date startTime = DateTimeUtil.getInstance().getTimeOneDayBefore(endTime);
        log.info("generating report, retreiving records between  : " + dateFormat.format(startTime) + "  and " + dateFormat.format(endTime));
        updateRecords(startTime, endTime);
        reportService.sendReportForProject(2,startTime, endTime );
    }

    private void updateRecords(Date startTime, Date endTime){
        List<EquipmentStatus> statusList = equipmentStatusRepository.findByTimestampBetweenOrderByOperatorDescTimestamp(startTime, endTime);
        log.info("patching records in database. Found " + statusList.size() + " records.");
        for(int i = 0; i < statusList.size(); i++){
            EquipmentStatus status = statusList.get(i);
            if(status.getImei().equals("353117071081280")){
                status.setEquipment("Dump Truck Ejector - Murph");
            }else if(status.getImei().equals("355078080538132")){
                status.setEquipment("Dump Truck Ejector - Dixon");
            }
        }
        equipmentStatusRepository.save(statusList);
    }

}
