package com.dempseywood.webservice;

import com.dempseywood.model.AppState;
import com.dempseywood.service.AppStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/appstate/")
public class AppStatusController {

    private Logger log = LoggerFactory.getLogger(AppStatusController.class);

    @Autowired
    private AppStateService appStateService;




    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    AppState getAppState(@RequestParam String imei) {
        log.debug("requesting appstate for device with imei: " + imei);

        return appStateService.getAppState(imei);

    }

}
