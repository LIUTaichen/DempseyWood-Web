package com.dempseywood.service;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.model.Haul;
import com.dempseywood.model.HaulSummary;
import org.apache.poi.ss.usermodel.Workbook;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Workbook writeReportForProject(List<EquipmentStatus> statusList, List<Haul> haulList, List<HaulSummary> summaryList);

    List<HaulSummary> getSummaryFromHauls(List<Haul> haulList);

    List<Haul> convertEventsToHauls(List<EquipmentStatus> statusList, Map<String, Double> revenueScheule, Map<String, Equipment> equipmentMap);

    List<HaulSummary> getSummaryList(Integer projectId);

    List<EquipmentStatus> getEquipmentStatusForTodayByProjectId(Integer projectId);

    List<EquipmentStatus> getEquipmentStatusByProjectIdAndTimestamp(Integer projectId, Date startTime, Date endTime );

    String buildEmailContentFromSummary(Map<String, Object> variableMap, String template);

    Map<String, Object> getLoadCountVariableMap(List<HaulSummary> summaryList);

    void sendReportForProject(Integer projectId, Date startTime, Date endTime);

}
