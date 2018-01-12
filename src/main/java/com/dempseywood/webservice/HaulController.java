package com.dempseywood.webservice;

import com.dempseywood.model.Haul;
import com.dempseywood.model.Project;
import com.dempseywood.model.Task;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.repository.TaskRepository;
import com.dempseywood.specification.HaulSpecs;
import com.dempseywood.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        List<Haul> hauls = new ArrayList<>();
        if (imei == null && fromDate == null && toDate == null) {
            haulRepository.findAll().forEach(hauls::add);
            return hauls;
        } else {
            List<Specification> specs = new ArrayList<Specification>();
            Specifications<Haul> combinedSpecs = null;
            if (imei != null) {
                specs.add(HaulSpecs.hasImei(imei));

            }
            if (fromDate != null) {
                specs.add(HaulSpecs.isAfter(fromDate));
            }
            if (fromDate != null) {
                specs.add(HaulSpecs.isBefore(toDate));
            }

            for (Specification specification : specs) {
                if (combinedSpecs == null) {
                    combinedSpecs = Specifications.where(specification);
                } else {
                    combinedSpecs = combinedSpecs.and(specification);
                }

            }
            haulRepository.findAll(combinedSpecs).forEach(hauls::add);
            return hauls;
        }
    }




    @RequestMapping(method = RequestMethod.GET,value="/{haulId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    ResponseEntity<?> getHaul(@PathVariable Integer haulId){
        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return new ResponseEntity<>(existingHaul, HttpStatus.OK);
        }
    }


    @RequestMapping(method = RequestMethod.POST,  produces = "application/json")
    ResponseEntity<?> add(@RequestBody Haul input) {
            Haul existingHaul = haulRepository.findOneByUuid(input.getUuid());
            if(existingHaul != null){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }else{
                input.setId(null);
                Haul newHaul = haulRepository.save(input);
                return new ResponseEntity<>(newHaul, HttpStatus.CREATED);
            }
    }


    @RequestMapping(value="/{haulId}",method = RequestMethod.PUT,  produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> update(@RequestBody Haul input, @PathVariable Integer haulId) {
        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            existingHaul.setUnloadTime(input.getUnloadTime());
            existingHaul.setUnloadLatitude(input.getUnloadLatitude());
            existingHaul.setUnloadLongitude(input.getUnloadLongitude());
            existingHaul = haulRepository.save(existingHaul);
            return new ResponseEntity<>(existingHaul, HttpStatus.ACCEPTED);
        }

    }

}
