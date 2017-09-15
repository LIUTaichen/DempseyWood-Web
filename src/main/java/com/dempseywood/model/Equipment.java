package com.dempseywood.model;


import javax.persistence.*;
import java.util.List;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private String fleetId;
    private Double capacity;
    private String type;
    private Double costPerHour;



    @ManyToMany(mappedBy = "equipments")
    private List<Project> projects;
    public Equipment(String name, Double capacity, Double costPerHour) {
        this.name = name;
        this.capacity = capacity;
        this.costPerHour = costPerHour;
    }

    public Equipment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(Double costPerHour) {
        this.costPerHour = costPerHour;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
