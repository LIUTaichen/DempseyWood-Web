package com.dempseywood.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tracker {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer equipmentId;

    @OneToMany(mappedBy="trackerId")
    private List<Track> tracks = new ArrayList<Track>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
