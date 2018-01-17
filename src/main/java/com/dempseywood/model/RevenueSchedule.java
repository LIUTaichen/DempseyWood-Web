package com.dempseywood.model;

import javax.persistence.*;

@Entity
public class RevenueSchedule {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="projectId")
    private Project project;

    private String description;
    private String name;

    private Double revenue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "RevenueSchedule{" +
                "id=" + id +
                ", project=" + project +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", revenue=" + revenue +
                '}';
    }
}
