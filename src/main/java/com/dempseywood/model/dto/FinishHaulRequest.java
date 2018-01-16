package com.dempseywood.model.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class FinishHaulRequest {
    @NotNull(message = "unloadLatitude is compulsory")
    @NotBlank(message = "unloadLatitude is compulsory")
    private Double unloadLatitude;
    @NotNull(message = "unloadLongitude is compulsory")
    @NotBlank(message = "unloadLongitude is compulsory")
    private Double unloadLongitude;
    @NotNull(message = "unloadTime is compulsory")
    @NotBlank(message = "unloadTime is compulsory")
    private Date unloadTime;

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

    public Date getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    @Override
    public String toString() {
        return "FinishHaulRequest{" +
                "unloadLatitude=" + unloadLatitude +
                ", unloadLongitude=" + unloadLongitude +
                ", unloadTime=" + unloadTime +
                '}';
    }
}
