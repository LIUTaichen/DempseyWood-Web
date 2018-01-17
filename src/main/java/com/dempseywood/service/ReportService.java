package com.dempseywood.service;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.model.report.HaulReportEntry;
import com.dempseywood.model.report.HaulSummary;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<HaulSummary> getSummaryFromHauls(List<HaulReportEntry> haulReportEntryList);

    List<HaulSummary> getSummaryList(Integer projectId);

    List<EquipmentStatus> getEquipmentStatusForTodayByProjectId(Integer projectId);

    List<EquipmentStatus> getEquipmentStatusByProjectIdAndTimestamp(Integer projectId, Date startTime, Date endTime );

    String buildEmailContentFromSummary(Map<String, Object> variableMap, String template);

    Map<String, Object> getLoadCountVariableMap(List<HaulSummary> summaryList);

    void sendReportForProject(Integer projectId, Date startTime, Date endTime);
    Workbook writeReportForProject(List<HaulReportEntry> haulReportEntryList, List<HaulSummary> summaryList);

}
