package com.dempseywood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dempseywood.model.Project;
import com.dempseywood.model.ProjectManager;
import com.dempseywood.repository.ProjectManagerRepository;
import com.dempseywood.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectManagerRepository managerRepository;
	
	@Transactional
	public Integer getProjectIdFromUserEmail(String email){
		ProjectManager manager = managerRepository.findOneByEmail(email);
		if(manager == null){
			manager = new ProjectManager();
			manager.setEmail( email);
			
			Project project = new Project();
			project = projectRepository.save(project);
			manager.setProject(project);
			manager = managerRepository.save(manager);
		}
		return manager.getProject().getId();
		
	}
	

}
