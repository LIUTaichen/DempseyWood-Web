package com.dempseywood.webservice.geofence;

import com.dempseywood.navixy.tracker.Latlng;
import com.dempseywood.webservice.equipmentstatus.EquipmentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geofence")
public class GeofenceController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    String addNewEquipmentStatus(@RequestBody Iterable<Latlng> latlngList) {
        latlngList.forEach(latlng  -> System.out.println(latlng));
        return "Saved";
    }
}
