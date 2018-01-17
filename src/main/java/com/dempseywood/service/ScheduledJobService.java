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

    @Scheduled(cron="0 0 1 * * *", zone="NZ")
    public void generateDailyLoadCountReport() {
        Date currentTime = new Date();
        Date endTime = DateTimeUtil.getInstance().getLastNZMidnight(currentTime);
        Date startTime = DateTimeUtil.getInstance().getTimeOneDayBefore(endTime);
        log.info("generating report, retreiving records between  : " + dateFormat.format(startTime) + "  and " + dateFormat.format(endTime));
        reportService.sendReportForProject(2,startTime, endTime );
    }


}
