package com.dempseywood.entity;

import com.dempseywood.entity.LatLng;

import javax.persistence.*;
import java.util.List;

@Entity
public class Geofence {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="geofenceId")
    private List<LatLng> vertices;

    private Integer projectId;


    private String material;
    private String zoneType;
    private String zoneName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<LatLng> getVertices() {
        return vertices;
    }

    public void setVertices(List<LatLng> vertices) {
        this.vertices = vertices;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
