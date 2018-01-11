package com.dempseywood.service;

import com.dempseywood.model.Haul;
import com.dempseywood.repository.HaulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HaulService {

    @Autowired
    private HaulRepository haulRepository;

    public List<Haul> findHauls(Haul filter){


        List<Haul> result = new ArrayList<>();
        return result;
    }

}
