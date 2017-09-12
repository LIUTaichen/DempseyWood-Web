package com.dempseywood.repository;

import org.springframework.data.repository.CrudRepository;

import com.dempseywood.model.ProjectManager;

public interface ProjectManagerRepository  extends CrudRepository<ProjectManager, Integer>{

	public ProjectManager findOneByEmail(String email);

}
