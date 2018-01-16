package com.dempseywood.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Entity
public class UpdateTaskRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String imei;

    private String task;

    private String HaulUuid;
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public String getHaulUuid() {
        return HaulUuid;
    }

    public void setHaulUuid(String haulUuid) {
        HaulUuid = haulUuid;
    }

    @Override
    public String toString() {
        return "UpdateTaskRequest{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", task='" + task + '\'' +
                ", HaulUuid='" + HaulUuid + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
