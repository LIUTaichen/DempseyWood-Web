package com.dempseywood.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class LatLng {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @JsonProperty("lat")
    public  Double latitude;
    @JsonProperty("lng")
    public Double longitude;

    public LatLng(){

    }

    public LatLng(Double lat, Double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
