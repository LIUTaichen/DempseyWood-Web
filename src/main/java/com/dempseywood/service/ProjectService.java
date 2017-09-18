package com.dempseywood.service;

import com.dempseywood.model.CostSchedule;
import com.dempseywood.model.Equipment;
import com.dempseywood.repository.CostScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dempseywood.model.Project;
import com.dempseywood.model.Manager;
import com.dempseywood.repository.ProjectManagerRepository;
import com.dempseywood.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

	private Logger log = LoggerFactory.getLogger("ProjectService");
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectManagerRepository managerRepository;


	@Autowired
	private CostScheduleRepository costScheduleRepository;
	
	@Transactional
	public Integer getProjectIdFromUserEmail(String email){
		Manager manager = managerRepository.findOneByEmail(email);
		if(manager == null){
			manager = new Manager();
			manager.setEmail( email);
			
			Project project = new Project();
			project = projectRepository.save(project);
			manager.getProjects().add(project);
			manager = managerRepository.save(manager);
		}
		if(manager.getProjects() == null || manager.getProjects().isEmpty()){
			return 1;
		}
		return manager.getProjects().get(0).getId();
		
	}


	public List<Equipment> getEquipmentsForProject(Integer projectId) {
		Project project = projectRepository.findOne(projectId);
		List<Equipment> equipmentList = new ArrayList<>();
		if (project != null) {
			project.getEquipments().forEach(equipment -> {
				equipmentList.add(equipment);
			});
		} else {
			log.error("::getNameToEquipmentMapForProject Unable to find any project with projectId: [" + projectId + "]");
		}
		return equipmentList;
	}

	public Map<String, Double> getTaskRevenueMapForProject(Integer projectId) {
		List<CostSchedule> costScheduleList = costScheduleRepository.findByProjectId(projectId);
		Map<String, Double> revenueScheule = new HashMap<String, Double>();
		for (CostSchedule cost : costScheduleList) {
			revenueScheule.put(cost.getTask(), cost.getRevenue());
		}
		return revenueScheule;
	}

	public List<String> getEmailOfProjectManagers(Integer projectId){
		List<String> emails = new ArrayList<>();
		Project project = projectRepository.findOne(projectId);
		if(project == null || project.getManagers() == null || project.getManagers().isEmpty()){
			//TODO:add logic to handle cases of no email address to send to
			emails.add ("jason.liu@dempseywood.co.nz");
		}else{
			project.getManagers().forEach(manager -> emails.add(manager.getEmail()));
		}
		return emails;
	}


}
