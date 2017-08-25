package com.dempseywood.entity;

import com.dempseywood.entity.Reading;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Track {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer trackerId;

    @OneToMany(mappedBy="trackId")
    private List<Reading> readings = new ArrayList<Reading>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
