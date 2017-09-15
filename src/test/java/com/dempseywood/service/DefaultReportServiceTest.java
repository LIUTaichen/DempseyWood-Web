package com.dempseywood.service;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReportServiceTest {

    private DefaultReportService service = new DefaultReportService();
    private Map<String, Double> revenueSchedule = new HashMap<>();
    private  Map<String, Equipment> equipmentMap = new HashMap<>();
    @Before
    public void setUp() throws Exception {
        revenueSchedule.put("Top soil", 20d);
        revenueSchedule.put("Unsuitable", 30d);
        revenueSchedule.put("Clay", 30d);
        revenueSchedule.put("Cut to fill", 30d);
        revenueSchedule.put("Other", 30d);

        equipmentMap.put("SJY873", new Equipment("SJY873", 20d, 200d));
        equipmentMap.put("Moxy 4", new Equipment("Moxy 4", 30d, 400d));
        equipmentMap.put("Moxy 5", new Equipment("Moxy 5", 40d, 500d));
    }

    @Test
    public void convertEventsToHaulsValidInput() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        assertEquals(1,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }
    @Test
    public void convertEventsToHaulsUnloadAtBeginningOfDay() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        assertEquals(0,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }
    @Test
    public void convertEventsToHaulsLoadedAtEndOfDay() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        assertEquals(1,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }

    @Test
    public void convertEventsToHaulsMaterialMismatchBetweenLoadingAndUnloading() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Unsuitable", "Unloaded", new Date()));
        assertEquals(1,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());

    }

    @Test
    public void convertEventsToHaulsLoadFollowedByLoad() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Unsuitable", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Unsuitable", "Unloaded", new Date()));
        assertEquals(2,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }@Test
    public void convertEventsToHaulsUnLoadFollowedByUnLoad() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Unsuitable", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Unsuitable", "Unloaded", new Date()));
        assertEquals(2,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }

    @Test
    public void convertEventsToHaulsInvalidMaterial() throws Exception {
    }
    @Test
    public void convertEventsToHaulsInvalidEquipment() throws Exception {
    }
    @Test
    public void convertEventsToHaulsMissingCost() throws Exception {
    }
}