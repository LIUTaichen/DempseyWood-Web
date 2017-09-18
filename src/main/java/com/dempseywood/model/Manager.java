package com.dempseywood.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    
    private String email;
    
    private String name;

	@ManyToMany(mappedBy = "managers")
	private List<Project> projects;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}
