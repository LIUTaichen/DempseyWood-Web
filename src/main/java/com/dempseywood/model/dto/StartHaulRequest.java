package com.dempseywood.model.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class StartHaulRequest {

    @NotNull
    @NotEmpty
    private String equipment;
    @NotNull
    @NotEmpty
    private String task;
    @NotNull
    @NotEmpty
    private String operator;
    @NotNull
    @NotEmpty
    private Double loadLatitude;
    @NotNull
    @NotEmpty
    private Double loadLongitude;
    @NotNull
    @NotEmpty
    private Date loadTime;
    @NotNull
    @NotEmpty
    private String imei;
    @NotNull
    @NotEmpty
    private String uuid;


    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
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

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
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

    @Override
    public String toString() {
        return "StartHaulRequest{" +
                "equipment='" + equipment + '\'' +
                ", task='" + task + '\'' +
                ", operator='" + operator + '\'' +
                ", loadLatitude=" + loadLatitude +
                ", loadLongitude=" + loadLongitude +
                ", loadTime=" + loadTime +
                ", imei='" + imei + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
