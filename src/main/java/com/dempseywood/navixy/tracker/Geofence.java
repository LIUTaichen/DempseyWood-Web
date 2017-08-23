package com.dempseywood.navixy.tracker;

import javax.persistence.*;
import java.util.List;

@Entity
public class Geofence {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade= CascadeType.ALL, fetch=FetchType.LAZY)
    private List<LatLng> vertices;

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

}
