package com.dempseywood.webservice;

import com.dempseywood.model.Task;
import com.dempseywood.repository.TaskRepository;
import com.dempseywood.service.VersionCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/versionCheck")
public class VersionCheckController {

    private Logger log = LoggerFactory.getLogger(VersionCheckController.class);

    @Autowired
    private VersionCheckService versionCheckService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public @ResponseBody
    Boolean isUpdateRequired(@RequestParam int clientVersion) {
        log.debug("calling get all tasks");

        return versionCheckService.isUpdateNeeded(clientVersion);
    }
}
