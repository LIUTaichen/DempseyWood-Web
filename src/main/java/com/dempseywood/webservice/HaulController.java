package com.dempseywood.webservice;

import com.dempseywood.model.Haul;
import com.dempseywood.model.dto.FinishHaulRequest;
import com.dempseywood.model.dto.StartHaulRequest;
import com.dempseywood.model.UpdateTaskRequest;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.service.HaulService;
import com.dempseywood.specification.HaulSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/hauls")
public class HaulController {

    private Logger log = LoggerFactory.getLogger(HaulController.class);

    @Autowired
    private HaulRepository haulRepository;

    @Autowired
    private HaulService haulService;

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
    ResponseEntity<?> createHaul(@RequestBody StartHaulRequest input, BindingResult results) {

            Haul existingHaul = haulRepository.findOneByUuid(input.getUuid());
            if(existingHaul != null){
                log.info("request with uuid : " + input.getUuid() + " is already processed");
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            boolean isValid = haulService.isValidHaul(input);
            if(!isValid) {
                log.info("Invalid task or equipment");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Haul newHaul = haulService.buildNewHaulFromInput(input);
            return new ResponseEntity<>(newHaul, HttpStatus.CREATED);
    }

    /**
     * Not being used
     * @param input
     * @param haulId
     * @return
     */
    @RequestMapping(value="/{haulId}", method = RequestMethod.PUT,  produces = "application/json")
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


    @RequestMapping(value="/{haulId}/unload", method = RequestMethod.POST,  produces = "application/json")
    ResponseEntity<?> unload(@RequestBody @Valid FinishHaulRequest input, @PathVariable Integer haulId) {

        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Haul newHaul = haulService.updateHaulForUnload(haulId, input);
        return new ResponseEntity<>(newHaul, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/{haulId}/updateTask", method = RequestMethod.POST,  produces = "application/json")
    ResponseEntity<?> updateTask(@RequestBody @Valid UpdateTaskRequest input, @PathVariable Integer haulId) {


        Haul existingHaul = haulRepository.findOne(haulId);
        if(existingHaul ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Haul newHaul = haulService.updateTask(haulId, input);
        return new ResponseEntity<>(newHaul, HttpStatus.ACCEPTED);
    }


}
