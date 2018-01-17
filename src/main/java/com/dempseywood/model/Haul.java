package com.dempseywood.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Haul {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="equipmentId")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name="taskId")
    private Task task;

    private String operator;

    private Double loadLatitude;
    private Double loadLongitude;

    private Double unloadLatitude;
    private Double unloadLongitude;

    private Date loadTime;
    private Date unloadTime;

    private String imei;

    private String uuid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Double getLoadLatitude() {
        return loadLatitude;
    }

    public void setLoadLatitude(Double loadLatitude) {
        this.loadLatitude = loadLatitude;
    }

    public Double getLoadLongitude() {
        return loadLongitude;
    }

    public void setLoadLongitude(Double loadLongitude) {
        this.loadLongitude = loadLongitude;
    }

    public Double getUnloadLatitude() {
        return unloadLatitude;
    }

    public void setUnloadLatitude(Double unloadLatitude) {
        this.unloadLatitude = unloadLatitude;
    }

    public Double getUnloadLongitude() {
        return unloadLongitude;
    }

    public void setUnloadLongitude(Double unloadLongitude) {
        this.unloadLongitude = unloadLongitude;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public Date getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
