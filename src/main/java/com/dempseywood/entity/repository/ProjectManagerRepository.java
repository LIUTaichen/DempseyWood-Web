package com.dempseywood.entity.repository;

import org.springframework.data.repository.CrudRepository;

import com.dempseywood.entity.Equipment;
import com.dempseywood.entity.ProjectManager;

public interface ProjectManagerRepository  extends CrudRepository<ProjectManager, Integer>{

	public ProjectManager findOneByEmail(String email);

}
