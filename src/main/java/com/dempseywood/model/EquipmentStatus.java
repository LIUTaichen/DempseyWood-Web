package com.dempseywood.model;


import javax.persistence.*;
import java.util.Date;

@Entity
public class EquipmentStatus {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String equipment;

    private String task;

    private String operator;

    private String status;

    private Double latitude;
    private Double longitude;

    private Date timestamp;
    
    private String imei;


    public EquipmentStatus(String equipment, String task, String status) {
        this.equipment = equipment;
        this.task = task;
        this.status = status;
    }

    public EquipmentStatus(String equipment, String task, String status, Date timestamp) {
        this.equipment = equipment;
        this.task = task;
        this.status = status;
        this.timestamp = timestamp;
    }

    public EquipmentStatus() {
    }

    public Integer getId() {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

    @Override
    public String toString() {
        return "EquipmentStatus{" +
                "id=" + id +
                ", equipment='" + equipment + '\'' +
                ", task='" + task + '\'' +
                ", operator='" + operator + '\'' +
                ", status='" + status + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                ", imei='" + imei + '\'' +
                '}';
    }
}
