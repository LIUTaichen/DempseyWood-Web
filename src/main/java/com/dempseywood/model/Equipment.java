package com.dempseywood.model;


import javax.persistence.*;
import java.util.List;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    @Column(unique = true)
    private String fleetId;
    private Double capacity;
    private String category;
    private Double costPerHour;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fleetId='" + fleetId + '\'' +
                ", capacity=" + capacity +
                ", category='" + category + '\'' +
                ", costPerHour=" + costPerHour +
                '}';
    }
}
