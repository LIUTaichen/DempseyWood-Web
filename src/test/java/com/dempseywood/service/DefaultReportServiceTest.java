package com.dempseywood.service;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.model.Haul;
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
        equipmentMap.put("Moxy 6", new Equipment("Moxy 6", 40d, null));
        equipmentMap.put("Moxy 7", new Equipment("Moxy 7", null, 500d));
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
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("Moxy 4", "invalid material", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "invalid material", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        assertEquals(1,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }
    @Test
    public void convertEventsToHaulsInvalidEquipment() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        statusList.add(new EquipmentStatus("invalid equipment", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("invalid equipment", "Top soil", "Unloaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 4", "Top soil", "Unloaded", new Date()));
        assertEquals(1,service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap ).size());
    }
    @Test
    //in case of missing equipment cost information, use 0
    public void convertEventsToHaulsMissingCost() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        //Moxy 6 is an equipment without cost information
        statusList.add(new EquipmentStatus("Moxy 6", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 6", "Top soil", "Unloaded", new Date()));
        List<Haul> resultList = service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap );
        assertEquals(1,resultList.size());
        assertEquals(0, resultList.get(0).getCost(), 0d);

    }

    @Test
    //in case of missing equipment capacity information, use 0
    public void convertEventsToHaulsMissingCapacity() throws Exception {
        List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
        //Moxy 7 is an equipment without capacity information
        statusList.add(new EquipmentStatus("Moxy 7", "Top soil", "Loaded", new Date()));
        statusList.add(new EquipmentStatus("Moxy 7", "Top soil", "Unloaded", new Date()));
        List<Haul> resultList = service.convertEventsToHauls(statusList, revenueSchedule,equipmentMap );
        assertEquals(1,resultList.size());
        assertEquals(0, resultList.get(0).getVolume(), 0d);

    }
}