package com.dempseywood.repository;

import org.springframework.data.repository.CrudRepository;

import com.dempseywood.model.Manager;

public interface ProjectManagerRepository  extends CrudRepository<Manager, Integer>{

	public Manager findOneByEmail(String email);

}
