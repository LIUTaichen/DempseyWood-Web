package com.dempseywood.webservice;

import com.dempseywood.model.Haul;
import com.dempseywood.model.Project;
import com.dempseywood.model.Task;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.repository.TaskRepository;
import com.dempseywood.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javafx.scene.input.KeyCode.H;

@RestController
@RequestMapping("/api/hauls")
public class HaulController {

    private Logger log = LoggerFactory.getLogger(HaulController.class);

    @Autowired
    private HaulRepository haulRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    List<Haul> getHauls(@RequestParam(required = false)  String imei, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        log.debug("calling get hauls");

        //return haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore(imei, fromDate, toDate);
        return null;


    }
}
